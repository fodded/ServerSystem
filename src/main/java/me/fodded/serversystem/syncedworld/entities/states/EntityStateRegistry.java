package me.fodded.serversystem.syncedworld.entities.states;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityStateRegistry {

    private final Map<Class, AbstractEntityState> states = new ConcurrentHashMap<>();
    public void registerState(AbstractEntityState state) {
        states.put(state.getClass(), state);
    }

    public AbstractEntityState getState(Class classType) {
        return states.get(classType);
    }
}