package me.fodded.serversystem.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MinecraftPlayerAnimationPacket extends PacketAdapter implements PacketListener {

    private final ServerSystem plugin;

    public MinecraftPlayerAnimationPacket(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.ENTITY_ACTION);
        this.plugin = (ServerSystem) plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        // 0 and 1 are the types of Sneak Animation
        int animationType = ((PacketPlayInEntityAction) event.getPacket().getHandle()).b().ordinal();
        if(animationType != 0 && animationType != 1) {
            return;
        }

        // + 2 because we would have conflicts with Swing Animation which is also 0 as Crouching and Take Damage(1)
        PlayerAnimationPacket playerAnimationPacket = new PlayerAnimationPacket(player.getUniqueId(), animationType+2);
        plugin.getRedisClient().sendMessage("playerAnimation", playerAnimationPacket.serializePacketInfo());
    }
}
