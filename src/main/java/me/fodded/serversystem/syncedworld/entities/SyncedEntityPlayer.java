package me.fodded.serversystem.syncedworld.entities;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.ISyncedEntity;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

public class SyncedEntityPlayer extends EntityPlayer implements ISyncedEntity {

    private final SyncedWorldManager syncedWorldManager;

    public SyncedEntityPlayer(CraftWorld worldIn, GameProfile gameprofile) {
        super(MinecraftServer.getServer(), worldIn.getHandle(), gameprofile, new PlayerInteractManager(worldIn.getHandle()));

        this.syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        this.syncedWorldManager.registerFakeEntity(gameprofile.getId(), this);
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
            SyncedEntityPlayerTracker.getInstance().untrackEntity(player, this);
        }
    }

    public void showEntity(Player... players) {
        sendPacket(players).accept(new PacketPlayOutNamedEntitySpawn(this));

        for(Player player : players) {
            SyncedEntityPlayerTracker.getInstance().trackEntity(player, this);
        }
    }

    public void moveEntity(Player player, double x, double y, double z, float yaw, float pitch) {
        if(x != 0 || y != 0 || z != 0) {
            setPosition(x, y, z);
        }
        setHeadRotation(player, yaw, pitch);

        boolean isEntityTracked = SyncedEntityPlayerTracker.getInstance().isEntityTracked(player, this);
        if(isEntityTracked) {
            sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
            return;
        }

        showEntity(player);
        sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
    }

    private void setHeadRotation(Player player, float yaw, float pitch) {
        if(yaw == 0 && pitch == 0) return;
        setYawPitch(yaw, pitch);

        byte translateYaw = (byte) ((yaw * 256.0F) / 360.0F);
        sendPacket(player).accept(new PacketPlayOutEntityHeadRotation(this, translateYaw));
    }
}
