package me.fodded.serversystem.syncedworld.info.impl;

import lombok.Getter;
import me.fodded.serversystem.syncedworld.info.PacketInfo;

import java.util.UUID;

@Getter
public class PlayerAnimationPacket extends PacketInfo {

    private final UUID uuid;
    private final int animationType;

    public PlayerAnimationPacket(UUID uuid, int animationType) {
        this.uuid = uuid;
        this.animationType = animationType;
    }

    public PlayerAnimationPacket(String message) {
        PlayerAnimationPacket playerAnimationPacket = (PlayerAnimationPacket) this.deserializePacketInfo(message, this);
        this.uuid = playerAnimationPacket.uuid;
        this.animationType = playerAnimationPacket.animationType;
    }
}
