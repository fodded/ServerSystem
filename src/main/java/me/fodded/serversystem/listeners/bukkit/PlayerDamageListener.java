package me.fodded.serversystem.listeners.bukkit;

import me.fodded.serversystem.ServerSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final ServerSystem plugin;
    public PlayerDamageListener(ServerSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            System.out.println("entity is not the player");
            return;
        }

        Player player = (Player) event.getEntity();

        System.out.println("sent damage animation");

        String formattedMessage = player.getUniqueId().toString() + ":1";
        plugin.getRedisClient().sendMessage("playerAnimation", formattedMessage);
    }
}
