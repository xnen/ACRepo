package org.improt.anticheat.punishment.type;

import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.detection.IDetection;
import org.improt.anticheat.punishment.PunishmentHandler;

public class CommandPunishment implements IPunishment {

    //TODO: Verify this is not a "strange" command, such as Game-mode or Op. This should be handled cautiously as it could be an entry method for exploits
    private String unformattedCommand;
    private IDetection detectionContext;

    public CommandPunishment(IDetection detectionContext, String unformattedCustomPunishCommand) {
        this.unformattedCommand = unformattedCustomPunishCommand;
        this.detectionContext = detectionContext;
    }

    @Override
    public void punish(PlayerContainer playerContainer, String friendlyDetectionName, Object... metaData) {
        //TODO: Format command and run punishment
    }

    @Override
    public PunishmentHandler.PunishmentType getType() {
        return PunishmentHandler.PunishmentType.CUSTOM;
    }
}
