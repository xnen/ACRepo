package com.github.xnen.anticheat.punishment;

import com.github.xnen.anticheat.detection.IDetection;
import com.github.xnen.anticheat.settings.Settings;

import java.util.HashMap;
import java.util.Map;

public class PunishmentHandler {
    private Map<IDetection, PunishmentType> punishmentTypeLookup = new HashMap<>();

    public Map<IDetection, PunishmentType> getPunishmentTypeLookup() {
        return punishmentTypeLookup;
    }

    public void init(Settings settings) {
        // Load punishment type lookups from config
        this.punishmentTypeLookup = settings.getPunishmentTypeMap();
    }

    public enum PunishmentType {
         CUSTOM(-1)
        ,NONE(0)
        ,NOTIFY(1)
        ,KICK(2)
        ,BAN(3)
        ;

        int severity;
        PunishmentType(int severity) {
            this.severity = severity;
        }

        public int getSeverity() {
            return severity;
        }
    }
}
