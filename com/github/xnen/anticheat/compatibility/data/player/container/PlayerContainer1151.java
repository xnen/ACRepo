package com.github.xnen.anticheat.compatibility.data.player.container;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import com.github.xnen.anticheat.AntiCheat;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateFlySpeed;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateFlying;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateGliding;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateSwimming;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateJump;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateLevitation;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateSlowFall;
import com.github.xnen.anticheat.compatibility.data.world.block.MaterialGenerifier;
import com.github.xnen.anticheat.compatibility.data.world.block.type.BasicMaterial;
import com.github.xnen.anticheat.util.VelocityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerContainer1151 extends PlayerContainer {

    // Distance player has jumped since last onGround status
    private double jumpHeight;

    // Last move the player made (PlayerMoveEvent)
    private Vec3D lastMove;

    // # of ticks the player has been in the air
    private int airTicks; // ticksOffGround TODO: Listen on Flying packet instead of Bukkit events
    private Vec3D expectedGlideVector;

    // Velocity Lag Compensation
    private VelocityCollector velocityCollector = new VelocityCollector();
    private double expectedJumpHeight;

    public PlayerContainer1151(Player player) {
        super(player);
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public void sendPacket(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            StructureModifier<Integer> ints = event.getPacket().getIntegers();
            int id = ints.read(0);

            if (id == getContainedPlayer().getEntityId()) {
                double x = ints.read(1) / 8000d;
                double y = ints.read(2) / 8000d;
                double z = ints.read(3) / 8000d;

                this.velocityCollector.addVelocity(x, y, z);
            }
        }

        if (event.getPacketType().equals(PacketType.Play.Server.EXPLOSION)) {
            StructureModifier<Float> floats = event.getPacket().getFloat();

            float x = floats.read(1);
            float y = floats.read(2);
            float z = floats.read(3);

            this.velocityCollector.addVelocity(x, y, z);
        }
    }

    @Override
    public void receivePacket(PacketEvent event) {

    }

    private double maxFriction;
    private double lastFriction;
    private double lastTickYMomentumTest;

    /*
     * Notes
     * AIMoveSpeed = dt();
     */

    @Override
    public void onEvent(Event event) {
        if (this.getContainedPlayer().getPlayer() == null) {
            return;
        }

        // First, we pass events to all states to modify as necessary
        this.stateHandler.passEvent(event);

        if (event instanceof PlayerMoveEvent && !(event instanceof PlayerTeleportEvent)) {
            PlayerMoveEvent moveEvent = ((PlayerMoveEvent) event);

            Location from = moveEvent.getFrom();
            Location to = moveEvent.getTo();

            if (to != null) {
                EntityHuman player = ((CraftPlayer) this.getContainedPlayer().getPlayer()).getHandle();
                Vec3D move = new Vec3D(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());

                System.out.println(getFlyingState().isFlying());
                // TODO: Slow Fall gravity value might only be in effect if move.y <= 0
                double playerGravity = getContainedPlayer().hasGravity() ? (this.getSlowFallState().getAmplifier() > -1 ? GRAVITY_SLOWFALL : WORLD_GRAVITY) : 0;

                // Testing material property collisions & states
                this.footMaterials = this.testMaterials(-0.5000001);
                this.headMaterials = this.testMaterials(player.getHeight() + 0.0625);
                this.headObstructed = this.headMaterials.contains(BasicMaterial.SOLID);

                // onGround status raytrace
                this.onGround = Math.abs(this.raytraceGround(player, move) - move.y) < 0.0625;

                // Testing vertical speeds and jump heights for state invalidation and a convenient node for neural network
                double maxVerticalSpeed = this.getMaxVerticalSpeed(playerGravity, move.y, this.lastTickYMomentumTest);

                // Check for proper speed within a limit
                if (move.y - maxVerticalSpeed <= 0.01) {
                    this.lastTickYMomentumTest = move.y;
                } else {
                    double[] consume = this.velocityCollector.consumeVelocity(0, move.y, 0);

                    // Exceeded expected vert speed, attempt to consume any buffered velocity.
                    if (consume[1] > 0) {
                        // Buffer is dry, invalidate
                        System.out.println("FLAG, excess velocity = " + consume[1] + " move details = " + move.y + " vs " + maxVerticalSpeed);
                        this.lastTickYMomentumTest = maxVerticalSpeed;
                    } else {
                        // Accepted new velocity, re-calculate jump height based on it.
                        this.expectedJumpHeight = this.calculateJumpHeight(move.y, playerGravity);
                        this.lastTickYMomentumTest = move.y;
                    }
                }

                // Update player jump height & air ticks if not flying.
                if (!this.onGround && !this.isAffectedBy(BasicMaterial.CLIMBABLE) && !this.getFlyingState().isFlying() && !this.getGlideState().isGliding()) {
                    if (move.y > 0.0) {
                        this.jumpHeight += move.y;
                    }

                    if (!this.onGround && this.lastTickOnGround) {
                        this.expectedJumpHeight = this.calculateJumpHeight(this.lastTickYMomentumTest, playerGravity);
                    }

                    this.airTicks++;
                } else {
                    this.jumpHeight = 0.0D;
                    this.airTicks = 0;
                }

                // Invalidate glide and flight states if exceeding an expected jump height
                if (this.jumpHeight > this.expectedJumpHeight) {
                    System.out.println("FLAG, jumpHeight");
                    if (getContainedPlayer().isFlying()) {
                        this.getFlyingState().invalidate();
                    }
                    if (getContainedPlayer().isGliding()) {
                        this.getGlideState().invalidate();
                    }
                }

                this.lastTickOnGround = this.onGround;
                this.lastMove = move;
            }
        }
    }

    private Vec3D calculateElytraMove(Vec3D move, float yaw, float pitch) {
        Vec3D vec3d3 = new Vec3D(move.x, move.y, move.z);
        Vec3D vec3d = this.getLookVector(yaw, pitch);

        float f6 = pitch * ((float) Math.PI / 180F);
        double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
        double d11 = Math.sqrt(this.magnifyHorizontal(vec3d3));
        double d12 = vec3d.f();
        float f3 = MathHelper.cos(f6);
        f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));

        double d0 = (move.y <= 0.0 && this.getSlowFallState().getAmplifier() > -1) ? GRAVITY_SLOWFALL : WORLD_GRAVITY;

        if (!getContainedPlayer().hasGravity()) {
            d0 = 0;
        }

        vec3d3 = move.add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);

        if (vec3d3.y < 0.0D && d9 > 0.0D) {
            double d3 = vec3d3.y * -0.1D * (double) f3;
            vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
        }

        if (f6 < 0.0F && d9 > 0.0D) {
            double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
            vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
        }

        if (d9 > 0.0D) {
            vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
        }

        return vec3d3.d(0.99, 0.98, 0.99);
    }

    /**
     * Calculate the maximum vertical speed the player is normally able to go in a given tick, from all of the player's statuses
     * TODO: (CRITICAL) Account for Bubble Columns
     *
     * @param yMotLastTick - Previous velocity
     * @return Maximum vertical speed a player may go in coordinates
     */
    private double getMaxVerticalSpeed(final double gravity, final double yMot, final double yMotLastTick) {
        double vertSpeed = 0.0d;
        // DEBUG: Assure proper flying, swimming state :: TODO
        this.stateHandler.getState(StateSwimming.class).invalidate();

        if (this.getFlyingState().isFlying()) {
            vertSpeed += 0.08 + (this.getFlySpeedState().getFlySpeed() * 3.0D);
        } else if (this.lastTickOnGround) {
            // Jump height
            vertSpeed = 0.6; // Player Step Height

            int jumpBoost = this.getJumpBoostState().getAmplifier();

            if (jumpBoost > -1) {
                vertSpeed += 0.1 * (jumpBoost + 1);
            }
        } else { // Not Flying or onGround, in Air or Liquid. Return proper gravity/friction/liquid movement
            if (this.isAffectedBy(BasicMaterial.UNCOLLIDABLE)) {
                vertSpeed = (Math.abs(yMotLastTick) + 0.08) * 0.98;
            } else if (this.isAffectedBy(BasicMaterial.WATER)) {
                vertSpeed = (Math.abs(yMotLastTick) + 0.04) * 0.8; // Allow last tick motion + climb/descend value
                vertSpeed += 0.0275;

                if (this.getSwimState().isSwimming()) {
                    vertSpeed += 0.066; // Swimming bonus TODO: More precise value checking
                }

                if (vertSpeed < 0.1) {
                    vertSpeed = 0.1;
                }
            } else if (this.isAffectedBy(BasicMaterial.LAVA)) {
                vertSpeed = 0.08; // Lava motion seems tied to gravity

                if (Math.abs(yMot) > vertSpeed) {
                    double d = (((Math.abs(yMotLastTick) * 0.5) + (-gravity / 4d))) + 0.06; // climb/descend value

                    if (vertSpeed < d) {
                        vertSpeed = d;
                    }
                }
            } else if (this.isAffectedBy(BasicMaterial.CLIMBABLE)) {
                vertSpeed = 0.2;
            }
        }

        return vertSpeed;
    }

    private StateSwimming getSwimState() {
        return ((StateSwimming) this.stateHandler.getState(StateSwimming.class));
    }

    private double magnifyHorizontal(Vec3D vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    private Vec3D getLookVector(float yaw, float pitch) {
        float f = pitch * ((float) Math.PI / 180F);
        float f1 = -yaw * ((float) Math.PI / 180F);
        float f2 = MathHelper.cos(f1);
        float f3 = MathHelper.sin(f1);
        float f4 = MathHelper.cos(f);
        float f5 = MathHelper.sin(f);
        return new Vec3D((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
    }

    private List<BasicMaterial> testMaterials(double verticalOffset) {
        MaterialGenerifier generifier = AntiCheat.getInstance().getServerCompat().getMaterialGenerifier();
        List<BasicMaterial> materials = new ArrayList<>();

        Location l = getContainedPlayer().getLocation();

        for (double x = -COLLISION_THRESHOLD; x <= COLLISION_THRESHOLD; x += COLLISION_THRESHOLD) {
            for (double z = -COLLISION_THRESHOLD; z <= COLLISION_THRESHOLD; z += COLLISION_THRESHOLD) {
                BasicMaterial[] mat = generifier.lookup(l.clone().add(x, verticalOffset, z).getBlock().getType());
                for (BasicMaterial basicMaterial : mat) {
                    if (!materials.contains(basicMaterial)) {
                        materials.add(basicMaterial);
                    }
                }
            }
        }

        return materials;
    }

    private double getMotionFromBlock(EntityHuman playerHandle) {
        BlockPosition playerPosition = new BlockPosition(playerHandle.locX(), playerHandle.locY() - 0.20000000298023224D, playerHandle.locZ());
        Vec3D oldMotion = playerHandle.getMot();

        // Check block collision for motion update
        Block block = playerHandle.world.getType(playerPosition).getBlock();
        block.a(playerHandle.world, playerHandle);

        double mot = playerHandle.getMot().y;

        // TODO: Just run the method from the block? (SlimeBlock?) And grab its velocity from there instead of modifying player velocity and changing it back un-safely.
        playerHandle.setMot(oldMotion);

        return mot;
    }

    private VoxelShapeCollision collisionEntity;

    private double raytraceGround(EntityHuman human, Vec3D motion) {
        if (collisionEntity == null) {
            collisionEntity = VoxelShapeCollision.a(human);
        }

        AxisAlignedBB bounds = human.getBoundingBox();
        StreamAccumulator<VoxelShape> accumulator = new StreamAccumulator<>(human.world.b(human, bounds.a(motion), (Set) ImmutableSet.of()));
        return VoxelShapes.a(EnumDirection.EnumAxis.Y, bounds, human.world, motion.y - 0.0625D, collisionEntity, accumulator.a()); //Entity.a(human, motion.add(0, -0.0625D, 0), bounds, human.world, collisionEntity, accumulator);
    }

    private EffectStateJump getJumpBoostState() {
        return ((EffectStateJump) this.stateHandler.getState(EffectStateJump.class));
    }

    private EffectStateLevitation getLevitationState() {
        return ((EffectStateLevitation) this.stateHandler.getState(EffectStateLevitation.class));
    }

    private EffectStateSlowFall getSlowFallState() {
        return ((EffectStateSlowFall) this.stateHandler.getState(EffectStateSlowFall.class));
    }

    // TODO: Flying seems to update slowly (multiple ticks after toggle it invalidates?)
    private StateFlying getFlyingState() {
        return ((StateFlying) this.stateHandler.getState(StateFlying.class));
    }

    private StateGliding getGlideState() {
        return ((StateGliding) this.stateHandler.getState(StateGliding.class));
    }

    private StateFlySpeed getFlySpeedState() {
        return ((StateFlySpeed) this.stateHandler.getState(StateFlySpeed.class));
   }

    /**
     * Calculates the maximum height the player is able to achieve from the first momentum value given
     *
     * @param initialMomentum - The initial vertical momentum the player has left the ground with
     * @return - The maximum height relative to the ground (1.28 blocks... 2.0 blocks... etc)
     */
    private double calculateJumpHeight(double initialMomentum, double gravity) {
        double jumpHeight = 0.0D;

        while (initialMomentum > 0.0d) {
            jumpHeight += initialMomentum;
            initialMomentum = (initialMomentum - gravity) * AIRFRICTION;
        }

        return jumpHeight;
    }

    private boolean isAffectedBy(BasicMaterial material) {
        switch (material) {
            case LAVA:
            case WATER:
            case SLOW_VERTICAL:
            case BUBBLE_COLUMN:
            case CLIMBABLE:
            case UNCOLLIDABLE:
                return this.headMaterials.contains(material) || this.footMaterials.contains(material);

            case SOLID:
            case SLIPPERY:
            case SLOW_HORIZONTAL:
                return this.footMaterials.contains(material);
                default: return false;
        }
    }

    // TODO:
    private float maxElytraSpeed;

    // Player statuses
    private boolean onGround;
    private boolean lastTickOnGround;

    private boolean headObstructed;

    private List<BasicMaterial> headMaterials;
    private List<BasicMaterial> footMaterials;
}
