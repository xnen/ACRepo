package com.github.xnen.anticheat.punishment.type;

import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.detection.IDetection;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

public class KickPunishment implements IPunishment {

    private IDetection detectionContext;

    public KickPunishment(IDetection detectionContext) {
        this.detectionContext = detectionContext;
    }

    @Override
    public void punish(PlayerContainer playerContainer, String friendlyDetectionName, Object... metaData) {

    }

    @Override
    public PunishmentHandler.PunishmentType getType() {
        return PunishmentHandler.PunishmentType.KICK;
    }
}
