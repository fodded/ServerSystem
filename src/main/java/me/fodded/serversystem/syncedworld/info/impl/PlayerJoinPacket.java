package me.fodded.serversystem.syncedworld.info.impl;

import lombok.Getter;
import me.fodded.serversystem.syncedworld.info.PacketInfo;

import java.util.UUID;

/**
 * This packet is used to inform other servers that they must spawn an entityplayer copy of the joined player
 */
@Getter
public class PlayerJoinPacket extends PacketInfo {

    private final UUID uuid;
    private final String playerDisplayName;

    public PlayerJoinPacket(UUID uuid, String playerDisplayName) {
        this.uuid = uuid;
        this.playerDisplayName = playerDisplayName;
    }

    public PlayerJoinPacket(String message) {
        PlayerJoinPacket playerJoinPacket = (PlayerJoinPacket) this.deserializePacketInfo(message, this);
        this.uuid = playerJoinPacket.uuid;
        this.playerDisplayName = playerJoinPacket.playerDisplayName;
    }
}