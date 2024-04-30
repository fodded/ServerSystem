package me.fodded.serversystem.data.storage;

import me.fodded.serversystem.data.impl.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IDataStorage {

    void connect();

    CompletableFuture<PlayerData> loadData(UUID uuid);
    CompletableFuture<Void> saveData(PlayerData playerData);
}
