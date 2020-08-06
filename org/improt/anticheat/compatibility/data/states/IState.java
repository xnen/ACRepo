package org.improt.anticheat.compatibility.data.states;

import org.bukkit.event.Event;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import java.util.LinkedList;


public abstract class IState {
    protected LinkedList<StateObj> pendingStates = new LinkedList<>();
    protected StateObj simulatedState;

    protected abstract void accept(Event event);

    /**
     * Grabs the current SERVER value of the state. This method should be called synchronously with the server thread, as it usually references Bukkit APIs.
     */
    protected abstract Object grabState();

    /**
     * Add a state that the server updated but the client may not have received that update yet. These states will only be
     */
    protected void addPendingState(Object state) {
        this.pendingStates.add(new StateObj(state, System.currentTimeMillis()));
    }

    /**
     * Invalidate to the latest updated state
     */
    public void reset() {
        if (this.pendingStates.size() == 0) {
            this.invalidate();
            return;
        }

        this.simulatedState = this.pendingStates.getLast();
        this.pendingStates.clear();
    }

    /**
     * Poll the oldest state in the pending states and set it as the simulated state.
     * If there are no valid states left, the state will return to its default state.
     */
    public void invalidate() {
        this.simulatedState = pendingStates.pollFirst();

        if (this.simulatedState == null) {
            this.simulatedState = new StateObj(this.grabState(), -1L);
        } else if (!this.simulatedState.isValid()) {
            invalidate();
        }
    }

    public boolean isPending() {
        return this.pendingStates.size() > 0;
    }
}
