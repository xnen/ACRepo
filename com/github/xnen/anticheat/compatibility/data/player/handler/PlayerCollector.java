package com.github.xnen.anticheat.compatibility.data.player.handler;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import com.github.xnen.anticheat.compatibility.data.IPacketCollector;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;

import java.util.HashMap;
import java.util.Map;

public abstract class PlayerCollector implements IPacketCollector {
    private Map<Player, PlayerContainer> containerList;

    PlayerCollector() {
        containerList = new HashMap<>();
    }

    private PlayerContainer getOrCreateContainer(Player player) {
        if (!containerList.containsKey(player)) {
            this.containerList.put(player, this.createVersionedContainer(player));
        }

        return containerList.get(player);
    }

    @Override
    public void sendPacket(PacketEvent event) {
        this.getOrCreateContainer(event.getPlayer()).sendPacket(event);
    }

    @Override
    public void receivePacket(PacketEvent event) {
        this.getOrCreateContainer(event.getPlayer()).receivePacket(event);
    }

    @Override
    public void onEvent(Event event) {
        Player player = this.getPlayerFromEvent(event);

        if (player != null) {
            this.getOrCreateContainer(player).onEvent(event);
        }
    }

    // TODO: Maybe needs handled individually by each versioned collector
    private Player getPlayerFromEvent(Event event) {
        if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer();
        } else if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
            return (Player) ((EntityDamageByEntityEvent) event).getDamager();
        } else if (event instanceof InventoryInteractEvent && ((InventoryInteractEvent) event).getWhoClicked() instanceof Player) {
            return (Player) ((InventoryInteractEvent) event).getWhoClicked();
        } else if (event instanceof VehicleMoveEvent && ((VehicleMoveEvent) event).getVehicle().getPassengers().size() > 0 && ((VehicleMoveEvent) event).getVehicle().getPassengers().get(0) instanceof Player) {
            return (Player) ((VehicleMoveEvent) event).getVehicle().getPassengers().get(0);
        } else if (event instanceof VehicleEnterEvent && ((VehicleEnterEvent) event).getEntered() instanceof Player) { // TODO: Double check this is the person who entered, and not the vehicle that was entered
            return (Player) ((VehicleEnterEvent) event).getEntered();
        }

        return null;
    }

    abstract PlayerContainer createVersionedContainer(Player player);
}
