package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayerTracker;
import me.fodded.serversystem.syncedworld.entities.operations.impl.EntityAnimationOperation;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerAnimationListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        PlayerAnimationPacket playerAnimationPacket = new PlayerAnimationPacket(message);

        UUID playerUUID = playerAnimationPacket.getUuid();

        SyncedWorldManager syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        SyncedEntityPlayer syncedEntityPlayer = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntityPlayer == null) {
            return;
        }

        Player[] players = SyncedEntityPlayerTracker.getInstance().getPlayersEntityTrackedFor(syncedEntityPlayer).stream().map(Player::getPlayer).toArray(Player[]::new);
        syncedEntityPlayer.getEntityOperationRegistry().getState(EntityAnimationOperation.class).operate(playerAnimationPacket, players);
    }
}
