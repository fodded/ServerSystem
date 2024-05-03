package me.fodded.serversystem.data.impl;

import lombok.Getter;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayerTracker;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityTabVisibilityState;
import me.fodded.serversystem.syncedworld.info.impl.PlayerDisconnectPacket;
import me.fodded.serversystem.syncedworld.info.impl.PlayerJoinPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    @Getter
    private final UUID playerUUID;
    private static final Map<UUID, PlayerManager> cachedPlayerManager = new HashMap<>();

    public PlayerManager(UUID playerUUID) {
        this.playerUUID = playerUUID;
        cachedPlayerManager.put(playerUUID, this);
    }

    // Method is called after the data has loaded from the database
    public void handleJoin() {
        Player player = Bukkit.getPlayer(playerUUID);
        ServerSystem plugin = ServerSystem.getInstance();

        // Loading all previously created fake entity players
        for(SyncedEntityPlayer syncedEntityPlayer : plugin.getSyncedWorldManager().getRegisteredSyncedEntities()) {
            syncedEntityPlayer.getEntityStateRegistry().getState(EntityTabVisibilityState.class).enable(player);
        }

        // Telling other server connected to the plugin that the player joined the server
        PlayerJoinPacket playerJoinPacket = new PlayerJoinPacket(playerUUID, player.getDisplayName());
        plugin.getRedisClient().sendMessage("playerJoined", playerJoinPacket.serializePacketInfo());
    }

    public void handleQuit() {
        Player player = Bukkit.getPlayer(playerUUID);
        SyncedEntityPlayerTracker.getInstance().clearEntitiesCacheToPlayer(playerUUID);

        // Telling other server connected to the plugin that the player left the server
        PlayerDisconnectPacket playerDisconnectPacket = new PlayerDisconnectPacket(playerUUID, player.getPlayerListName());
        ServerSystem.getInstance().getRedisClient().sendMessage("playerQuit", playerDisconnectPacket.serializePacketInfo());

        cachedPlayerManager.remove(playerUUID);
    }

    public static PlayerManager getPlayerManager(UUID playerUUID) {
        return cachedPlayerManager.getOrDefault(playerUUID, new PlayerManager(playerUUID));
    }
}
