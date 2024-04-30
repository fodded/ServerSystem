package me.fodded.serversystem.data.impl;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import me.fodded.serversystem.ServerSystem;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PlayerDataManager {

    @Getter
    private static final PlayerDataManager instance = new PlayerDataManager();
    private final AsyncLoadingCache<UUID, PlayerData> playerDataCache;

    private PlayerDataManager() {
        this.playerDataCache = Caffeine.newBuilder().buildAsync(this::loadPlayerData);
    }

    public CompletableFuture<PlayerData> getPlayerData(UUID uuid) {
        return this.playerDataCache.get(uuid);
    }

    public void invalidatePlayerData(UUID uuid) {
        this.playerDataCache.synchronous().invalidate(uuid);
    }

    private CompletableFuture<PlayerData> loadPlayerData(UUID uuid, Executor executor) {
        return ServerSystem.getInstance().getDataStorage().loadData(uuid);
    }
}
