package org.improt.anticheat.compatibility.data.states.potion;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.compatibility.data.states.IState;

public class EffectStateJump extends IState {
    private PlayerContainer playerContainer;

    public EffectStateJump(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    private int amplifier;

    public int getAmplifier() {
        int i = (int) grabState();

        // Only invalidate the speed if lower amplifier as jump boost gives advantage to jump height.
        if (i > this.amplifier) {
            this.reset();
        } else if (i < this.amplifier) {
            // TODO: Invalidate when jumping @ an expected jump height
            this.addPendingState(i);
        }

        this.amplifier = i;

        if (!this.simulatedState.isValid())
            this.invalidate();

        return (int) this.simulatedState.getState();
    }

    @Override
    protected void accept(Event event) {
        // ...
    }

    @Override
    protected Object grabState() {
        PotionEffect potionEffect = this.playerContainer.getContainedPlayer().getPotionEffect(PotionEffectType.JUMP);
        if (potionEffect == null) {
            return -1;
        } else {
            return potionEffect.getAmplifier();
        }
    }
}
