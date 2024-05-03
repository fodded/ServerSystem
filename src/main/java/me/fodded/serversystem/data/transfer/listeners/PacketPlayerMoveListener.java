package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.operations.impl.EntityMovementOperation;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityTabVisibilityState;
import me.fodded.serversystem.syncedworld.info.impl.PlayerMovementPacket;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerMoveListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        // We need to handle the code synchronously with the main server's thread, otherwise it throws exception trying to add a player asynchronously
        Bukkit.getScheduler().runTask(ServerSystem.getInstance(), () -> handle((String) msg));
    }

    private void handle(String message) {
        PlayerMovementPacket playerMovementPacket = new PlayerMovementPacket(message);
        UUID playerUUID = playerMovementPacket.getUuid();

        double x = playerMovementPacket.getX();
        double z = playerMovementPacket.getZ();

        SyncedWorldManager syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        SyncedEntityPlayer syncedEntityPlayer = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntityPlayer == null) {
            return;
        }

        World world = Bukkit.getWorld("world");
        Chunk locationChunk = world.getChunkAt((int) x, (int) z);

        Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);
        if (!locationChunk.isLoaded()) {
            syncedEntityPlayer.getEntityStateRegistry().getState(EntityTabVisibilityState.class).disable(players);
        } else {
            syncedEntityPlayer.getEntityOperationRegistry().getState(EntityMovementOperation.class).operate(playerMovementPacket, players);
        }
    }
}
