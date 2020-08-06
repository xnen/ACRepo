package org.improt.anticheat.compatibility.data.player.container;

import org.bukkit.entity.Player;
import org.improt.anticheat.compatibility.data.IPacketCollector;
import org.improt.anticheat.compatibility.data.states.StateHandler;

public abstract class PlayerContainer implements IPacketCollector {
    // Threshold in which a player can be in a block and collide with it
    static final double COLLISION_THRESHOLD = 0.3;
    static final double GRAVITY_SLOWFALL = 0.01D;
    static final double WORLD_GRAVITY = 0.08;
    static final double AIRFRICTION = 0.99;

    // State manager to assure that we are synchronized with their client's states
    final StateHandler stateHandler;

    private final Player containedPlayer;

    PlayerContainer(Player player) {
        this.containedPlayer = player;

        this.stateHandler = new StateHandler();
        this.stateHandler.init(this);
    }

    public Player getContainedPlayer() {
        return containedPlayer;
    }

    public abstract boolean isOnGround();
}
