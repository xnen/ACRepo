package org.improt.anticheat.punishment.type;

import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.punishment.PunishmentHandler;

public interface IPunishment {
    PunishmentHandler.PunishmentType getType();
    void punish(PlayerContainer playerContainer, String friendlyDetectionName, Object... metaData);
}
