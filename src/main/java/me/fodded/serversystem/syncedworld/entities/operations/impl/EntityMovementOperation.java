package me.fodded.serversystem.syncedworld.entities.operations.impl;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayerTracker;
import me.fodded.serversystem.syncedworld.entities.operations.AbstractEntityOperation;
import me.fodded.serversystem.syncedworld.entities.operations.EntityOperationRegistry;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityBodyVisibleState;
import me.fodded.serversystem.syncedworld.info.PacketInfo;
import me.fodded.serversystem.syncedworld.info.impl.PlayerMovementPacket;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.entity.Player;

public class EntityMovementOperation extends AbstractEntityOperation {

    private final SyncedEntityPlayerTracker syncedEntityPlayerTracker;
    private final SyncedEntityPlayer syncedEntityPlayer;

    public EntityMovementOperation(Entity entity, EntityOperationRegistry entityOperationRegistry) {
        super(entity, entityOperationRegistry);
        this.syncedEntityPlayerTracker = SyncedEntityPlayerTracker.getInstance();
        this.syncedEntityPlayer = (SyncedEntityPlayer) entity;
    }

    @Override
    public void operate(PacketInfo packetInfo, Player... players) {
        PlayerMovementPacket playerMovementPacket = (PlayerMovementPacket) packetInfo;

        double x = playerMovementPacket.getX();
        double y = playerMovementPacket.getY();
        double z = playerMovementPacket.getZ();
        float yaw = playerMovementPacket.getYaw();
        float pitch = playerMovementPacket.getPitch();

        if(playerMovementPacket.getX() != 0 || y != 0 || z != 0) {
            entity.setPosition(x, y, z);
        }
        setHeadRotation(yaw, pitch, players);

        PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entity);
        EntityBodyVisibleState entityBodyVisibleState = (EntityBodyVisibleState) syncedEntityPlayer.getEntityStateRegistry().getState(EntityBodyVisibleState.class);

        for(Player player : players) {
            boolean isEntityTracked = syncedEntityPlayerTracker.isEntityTracked(player, syncedEntityPlayer);
            if (isEntityTracked) {
                sendPacket(player).accept(packetPlayOutEntityTeleport);
                continue;
            }

            entityBodyVisibleState.enable(player);
            sendPacket(player).accept(packetPlayOutEntityTeleport);
        }
    }

    private void setHeadRotation(float yaw, float pitch, Player... players) {
        if(yaw == 0 && pitch == 0) return;
        entity.setLocation(entity.locX, entity.locY, entity.locZ, yaw, pitch);

        byte translateYaw = (byte) ((yaw * 256.0F) / 360.0F);
        sendPacket(players).accept(new PacketPlayOutEntityHeadRotation(entity, translateYaw));
    }
}
