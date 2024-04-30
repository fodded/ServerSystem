package me.fodded.serversystem.data.storage.mysql;

import me.fodded.serversystem.data.impl.PlayerData;
import me.fodded.serversystem.data.storage.IDataStorage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// Has not been implemented yet
public class MySQLStorage implements IDataStorage {

    @Override
    public void connect() {

    }

    @Override
    public CompletableFuture<PlayerData> loadData(UUID uuid) {
        return null;
    }

    @Override
    public CompletableFuture<Void> saveData(PlayerData playerData) {
        return null;
    }
}
