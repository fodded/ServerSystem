package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import me.fodded.serversystem.ServerSystem;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MinecraftPlayerMovePacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerMovePacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION, PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION_LOOK);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        UUID spawnedPlayerUUID = event.getPlayer().getUniqueId();

        double x = packet.getDoubles().read(0);
        double y = packet.getDoubles().read(1);
        double z = packet.getDoubles().read(2);
        float yaw = packet.getFloat().read(0);
        float pitch = packet.getFloat().read(1);

        String formattedMessage = spawnedPlayerUUID.toString() + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
        plugin.getRedisClient().sendMessage("playerMove", formattedMessage);
    }
}
