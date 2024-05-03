package me.fodded.serversystem.listeners.bukkit;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.impl.PlayerDataManager;
import me.fodded.serversystem.data.impl.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnectListener implements Listener {

    private final ServerSystem plugin;
    public PlayerConnectListener(ServerSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
        playerDataManager.getPlayerData(playerUUID).thenAccept(playerData -> {
            if(!player.isOnline()) {
                return;
            }
            PlayerManager.getPlayerManager(playerUUID).handleJoin();
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
        playerDataManager.getPlayerData(playerUUID).thenAccept(playerData -> {
            PlayerManager.getPlayerManager(playerUUID).handleQuit();

            plugin.getDataStorage().saveData(playerData);
            playerDataManager.invalidatePlayerData(player.getUniqueId());
        });
    }
}
