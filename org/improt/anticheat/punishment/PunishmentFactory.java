package org.improt.anticheat.punishment;

import org.improt.anticheat.AntiCheat;
import org.improt.anticheat.detection.IDetection;
import org.improt.anticheat.punishment.type.*;
import org.improt.anticheat.settings.Settings;

public class PunishmentFactory {
    public static IPunishment createPunishment(IDetection detectionContext) {
        AntiCheat antiCheat = AntiCheat.getInstance();

        switch (antiCheat.getPunishments().getPunishmentTypeLookup().get(detectionContext)) {
            case CUSTOM:
                Settings settings = AntiCheat.getInstance().getSettings();
                return new CommandPunishment(detectionContext, settings.getUnformattedCustomPunishCommandFor(detectionContext));
            case NOTIFY:
                return new NotifyPunishment(detectionContext);
            case KICK:
                return new KickPunishment(detectionContext);
            case BAN:
                return new BanPunishment(detectionContext);

            case NONE:
            default:
                return new NullPunishment();
        }
    }
}
