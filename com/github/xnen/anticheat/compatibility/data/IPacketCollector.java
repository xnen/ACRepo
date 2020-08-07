package com.github.xnen.anticheat.compatibility.data;

import com.comphenix.protocol.events.PacketEvent;

public interface IPacketCollector extends IEventCollector {
    void sendPacket(PacketEvent event);
    void receivePacket(PacketEvent event);
}
