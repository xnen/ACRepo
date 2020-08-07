package com.github.xnen.anticheat.punishment.type;

import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

public interface IPunishment {
    PunishmentHandler.PunishmentType getType();
    void punish(PlayerContainer playerContainer, String friendlyDetectionName, Object... metaData);
}
