package com.github.xnen.anticheat.settings;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import com.github.xnen.anticheat.AntiCheat;
import com.github.xnen.anticheat.detection.IDetection;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private Map<String, Object> settings = new HashMap<>();

    public void loadSettings() {
        // ... (TODO)
    }

    public boolean isDetectionEnabled(IDetection detection) {
        return Boolean.parseBoolean(String.valueOf(this.settings.get(detection.getDetectionName() + ".enabled")));
    }

    //TODO: Load PunishmentType lookups from the config and return it as a map
    public Map<IDetection, PunishmentHandler.PunishmentType> getPunishmentTypeMap() {
        Map<IDetection, PunishmentHandler.PunishmentType> lookupMap = new HashMap<>();

        for (IDetection detection : AntiCheat.getInstance().getDetectionCaller().getAllDetections()) {
            lookupMap.put(detection, detection.getDefaultPunishment());
        }

        return lookupMap;
    }

    //TODO: Load Unformatted Custom Punish Text from config file
    public String getUnformattedCustomPunishCommandFor(IDetection detectionContext) {
        return "testcommand %player% %detection% %message%";
    }

    //TODO: Load translation map from config(?) file
    public Map<String, String> getTranslationMap() {
        return null;
    }
}
