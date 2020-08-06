package org.improt.anticheat.compatibility.permission;

import org.bukkit.entity.Player;
import org.improt.anticheat.AntiCheat;

public interface IPermissions {
    String EXEMPT = AntiCheat.getInstance().getPluginProxy().getName() + "." + "exempt";
    boolean hasPermission(Player player, String permission);
}
