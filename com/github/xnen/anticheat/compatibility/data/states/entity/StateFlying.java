package com.github.xnen.anticheat.compatibility.data.states.entity;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.compatibility.data.states.IState;

public class StateFlying extends IState {
    private PlayerContainer playerContainer;

    public StateFlying(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
        this.reset();
    }

    public boolean isFlying() {
        if (!this.simulatedState.isValid())
            this.invalidate();

        return (boolean) this.simulatedState.getState();
    }

    @Override
    protected void accept(Event event) {
        if (event instanceof PlayerMoveEvent) {
            if (this.isFlying()) {
                if (this.playerContainer.isOnGround()) {
                    this.invalidate(); // Cannot be onGround while flying client-side
                }
            }
        }

        if (event instanceof PlayerToggleFlightEvent) {
            // TODO: This method of testing flight may be incompatible with earlier versions.
            boolean flying = ((PlayerToggleFlightEvent) event).isFlying();

            // Only invalidate when TRUE -> FALSE as you only get an advantage with how high you can go.
            if (flying) {
                this.reset();
            } else {
                System.out.println("Bingo?");
                // TODO: Invalidate when falling at an expected rate or player sends onGround = true? (Is there ever a case outside of SPECTATOR where player is flying and onGround is true?
                this.addPendingState(false);
            }
        }
    }

    @Override
    protected Object grabState() {
        return playerContainer.getContainedPlayer().isFlying();
    }
}
