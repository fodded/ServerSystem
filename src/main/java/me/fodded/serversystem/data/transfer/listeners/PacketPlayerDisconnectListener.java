package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.fakeworld.FakeEntityPlayer;
import me.fodded.serversystem.fakeworld.FakeWorldManager;
import me.fodded.serversystem.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerDisconnectListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        UUID playerUUID = UUID.fromString(message);

        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null) {
            return;
        }

        FakeWorldManager fakeWorldManager = ServerSystem.getInstance().getFakeWorldManager();
        FakeEntityPlayer foundFakeEntity = fakeWorldManager.getFakeEntityPlayer(playerUUID);
        if(foundFakeEntity == null) {
            return;
        }

        Player[] players = Bukkit.getOnlinePlayers().stream().map(Player::getPlayer).toArray(Player[]::new);
        foundFakeEntity.hideEntity(players);
        foundFakeEntity.hideTabName(players);
        foundFakeEntity.clearFakeEntitiesToPlayer(player);

        Bukkit.getOnlinePlayers().forEach(eachPlayer -> {
            eachPlayer.sendMessage(ChatUtil.format("&e" + player.getDisplayName() + " left the server"));
        });
    }
}
