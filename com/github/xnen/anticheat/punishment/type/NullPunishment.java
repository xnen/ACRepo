package com.github.xnen.anticheat.punishment.type;

import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

public class NullPunishment implements IPunishment {
    @Override
    public PunishmentHandler.PunishmentType getType() {
        return PunishmentHandler.PunishmentType.NONE;
    }

    @Override
    public void punish(PlayerContainer playerContainer, String friendlyDetectionName, Object... metaData) {
        // ... (No punishments)
    }
}
