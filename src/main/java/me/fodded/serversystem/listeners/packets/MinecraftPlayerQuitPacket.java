package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import me.fodded.serversystem.ServerSystem;
import org.bukkit.plugin.Plugin;

public class MinecraftPlayerQuitPacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerQuitPacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Login.Server.DISCONNECT);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        String formattedMessage = event.getPlayer().getUniqueId().toString();
        plugin.getRedisClient().sendMessage("playerQuit", formattedMessage);
    }
}
