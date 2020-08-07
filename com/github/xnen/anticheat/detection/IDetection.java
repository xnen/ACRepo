package com.github.xnen.anticheat.detection;

import com.github.xnen.anticheat.compatibility.ServerCompat;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

public abstract class IDetection {
    boolean active = true;

    /**
     * Test if the Detection is compatible with the server version. Defaults true
     */
    boolean compatibleWith(ServerCompat.Version version) { return true; }

    public abstract String getDetectionName();
    protected abstract void check(PlayerContainer collector, Object context);
    protected abstract boolean isRegistered(Object context);

    public abstract PunishmentHandler.PunishmentType getDefaultPunishment();
}
