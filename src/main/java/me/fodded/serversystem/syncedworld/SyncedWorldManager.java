package me.fodded.serversystem.syncedworld;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncedWorldManager {

    private final Map<UUID, SyncedEntityPlayer> registeredFakeEntities = new HashMap<>();

    public void registerFakeEntity(UUID uuid, SyncedEntityPlayer entityPlayer) {
        registeredFakeEntities.put(uuid, entityPlayer);
    }

    public void unregisterSyncedEntity(UUID uuid) {
        registeredFakeEntities.remove(uuid);
    }

    public SyncedEntityPlayer getSyncedEntityPlayer(UUID uuid) {
        return registeredFakeEntities.get(uuid);
    }

    public SyncedEntityPlayer getSyncedEntityPlayer(int entityId) {
        for(SyncedEntityPlayer syncedEntityPlayer : getRegisteredSyncedEntities()) {
            if(syncedEntityPlayer.getId() == entityId) {
                return syncedEntityPlayer;
            }
        }
        return null;
    }

    public Collection<SyncedEntityPlayer> getRegisteredSyncedEntities() {
        return registeredFakeEntities.values();
    }
}
