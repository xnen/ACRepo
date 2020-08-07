package com.github.xnen.anticheat.detection.detections;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.detection.IDetection;
import com.github.xnen.anticheat.punishment.PunishmentFactory;
import com.github.xnen.anticheat.punishment.PunishmentHandler;

public class InvalidPitchDetection extends IDetection {
    
    private final float MAX_PITCH = 90.0001F;
    
    @Override
    public String getDetectionName() {
        return "InvalidPlayerPitch";
    }

    @Override
    public void check(PlayerContainer collector, Object context) {
        float pitch = ((PacketEvent) context).getPacket().getFloat().read(1);
        if (pitch > MAX_PITCH || pitch < -MAX_PITCH) {
            PunishmentFactory.createPunishment(this).punish(collector, "Invalid Player Pitch");
        }
    }

    @Override
    public boolean isRegistered(Object context) {
        if (context instanceof PacketEvent) {
            PacketType type = ((PacketEvent) context).getPacketType();
            return (type == PacketType.Play.Client.POSITION_LOOK || type == PacketType.Play.Client.LOOK);
        }

        return false;
    }

    @Override
    public PunishmentHandler.PunishmentType getDefaultPunishment() {
        return PunishmentHandler.PunishmentType.KICK;
    }
}
