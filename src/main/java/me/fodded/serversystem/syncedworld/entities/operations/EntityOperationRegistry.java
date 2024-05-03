package me.fodded.serversystem.syncedworld.entities.operations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityOperationRegistry {

    private final Map<Class, AbstractEntityOperation> states = new ConcurrentHashMap<>();
    public void registerState(AbstractEntityOperation state) {
        states.put(state.getClass(), state);
    }

    public AbstractEntityOperation getState(Class classType) {
        return states.get(classType);
    }
}