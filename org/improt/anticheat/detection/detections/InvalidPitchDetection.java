package org.improt.anticheat.detection.detections;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.detection.IDetection;
import org.improt.anticheat.punishment.PunishmentFactory;
import org.improt.anticheat.punishment.PunishmentHandler;

public class InvalidPitchDetection extends IDetection {
    @Override
    public String getDetectionName() {
        return "InvalidPlayerPitch";
    }

    @Override
    public void check(PlayerContainer collector, Object context) {
        float pitch = ((PacketEvent) context).getPacket().getFloat().read(1);
        if (pitch > 90.5F || pitch < -90.5F) {
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
