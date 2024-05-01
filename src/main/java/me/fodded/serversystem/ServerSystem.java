package me.fodded.serversystem;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import me.fodded.serversystem.data.storage.IDataStorage;
import me.fodded.serversystem.data.storage.mongo.MongoStorage;
import me.fodded.serversystem.data.transfer.RedisClient;
import me.fodded.serversystem.listeners.PlayerConnectListener;
import me.fodded.serversystem.listeners.packets.MinecraftPlayerMovePacket;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ServerSystem extends JavaPlugin {

    @Getter
    private static ServerSystem instance;

    private IDataStorage dataStorage;
    private RedisClient redisClient;
    private ProtocolManager protocolManager;

    private final SyncedWorldManager syncedWorldManager = new SyncedWorldManager();

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerConnectListener(instance), instance);
        protocolManager.addPacketListener(new MinecraftPlayerMovePacket(instance));

        initializeDataStorage();
        initializeDataTransfer();
    }

    private void initializeDataStorage() {
        dataStorage = new MongoStorage();
        dataStorage.connect();
    }

    private void initializeDataTransfer() {
        redisClient = new RedisClient("localhost", 6379);
        redisClient.registerAllListeners();
    }

    @Override
    public void onDisable() {

    }
}
