package me.fodded.serversystem.data.transfer.listeners;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityTabVisibilityState;
import me.fodded.serversystem.syncedworld.info.impl.PlayerJoinPacket;
import me.fodded.serversystem.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerJoinListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        PlayerJoinPacket playerJoinPacket = new PlayerJoinPacket(message);

        UUID playerUUID = playerJoinPacket.getUuid();
        String playerName = playerJoinPacket.getPlayerDisplayName();

        // We do not want to create a copy of a player on the server where he is already present himself
        if(Bukkit.getPlayer(playerUUID) != null) {
            return;
        }

        // Register an entity copy from the recently joined player
        GameProfile gameProfile = new GameProfile(playerUUID, playerName);
        CraftWorld craftWorld = (CraftWorld) Bukkit.getWorld("world");

        Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);

        SyncedEntityPlayer syncedEntityPlayer = new SyncedEntityPlayer(craftWorld, gameProfile);
        syncedEntityPlayer.getEntityStateRegistry().getState(EntityTabVisibilityState.class).enable(players);

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> eachPlayer.sendMessage(ChatUtil.format("&e" + playerName + " joined the server")));
    }
}
