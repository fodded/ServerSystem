package me.fodded.serversystem.listeners.bukkit;

import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.info.impl.PlayerAnimationPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final ServerSystem plugin;
    public PlayerDamageListener(ServerSystem plugin) {
        this.plugin = plugin;
    }

    // TODO: Does not seem to work when the damage is custom, might have worked around it to make it compatible with everything
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        PlayerAnimationPacket playerAnimationPacket = new PlayerAnimationPacket(player.getUniqueId(), 1);
        plugin.getRedisClient().sendMessage("playerAnimation", playerAnimationPacket.serializePacketInfo());
    }
}
