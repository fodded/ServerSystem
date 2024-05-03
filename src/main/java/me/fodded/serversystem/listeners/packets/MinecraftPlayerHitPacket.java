package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.entities.SyncedEntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MinecraftPlayerHitPacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerHitPacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        EnumWrappers.EntityUseAction useAction = packet.getEntityUseActions().read(0);
        if(useAction != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }

        Player attackerPlayer = event.getPlayer();

        float playerYaw = attackerPlayer.getLocation().getYaw();
        float constExtraKnockback = calculateItemKnockback(attackerPlayer);

        double knockbackX = (-MathHelper.sin(playerYaw * 3.1415927F / 180.0F) * constExtraKnockback * 0.5F);
        double knockbackY = 0.1D;
        double knockbackZ = (MathHelper.cos(playerYaw * 3.1415927F / 180.0F) * constExtraKnockback * 0.5F);

        int attackedPlayerId = packet.getIntegers().read(0);
        SyncedEntityPlayer syncedEntityPlayer = plugin.getSyncedWorldManager().getSyncedEntityPlayer(attackedPlayerId);
        if(syncedEntityPlayer == null) { // shouldn't be the case
            return;
        }

        double damage = calculatePlayerDamage(attackerPlayer);

        String formattedMessage = syncedEntityPlayer.getUniqueID().toString() + ":" + damage + ":" + knockbackX + ":" + knockbackY + ":" + knockbackZ;
        plugin.getRedisClient().sendMessage("playerHit", formattedMessage);
    }

    private double calculatePlayerDamage(Player player) {
        return 1.0;

        /*
        ItemStack heldItem = player.getItemInHand();
        if(heldItem == null) {
            return 1.0;
        }

        net.minecraft.server.v1_8_R3.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(heldItem);
        AttributeBase damage = (AttributeBase) itemStackNMS.B().get("generic.attackDamage");
        if(damage == null) {
            return 1.0;
        }

        return damage.b() + 1.0;*/
    }

    private int calculateItemKnockback(Player attackerPlayer) {
        ItemStack heldItem = attackerPlayer.getItemInHand();
        int initialValue = attackerPlayer.isSprinting() ? 2 : 1;
        if(heldItem == null || !heldItem.hasItemMeta() || !heldItem.getItemMeta().hasEnchants() || !heldItem.getItemMeta().hasEnchant(Enchantment.KNOCKBACK)){
            return initialValue;
        }
        return heldItem.getItemMeta().getEnchantLevel(Enchantment.KNOCKBACK) + initialValue;
    }
}
