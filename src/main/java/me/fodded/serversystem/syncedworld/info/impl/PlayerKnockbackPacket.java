package me.fodded.serversystem.syncedworld.info.impl;

import lombok.Getter;
import me.fodded.serversystem.syncedworld.info.PacketInfo;

import java.util.UUID;

@Getter
public class PlayerKnockbackPacket extends PacketInfo {

    private final UUID uuid;
    private final double damage, knockbackX, knockbackY, knockbackZ;

    public PlayerKnockbackPacket(UUID uuid, double damage, double knockbackX, double knockbackY, double knockbackZ) {
        this.uuid = uuid;
        this.damage = damage;
        this.knockbackX = knockbackX;
        this.knockbackY = knockbackY;
        this.knockbackZ = knockbackZ;
    }

    public PlayerKnockbackPacket(String message) {
        PlayerKnockbackPacket playerKnockbackPacket = (PlayerKnockbackPacket) this.deserializePacketInfo(message, this);
        this.uuid = playerKnockbackPacket.uuid;
        this.damage = playerKnockbackPacket.damage;
        this.knockbackX = playerKnockbackPacket.knockbackX;
        this.knockbackY = playerKnockbackPacket.knockbackY;
        this.knockbackZ = playerKnockbackPacket.knockbackZ;
    }
}
