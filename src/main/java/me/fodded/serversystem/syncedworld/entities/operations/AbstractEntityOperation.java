package me.fodded.serversystem.syncedworld.entities.operations;

import net.minecraft.server.v1_8_R3.Entity;

public abstract class AbstractEntityOperation implements PacketEntityOperation {

    protected final Entity entity;
    protected final EntityOperationRegistry entityOperationRegistry;

    public AbstractEntityOperation(Entity entity, EntityOperationRegistry entityOperationRegistry) {
        this.entity = entity;
        this.entityOperationRegistry = entityOperationRegistry;
    }
}