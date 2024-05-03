package me.fodded.serversystem.syncedworld.entities.operations.impl;

import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import me.fodded.serversystem.syncedworld.entities.operations.AbstractEntityOperation;
import me.fodded.serversystem.syncedworld.entities.operations.EntityOperationRegistry;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntitySneakState;
import me.fodded.serversystem.syncedworld.info.PacketInfo;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EntityAnimationOperation extends AbstractEntityOperation {

    private final SyncedEntityPlayer syncedEntityPlayer;

    public EntityAnimationOperation(Entity entity, EntityOperationRegistry entityOperationRegistry) {
        super(entity, entityOperationRegistry);
        this.syncedEntityPlayer = (SyncedEntityPlayer) entity;
    }

    @Override
    public void operate(PacketInfo packetInfo, Player... players) {
        PlayerAnimationPacket playerAnimationPacket = (PlayerAnimationPacket) packetInfo;
        int animationType = playerAnimationPacket.getAnimationType();
        if(animationType == 2) {
            syncedEntityPlayer.getEntityStateRegistry().getState(EntitySneakState.class).enable(players);
            return;
        }

        if(animationType == 3) {
            syncedEntityPlayer.getEntityStateRegistry().getState(EntitySneakState.class).disable(players);
            return;
        }

        sendPacket(players).accept(new PacketPlayOutAnimation(syncedEntityPlayer, animationType));
        if(animationType == 1) {
            // If the animation type is when the player damaged we need to play damage sound manually
            org.bukkit.entity.Entity entity = syncedEntityPlayer.getBukkitEntity();
            entity.getWorld().playSound(entity.getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
        }
    }
}
