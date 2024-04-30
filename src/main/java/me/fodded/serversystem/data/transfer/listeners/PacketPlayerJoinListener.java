package me.fodded.serversystem.data.transfer.listeners;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.fakeworld.FakeEntityPlayer;
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

        Player player = Bukkit.getPlayer(playerUUID);
        if(player != null) {
            return;
        }

        GameProfile gameProfile = new GameProfile(playerUUID, playerName);
        CraftWorld craftWorld = (CraftWorld) Bukkit.getWorld("world");

        FakeEntityPlayer fakeEntityPlayer = new FakeEntityPlayer(craftWorld, gameProfile);
        fakeEntityPlayer.showTabName(Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new));

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> {
            eachPlayer.sendMessage(ChatUtil.format("&e" + playerName + " joined the server"));
        });
    }
}
