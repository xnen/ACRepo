package com.github.xnen.anticheat.compatibility.permission;

import org.bukkit.entity.Player;
import com.github.xnen.anticheat.AntiCheat;

public class PermissionsVer1151 implements IPermissions {
    public boolean hasPermission(Player player, String permission) {
        return AntiCheat.getInstance().getServerCompat().getMethodWrapper().checkPermission(player, permission);
    }
}
