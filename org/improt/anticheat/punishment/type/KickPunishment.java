package org.improt.anticheat.punishment.type;

import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.detection.IDetection;
import org.improt.anticheat.punishment.PunishmentHandler;

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
