package org.improt.anticheat.compatibility.methodwrapper;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.PlayerInventory;

public class MethodWrapper1151 implements IMethodWrapper {
    @Override
    public boolean checkPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean isVehicleControllable(Player player, Entity vehicle) {
        return (vehicle instanceof Pig && this.isHoldingItem(player, Material.CARROT_ON_A_STICK))
                || (vehicle instanceof Horse && this.isSaddled(vehicle))
                || (vehicle instanceof Boat);
    }

    @Override
    public boolean isHoldingItem(Player player, Material item) {
        PlayerInventory inventory = player.getInventory();
        return inventory.getItemInMainHand().getType() == item || inventory.getItemInOffHand().getType() == item;
    }

    @Override
    public boolean isSaddled(Entity horse) {
        return (!(horse instanceof AbstractHorse)) || ((AbstractHorse) horse).getInventory().getSaddle() != null;
    }


}
