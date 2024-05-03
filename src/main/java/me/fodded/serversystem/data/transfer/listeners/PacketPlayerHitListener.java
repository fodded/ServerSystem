package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.data.transfer.IRedisListener;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import me.fodded.serversystem.syncedworld.info.impl.PlayerKnockbackPacket;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This listener is triggered when a player attacks an NPC which is copy of the original player and then the packet sent to the original player to take damage
 */
public class PacketPlayerHitListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;

        PlayerKnockbackPacket playerKnockbackPacket = new PlayerKnockbackPacket(message);
        UUID playerId = playerKnockbackPacket.getUuid();

        Player player = Bukkit.getPlayer(playerId);
        if(player == null) {
            return;
        }

        // TODO: maybe put it on another class to separate logic from here
        if(player.getGameMode() == GameMode.CREATIVE || (float) player.getNoDamageTicks() >= (float) player.getMaximumNoDamageTicks() / 2.0F) {
            return;
        }

        // player#damage does not seem to trigger EntityDamageEvent, so we have to send animation packet manually
        player.damage(playerKnockbackPacket.getDamage());

        PlayerAnimationPacket playerAnimationPacket = new PlayerAnimationPacket(playerId, 0);
        ServerSystem.getInstance().getRedisClient().sendMessage("playerAnimation", playerAnimationPacket.serializePacketInfo());

        // Here we apply knockback to the player and send a packet to make him d
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.g(playerKnockbackPacket.getKnockbackX(), playerKnockbackPacket.getKnockbackY(), playerKnockbackPacket.getKnockbackZ());
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(entityPlayer));
    }
}
