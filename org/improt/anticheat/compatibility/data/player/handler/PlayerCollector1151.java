package org.improt.anticheat.compatibility.data.player.handler;

import org.bukkit.entity.Player;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer1151;

public class PlayerCollector1151 extends PlayerCollector {
    @Override
    PlayerContainer createVersionedContainer(Player player) {
        return new PlayerContainer1151(player);
    }
}
