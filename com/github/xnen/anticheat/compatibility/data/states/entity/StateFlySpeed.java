package com.github.xnen.anticheat.compatibility.data.states.entity;

import org.bukkit.event.Event;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.compatibility.data.states.IState;

public class StateFlySpeed extends IState {
    private PlayerContainer playerContainer;
    private float flySpeed;

    public StateFlySpeed(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    public float getFlySpeed() {
        float f = (float) this.grabState();

        // Only invalidate the speed if slows as fly speed's advantage lies in how fast it goes
        if (f > this.flySpeed) {
           this.reset();
        } else if (f < this.flySpeed) {
            // TODO: Invalidate when travelling at an expected fly speed
            this.addPendingState(f);
        }

        this.flySpeed = f;

        if (!this.simulatedState.isValid())
            this.invalidate();

        return (float) this.simulatedState.getState();
    }

    @Override
    protected void accept(Event event) {
        // ... We don't need to accept any events as there are no events for flySpeed updates ( TODO: Performance? )
    }

    @Override
    protected Object grabState() {
        return playerContainer.getContainedPlayer().getFlySpeed();
    }
}
