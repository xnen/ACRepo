package com.github.xnen.anticheat.text.component;

import com.github.xnen.anticheat.punishment.PunishmentReason;

public class ReasonTextComponent extends TranslationComponent<PunishmentReason> {
    public ReasonTextComponent() {
        super("reason");
    }

    @Override
    String convert(PunishmentReason object) {
        return object.getReason();
    }
}
