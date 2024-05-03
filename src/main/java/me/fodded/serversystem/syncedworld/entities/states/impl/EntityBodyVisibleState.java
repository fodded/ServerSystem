package me.fodded.serversystem.syncedworld.entities.states.impl;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayerTracker;
import me.fodded.serversystem.syncedworld.entities.states.AbstractEntityState;
import me.fodded.serversystem.syncedworld.entities.states.EntityStateRegistry;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.entity.Player;

public class EntityBodyVisibleState extends AbstractEntityState {

    private final SyncedEntityPlayer syncedEntityPlayer;
    public EntityBodyVisibleState(Entity entity, EntityStateRegistry entityStateRegistry) {
        super(entity, entityStateRegistry);
        this.syncedEntityPlayer = (SyncedEntityPlayer) entity;
    }

    @Override
    public void enable(Player... players) {
        sendPacket(players).accept(new PacketPlayOutNamedEntitySpawn(syncedEntityPlayer));
        for(Player player : players) {
            SyncedEntityPlayerTracker.getInstance().trackEntity(player, syncedEntityPlayer);
        }
    }

    @Override
    public void disable(Player... players) {
        sendPacket(players).accept(new PacketPlayOutEntityDestroy(syncedEntityPlayer.getId()));
        for(Player player : players) {
            SyncedEntityPlayerTracker.getInstance().untrackEntity(player, syncedEntityPlayer);
        }
    }
}
