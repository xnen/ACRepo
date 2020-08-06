package org.improt.anticheat.text.component;

import org.bukkit.entity.Player;

public class PlayerComponent extends TranslationComponent<Player> {
    public PlayerComponent() {
        super("player");
    }

    public String convert(Player object) {
        return object.getName();
    }
}
