package org.improt.anticheat.text.component;

import org.improt.anticheat.punishment.PunishmentReason;

public class ReasonTextComponent extends TranslationComponent<PunishmentReason> {
    public ReasonTextComponent() {
        super("reason");
    }

    @Override
    String convert(PunishmentReason object) {
        return object.getReason();
    }
}
