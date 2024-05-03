package me.fodded.serversystem.syncedworld.entities.states.impl;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.states.AbstractEntityState;
import me.fodded.serversystem.syncedworld.entities.states.EntityStateRegistry;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import org.bukkit.entity.Player;

public class EntitySneakState extends AbstractEntityState {

    private final SyncedEntityPlayer syncedEntityPlayer;
    public EntitySneakState(Entity entity, EntityStateRegistry entityStateRegistry) {
        super(entity, entityStateRegistry);
        this.syncedEntityPlayer = (SyncedEntityPlayer) entity;
    }

    @Override
    public void enable(Player... players) {
        DataWatcher dataWatcher = syncedEntityPlayer.getDataWatcher();
        dataWatcher.watch(0, 0x02);
        sendPacket(players).accept(new PacketPlayOutEntityMetadata(syncedEntityPlayer.getId(), dataWatcher, true));
    }

    @Override
    public void disable(Player... players) {
        DataWatcher dataWatcher = syncedEntityPlayer.getDataWatcher();
        dataWatcher.watch(0, 0);
        sendPacket(players).accept(new PacketPlayOutEntityMetadata(syncedEntityPlayer.getId(), dataWatcher, true));
    }
}
