package me.fodded.serversystem.syncedworld.entities;

import com.mojang.authlib.GameProfile;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.ISyncedEntity;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
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
            // TODO: it's a bad way to create a new packet with the same data for each player
            sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
            return;
        }

        showEntity(player);
        sendPacket(player).accept(new PacketPlayOutEntityTeleport(this));
    }

    public void hitEntity() {
        if((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {

        }
    }

    public void sneakEntity(Player... players) {
        datawatcher.watch(1, 0x02);
        sendPacket(players).accept(new PacketPlayOutEntityMetadata(this.getId(), datawatcher, true));
    }

    public void unSneakEntity(Player... players) {
        datawatcher.watch(1, 0);
        sendPacket(players).accept(new PacketPlayOutEntityMetadata(this.getId(), datawatcher, true));
    }

    public void playAnimation(int animationType, Player... players) {
        sendPacket(players).accept(new PacketPlayOutAnimation(this, animationType));

        // If the animation type is when the player damaged we need to play damage sound manually
        if(animationType == 1) {
            Entity entity = getBukkitEntity();
            entity.getWorld().playSound(entity.getLocation(), Sound.HURT_FLESH, 1.0f, 1.0f);
        }
    }

    private void setHeadRotation(Player player, float yaw, float pitch) {
        if(yaw == 0 && pitch == 0) return;
        setYawPitch(yaw, pitch);

        byte translateYaw = (byte) ((yaw * 256.0F) / 360.0F);

        // TODO: it's a bad way to create a new packet with the same data for each player
        sendPacket(player).accept(new PacketPlayOutEntityHeadRotation(this, translateYaw));
    }
}
