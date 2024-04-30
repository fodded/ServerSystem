package me.fodded.serversystem.fakeworld;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.ServerSystem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

public class FakeEntityPlayer extends EntityPlayer implements IFakeEntity {

    private final Map<UUID, List<FakeEntityPlayer>> showedFakeEntitiesToPlayers = new HashMap<>();
    private final FakeWorldManager fakeWorldManager;

    public FakeEntityPlayer(CraftWorld worldIn, GameProfile gameprofile) {
        super(MinecraftServer.getServer(), worldIn.getHandle(), gameprofile, new PlayerInteractManager(worldIn.getHandle()));

        this.fakeWorldManager = ServerSystem.getInstance().getFakeWorldManager();
        this.fakeWorldManager.registerFakeEntity(gameprofile.getId(), this);
    }

    public void showTabName(Player... players) {
        sendPacket(players).accept(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
    }

    public void hideTabName(Player... players) {
        sendPacket(players).accept(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
    }

    public void hideEntity(Player... players) {
        sendPacket(players).accept(new PacketPlayOutEntityDestroy(getId()));

        for(Player player : players) {
            List<FakeEntityPlayer> showedEntitiesList = showedFakeEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
            showedEntitiesList.remove(this);
            showedFakeEntitiesToPlayers.put(player.getUniqueId(), showedEntitiesList);
        }
    }

    public void showEntity(Player... players) {
        sendPacket(players).accept(new PacketPlayOutNamedEntitySpawn(this));

        for(Player player : players) {
            List<FakeEntityPlayer> showedEntitiesList = showedFakeEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
            showedEntitiesList.add(this);
            showedFakeEntitiesToPlayers.put(player.getUniqueId(), showedEntitiesList);
        }
    }

    public void clearFakeEntitiesToPlayer(Player player) {
        showedFakeEntitiesToPlayers.put(player.getUniqueId(), Collections.emptyList());
    }

    public void moveFakeEntity(Player player, double x, double y, double z, float yaw, float pitch) {
        setPositionRotation(x, y, z, yaw, pitch);

        List<FakeEntityPlayer> showedEntitiesList = showedFakeEntitiesToPlayers.computeIfAbsent(player.getUniqueId(), ignore -> new ArrayList<>());
        if(showedEntitiesList.contains(this)) {
            sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
            return;
        }

        showEntity(player);
        sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
    }
}
