package org.improt.anticheat.compatibility.data.states;

import org.improt.anticheat.AntiCheat;

public final class StateObj {
    private final long time;
    private final Object state;

    /**
     * State "value" object to hold when the state was updated, and what value it has
     */
    public StateObj(Object state, long time) {
        this.state = state;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public Object getState() {
        return state;
    }

    public boolean isValid() {
        return this.time == -1L || System.currentTimeMillis() - this.time < AntiCheat.NETTY_TIMEOUT_DELAY;
    }
}
