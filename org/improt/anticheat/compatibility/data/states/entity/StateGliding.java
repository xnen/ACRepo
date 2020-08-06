package org.improt.anticheat.compatibility.data.states.entity;

import org.bukkit.event.Event;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.compatibility.data.states.IState;

public class StateGliding extends IState {
    private PlayerContainer playerContainer;

    public StateGliding(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    private boolean gliding;

    public boolean isGliding() {
        boolean b = (boolean) this.grabState();

        // TODO: Invalidate when onGround = true, or if falling @ a normal rate
        if (b != this.gliding) {
            if (b) {
                this.reset();
            } else {
                this.addPendingState(false);
            }

            this.gliding = b;
        }

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
        return playerContainer.getContainedPlayer().isGliding();
    }
}
