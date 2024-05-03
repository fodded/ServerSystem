package me.fodded.serversystem.syncedworld.info.impl;

import lombok.Getter;
import me.fodded.serversystem.syncedworld.info.PacketInfo;

import java.util.UUID;

@Getter
public class PlayerMovementPacket extends PacketInfo {

    private final UUID uuid;
    private final double x, y, z;
    private final float yaw, pitch;

    public PlayerMovementPacket(UUID uuid, double x, double y, double z, float yaw, float pitch) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PlayerMovementPacket(String message) {
        PlayerMovementPacket playerMovementPacket = (PlayerMovementPacket) this.deserializePacketInfo(message, this);
        this.uuid = playerMovementPacket.uuid;
        this.x = playerMovementPacket.x;
        this.y = playerMovementPacket.y;
        this.z = playerMovementPacket.z;
        this.yaw = playerMovementPacket.yaw;
        this.pitch = playerMovementPacket.pitch;
    }
}
