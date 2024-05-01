package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class PacketPlayerMoveListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        Bukkit.getScheduler().runTask(ServerSystem.getInstance(), () -> handle((String) msg));
    }

    private void handle(String message) {
        String[] arrMessage = message.split(":");
        UUID playerUUID = UUID.fromString(arrMessage[0]);

        double x = Double.parseDouble(arrMessage[1]);
        double y = Double.parseDouble(arrMessage[2]);
        double z = Double.parseDouble(arrMessage[3]);
        float yaw = Float.parseFloat(arrMessage[4]);
        float pitch = Float.parseFloat(arrMessage[5]);

        SyncedWorldManager syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        SyncedEntityPlayer syncedEntityPlayer = syncedWorldManager.getSyncedEntityPlayer(playerUUID);
        if(syncedEntityPlayer == null) {
            return;
        }

        World world = Bukkit.getWorld("world");
        Location location = new Location(world, x, y, z);
        Chunk locationChunk = location.getChunk();

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> {
            if (!locationChunk.isLoaded()) {
                syncedEntityPlayer.hideEntity(eachPlayer);
            } else {
                syncedEntityPlayer.moveEntity(eachPlayer, x, y, z, yaw, pitch);
            }
        });
    }
}
