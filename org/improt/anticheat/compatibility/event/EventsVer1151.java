package org.improt.anticheat.compatibility.event;

//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.ProtocolManager;
//import com.comphenix.protocol.events.ListenerPriority;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.improt.anticheat.AntiCheat;
import org.improt.anticheat.PluginProxy;

import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Client;
import static com.comphenix.protocol.PacketType.Play.Server;

public class EventsVer1151 implements IEvents, Listener {

    private AntiCheat antiCheatInstance;

    private static PacketType[] PACKET_LISTENERS = {
            // Outgoing Packet Types
              Server.ENTITY_VELOCITY
            , Server.EXPLOSION
//            , Server.ENTITY_TELEPORT
//            , Server.VEHICLE_MOVE
//            , Server.POSITION
//            , Server.ENTITY_MOVE_LOOK

            // Incoming Packet Types
//            , Client.TELEPORT_ACCEPT
//            , Client.POSITION_LOOK
//            , Client.STEER_VEHICLE
//            , Client.ARM_ANIMATION
//            , Client.ENTITY_ACTION
//            , Client.WINDOW_CLICK
//            , Client.VEHICLE_MOVE
//            , Client.BLOCK_PLACE
//            , Client.USE_ENTITY
//            , Client.BLOCK_DIG
//            , Client.POSITION
//            , Client.USE_ITEM
//            , Client.FLYING
//            , Client.LOOK
            ,
    };

    @Override
    public void register(AntiCheat antiCheat) {
        PluginProxy plugin = antiCheat.getPluginProxy();

        // Register Spigot/Bukkit Events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Register Packet-Related Events
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PACKET_LISTENERS) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.isCancelled()) return;
                antiCheat.getServerCompat().getPlayerDataCollector().receivePacket(event);
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.isCancelled()) return;
                antiCheat.getServerCompat().getPlayerDataCollector().sendPacket(event);
            }
        });

        this.antiCheatInstance = antiCheat;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAnimationEvent(PlayerAnimationEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChangedMainHand(PlayerChangedMainHandEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChangedWorldEvent(PlayerChangedWorldEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCommandSendEvent(PlayerCommandSendEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDropItemEvent(PlayerDropItemEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEditBookEvent(PlayerEditBookEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onBlockDamageEvent(BlockDamageEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onInteractEvent(PlayerInteractEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onItemHeldEvent(PlayerItemHeldEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onMoveEvent(PlayerMoveEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onQuitEvent(PlayerQuitEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onRespawnEvent(PlayerRespawnEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onTeleportEvent(PlayerTeleportEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onToggleSneakEvent(PlayerToggleSneakEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onToggleFlightEvent(PlayerToggleFlightEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onToggleSprintEvent(PlayerToggleSprintEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onVehicleMove(VehicleMoveEvent event) {
        List<Entity> vehiclePassengers = event.getVehicle().getPassengers();
        if (vehiclePassengers.size() == 0 || !(vehiclePassengers.get(0) instanceof Player)) return;

        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player)) return;
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onPistonExtendEvent(BlockPistonExtendEvent event) {
        this.antiCheatInstance.getServerCompat().passBukkitEvent(event);
    }

}
