package com.github.xnen.anticheat.compatibility.permission;

import org.bukkit.entity.Player;
import com.github.xnen.anticheat.AntiCheat;

public interface IPermissions {
    String EXEMPT = AntiCheat.getInstance().getPluginProxy().getName() + "." + "exempt";
    boolean hasPermission(Player player, String permission);
}
