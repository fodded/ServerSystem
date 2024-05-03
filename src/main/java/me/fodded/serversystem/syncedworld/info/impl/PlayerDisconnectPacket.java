package me.fodded.serversystem.syncedworld.info.impl;

import lombok.Getter;
import me.fodded.serversystem.syncedworld.info.PacketInfo;

import java.util.UUID;

/**
 * This packet is used to inform other servers that they must despawn the entityplayer copy of the joined player
 */
@Getter
public class PlayerDisconnectPacket extends PacketInfo {

    private final UUID uuid;
    private final String playerDisplayName;

    public PlayerDisconnectPacket(UUID uuid, String playerDisplayName) {
        this.uuid = uuid;
        this.playerDisplayName = playerDisplayName;
    }

    public PlayerDisconnectPacket(String message) {
        PlayerDisconnectPacket playerJoinPacket = (PlayerDisconnectPacket) this.deserializePacketInfo(message, this);
        this.uuid = playerJoinPacket.uuid;
        this.playerDisplayName = playerJoinPacket.playerDisplayName;
    }
}