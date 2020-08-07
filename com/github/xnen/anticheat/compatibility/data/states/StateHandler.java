package com.github.xnen.anticheat.compatibility.data.states;

import org.bukkit.event.Event;
import com.github.xnen.anticheat.compatibility.data.player.container.PlayerContainer;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateFlySpeed;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateFlying;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateGliding;
import com.github.xnen.anticheat.compatibility.data.states.entity.StateSwimming;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateJump;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateLevitation;
import com.github.xnen.anticheat.compatibility.data.states.potion.EffectStateSlowFall;

import java.util.HashMap;
import java.util.Map;

public class StateHandler {
    // TODO: Most of these states don't change per version, so I'm not really bothering to create compatibilities for them. In the future, this should be changed and a factory for certain states per version should be made
    private Map<Class, IState> states = new HashMap<>();


    public void init(PlayerContainer playerContainer) {
        // TODO ... (Register states)
        register(new StateFlying(playerContainer));
        register(new StateFlySpeed(playerContainer));
        register(new StateGliding(playerContainer));
        register(new StateSwimming(playerContainer));
        register(new EffectStateJump(playerContainer));
        register(new EffectStateLevitation(playerContainer));
        register(new EffectStateSlowFall(playerContainer));
    }

    private void register(IState state) {
        this.states.put(state.getClass(), state);
    }

    public IState getState(Class clazz) {
        return this.states.get(clazz);
    }

    /**
     * Pass a bukkit event to test invalidations for each state
     */
    public void passEvent(Event event) {
       this.states.values().forEach(e -> e.accept(event));
    }

    /**
     * Invalidate all states due to the client responding to something the server sent
     */
    public void invalidateAll() {
        this.states.values().forEach(e -> e.reset());
    }

//    public enum StateType {
//        FLYING,
//        FLY_SPEED,
//        FOOD_LEVEL,
//        GLIDING,
//        SWIMMING,
//        WALK_SPEED,
//        DOLPHINS_GRACE,
//        HASTE,
//        JUMP_BOOST,
//        MINING_FATIGUE,
//        SLOW_FALL,
//        SLOWNESS,
//        SPEED
//    }

}
