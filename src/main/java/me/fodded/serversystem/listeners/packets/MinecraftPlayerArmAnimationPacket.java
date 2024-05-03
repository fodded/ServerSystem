package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MinecraftPlayerArmAnimationPacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerArmAnimationPacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.ARM_ANIMATION);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        PlayerAnimationPacket playerAnimationPacket = new PlayerAnimationPacket(player.getUniqueId(), 0);
        plugin.getRedisClient().sendMessage("playerAnimation", playerAnimationPacket.serializePacketInfo());
    }
}
