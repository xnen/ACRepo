package org.improt.anticheat.punishment.type;

import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.punishment.PunishmentHandler;

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
