package com.github.xnen.anticheat.compatibility.methodwrapper;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface IMethodWrapper {
    boolean checkPermission(Player player, String permission);

    boolean isVehicleControllable(Player player, Entity vehicle);

    boolean isHoldingItem(Player player, Material item);

    boolean isSaddled(Entity horse);
}
