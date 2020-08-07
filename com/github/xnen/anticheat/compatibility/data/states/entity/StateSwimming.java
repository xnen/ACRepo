package com.github.xnen.anticheat.compatibility.data.states.entity;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.compatibility.data.states.IState;

public class StateSwimming extends IState {
    private PlayerContainer playerContainer;

    public StateSwimming(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    public boolean isSwimming() {
        if (!this.simulatedState.isValid())
            this.invalidate();

        return (boolean) this.simulatedState.getState();
    }

    @Override
    protected void accept(Event event) {
        // ...
    }

    @Override
    protected Object grabState() {
        return playerContainer.getContainedPlayer().isSwimming();
    }
}
