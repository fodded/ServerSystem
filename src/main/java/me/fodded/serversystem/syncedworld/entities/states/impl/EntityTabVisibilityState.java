package me.fodded.serversystem.syncedworld.entities.states.impl;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.states.AbstractEntityState;
import me.fodded.serversystem.syncedworld.entities.states.EntityStateRegistry;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.entity.Player;

public class EntityTabVisibilityState extends AbstractEntityState {

    private final SyncedEntityPlayer syncedEntityPlayer;

    public EntityTabVisibilityState(Entity entity, EntityStateRegistry entityStateRegistry) {
        super(entity, entityStateRegistry);
        this.syncedEntityPlayer = (SyncedEntityPlayer) entity;
    }

    @Override
    public void enable(Player... players) {
        sendPacket(players).accept(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, syncedEntityPlayer));
    }

    @Override
    public void disable(Player... players) {
        sendPacket(players).accept(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, syncedEntityPlayer));
    }
}
