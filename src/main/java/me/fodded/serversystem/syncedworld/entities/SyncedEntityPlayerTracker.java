package me.fodded.serversystem.syncedworld.entities;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SyncedEntityPlayerTracker {

    @Getter
    private static final SyncedEntityPlayerTracker instance = new SyncedEntityPlayerTracker();

    // That's fine to store SyncedEntityPlayer instead of Entity IDs since they disappear once a player disconnects from the server
    private final Map<UUID, List<SyncedEntityPlayer>> showedEntitiesToPlayers = new HashMap<>();

    public void untrackEntity(Player player, SyncedEntityPlayer entityPlayer) {
        List<SyncedEntityPlayer> showedEntitiesList = showedEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
        showedEntitiesList.remove(entityPlayer);
        showedEntitiesToPlayers.put(player.getUniqueId(), showedEntitiesList);
    }

    public void trackEntity(Player player, SyncedEntityPlayer entityPlayer) {
        List<SyncedEntityPlayer> showedEntitiesList = showedEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
        showedEntitiesList.add(entityPlayer);
        showedEntitiesToPlayers.put(player.getUniqueId(), showedEntitiesList);
    }

    public boolean isEntityTracked(Player player, SyncedEntityPlayer entityPlayer) {
        List<SyncedEntityPlayer> showedEntitiesList = showedEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
        return showedEntitiesList.contains(entityPlayer);
    }

    public List<Player> getPlayersEntityTrackedFor(EntityPlayer entityPlayer) {
        return showedEntitiesToPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().contains(entityPlayer))
                .map(entry -> Bukkit.getPlayer(entry.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void clearEntitiesCacheToPlayer(UUID uniqueId) {
        showedEntitiesToPlayers.remove(uniqueId);
    }
}
