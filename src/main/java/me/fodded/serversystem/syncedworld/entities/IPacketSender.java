package me.fodded.serversystem.syncedworld.entities;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface IPacketSender {

    default Consumer<Packet> sendPacketEveryone() {
        return arg -> Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            PlayerConnection playerConnection = craftPlayer.getHandle().playerConnection;
            playerConnection.sendPacket(arg);
        });
    }

    default Consumer<Packet> sendPacket(Player... players) {
        return arg -> {
            for(Player player : players) {
                CraftPlayer craftPlayer = (CraftPlayer) player;
                PlayerConnection playerConnection = craftPlayer.getHandle().playerConnection;
                playerConnection.sendPacket(arg);
            }
        };
    }
}
