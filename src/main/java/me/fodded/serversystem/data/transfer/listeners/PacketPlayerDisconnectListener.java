package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerDisconnectListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        String[] arrMessage = message.split(":");

        UUID playerUUID = UUID.fromString(arrMessage[0]);
        String playerDisplayName = arrMessage[1];

        SyncedWorldManager syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        SyncedEntityPlayer syncedEntity = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntity == null) {
            return;
        }

        Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);
        syncedEntity.hideEntity(players);
        syncedEntity.hideTabName(players);
        syncedWorldManager.unregisterSyncedEntity(syncedEntity.getUniqueID());

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> eachPlayer.sendMessage(ChatUtil.format("&e" + playerDisplayName + " left the server")));
    }
}
