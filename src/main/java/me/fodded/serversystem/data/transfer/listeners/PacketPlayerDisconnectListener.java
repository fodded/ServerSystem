package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.states.EntityStateRegistry;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityBodyVisibleState;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityTabVisibilityState;
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
        SyncedEntityPlayer syncedEntityPlayer = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntityPlayer == null) {
            return;
        }

        EntityStateRegistry entityStateRegistry = syncedEntityPlayer.getEntityStateRegistry();
        Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);

        entityStateRegistry.getState(EntityBodyVisibleState.class).disable(players);
        entityStateRegistry.getState(EntityTabVisibilityState.class).disable(players);

        syncedWorldManager.unregisterSyncedEntity(syncedEntityPlayer.getUniqueID());
        Bukkit.getOnlinePlayers().forEach(eachPlayer -> eachPlayer.sendMessage(ChatUtil.format("&e" + playerDisplayName + " left the server")));
    }
}
