package me.fodded.serversystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static Player getPlayerFromId(int playerId) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(((CraftPlayer) player).getHandle().getId() == playerId) {
                return player;
            }
        }
        return null;
    }
}
