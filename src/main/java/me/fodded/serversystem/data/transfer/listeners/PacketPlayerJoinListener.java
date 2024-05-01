package me.fodded.serversystem.data.transfer.listeners;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerJoinListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        String[] arrMessage = message.split(":");

        UUID playerUUID = UUID.fromString(arrMessage[0]);
        String playerName = arrMessage[1];

        // We do not want to create a copy of a player on the server where he is already present himself
        if(Bukkit.getPlayer(playerUUID) != null) {
            return;
        }

        // Register an entity copy from the recently joined player
        GameProfile gameProfile = new GameProfile(playerUUID, playerName);
        CraftWorld craftWorld = (CraftWorld) Bukkit.getWorld("world");

        SyncedEntityPlayer syncedEntityPlayer = new SyncedEntityPlayer(craftWorld, gameProfile);
        syncedEntityPlayer.showTabName(Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new));

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> {
            eachPlayer.sendMessage(ChatUtil.format("&e" + playerName + " joined the server"));
        });
    }
}
