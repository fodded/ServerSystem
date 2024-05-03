package me.fodded.serversystem.data.transfer.listeners;

import me.fodded.serversystem.data.transfer.IRedisListener;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerHitListener implements IRedisListener {

    @Override
    public void onMessage(CharSequence channel, Object msg) {
        String message = (String) msg;
        String[] arrMessage = message.split(":");

        UUID playerId = UUID.fromString(arrMessage[0]);
        double damage = Double.parseDouble(arrMessage[1]);
        double knockbackX = Double.parseDouble(arrMessage[2]);
        double knockbackY = Double.parseDouble(arrMessage[3]);
        double knockbackZ = Double.parseDouble(arrMessage[4]);

        Player player = Bukkit.getPlayer(playerId);
        if(player == null) {
            return;
        }

        if(player.getGameMode() == GameMode.CREATIVE || (float) player.getNoDamageTicks() >= (float) player.getMaximumNoDamageTicks() / 2.0F) {
            return;
        }

        player.damage(damage);
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.g(knockbackX, knockbackY, knockbackZ);
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(entityPlayer));
    }
}
