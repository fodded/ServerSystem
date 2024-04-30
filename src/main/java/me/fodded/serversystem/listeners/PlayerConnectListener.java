package me.fodded.serversystem.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.impl.PlayerDataManager;
import me.fodded.serversystem.fakeworld.FakeEntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerConnectListener implements Listener {

    private final ServerSystem plugin;
    public PlayerConnectListener(ServerSystem plugin) {
        this.plugin = plugin;
    }

    // TODO: move the logic from this method to a separate PlayerManager class
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
        playerDataManager.getPlayerData(player.getUniqueId()).thenAccept(playerData -> {
            FakeEntityPlayer foundEntityPlayer = plugin.getFakeWorldManager().getFakeEntityPlayer(player.getUniqueId());
            if(foundEntityPlayer != null) {
                Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);
                foundEntityPlayer.hideTabName(players);
                foundEntityPlayer.hideEntity(players);
                plugin.getFakeWorldManager().unregisterFakeEntity(player.getUniqueId());
            }

            for(FakeEntityPlayer fakeEntityPlayer : plugin.getFakeWorldManager().getRegisteredFakeEntities()) {
                fakeEntityPlayer.showTabName(player);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
        playerDataManager.getPlayerData(player.getUniqueId()).thenAccept(playerData -> {
            ServerSystem.getInstance().getDataStorage().saveData(playerData);
            playerDataManager.invalidatePlayerData(player.getUniqueId());
        });
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
        playerDataManager.getPlayerData(player.getUniqueId()).thenAccept(playerData -> {
            //
        });
    }
}
