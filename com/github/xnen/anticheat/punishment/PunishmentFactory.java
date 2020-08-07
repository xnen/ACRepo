package com.github.xnen.anticheat.punishment;

import com.github.xnen.anticheat.AntiCheat;
import com.github.xnen.anticheat.detection.IDetection;
import com.github.xnen.anticheat.punishment.type.*;
import com.github.xnen.anticheat.settings.Settings;

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
