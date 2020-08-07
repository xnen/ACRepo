package com.github.xnen.anticheat.compatibility.data.states.potion;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.compatibility.data.states.IState;

public class EffectStateLevitation extends IState {
    private PlayerContainer playerContainer;

    public EffectStateLevitation(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    private int amplifier;

    // TODO: Invalidate when stopped rising at the expected rate and not in any materials

    public int getAmplifier() {
        int i = (int) grabState();

        if (i != this.amplifier) {
            this.addPendingState(i);
            this.amplifier = i;
        }

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
