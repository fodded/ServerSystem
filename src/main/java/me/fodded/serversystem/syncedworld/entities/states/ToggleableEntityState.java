package me.fodded.serversystem.syncedworld.entities.states;

import me.fodded.serversystem.syncedworld.entities.IPacketSender;
import org.bukkit.entity.Player;

public interface ToggleableEntityState extends IPacketSender {

    void enable(Player... players);
    void disable(Player... players);
}
