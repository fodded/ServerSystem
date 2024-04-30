package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.fodded.serversystem.ServerSystem;
import org.bukkit.plugin.Plugin;

public class MinecraftPlayerJoinPacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerJoinPacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Login.Server.SUCCESS);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrappedGameProfile gameProfile = packet.getGameProfiles().read(0);

        String formattedMessage = gameProfile.getUUID().toString() + ":" + gameProfile.getName();
        plugin.getRedisClient().sendMessage("playerJoined", formattedMessage);
    }
}
