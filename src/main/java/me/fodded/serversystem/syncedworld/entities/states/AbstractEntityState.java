package me.fodded.serversystem.syncedworld.entities.states;

import net.minecraft.server.v1_8_R3.Entity;

public abstract class AbstractEntityState implements ToggleableEntityState {

    protected final Entity entity;
    protected final EntityStateRegistry entityStateRegistry;

    public AbstractEntityState(Entity entity, EntityStateRegistry entityStateRegistry) {
        this.entity = entity;
        this.entityStateRegistry = entityStateRegistry;
    }
}
