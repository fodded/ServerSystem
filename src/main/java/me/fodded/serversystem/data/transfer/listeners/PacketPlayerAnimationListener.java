package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayerTracker;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerAnimationListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        String[] messageArray = message.split(":");

        UUID playerUUID = UUID.fromString(messageArray[0]);
        int animationType = Integer.parseInt(messageArray[1]);

        SyncedWorldManager syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        SyncedEntityPlayer syncedEntityPlayer = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntityPlayer == null) {
            return;
        }

        if(animationType == 2) {
            syncedEntityPlayer.sneakEntity();
            return;
        }

        if(animationType == 3) {
            syncedEntityPlayer.unSneakEntity();
            return;
        }

        Player[] players = SyncedEntityPlayerTracker.getInstance().getPlayersEntityTrackedFor(syncedEntityPlayer).stream().map(Player::getPlayer).toArray(Player[]::new);
        syncedEntityPlayer.playAnimation(animationType, players);
    }
}
