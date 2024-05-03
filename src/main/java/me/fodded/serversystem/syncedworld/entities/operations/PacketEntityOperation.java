package me.fodded.serversystem.syncedworld.entities.operations;

import me.fodded.serversystem.syncedworld.entities.IPacketSender;
import me.fodded.serversystem.syncedworld.info.PacketInfo;
import org.bukkit.entity.Player;

public interface PacketEntityOperation extends IPacketSender {
    <T extends PacketInfo> void operate(T packetInfo, Player... players);
}
