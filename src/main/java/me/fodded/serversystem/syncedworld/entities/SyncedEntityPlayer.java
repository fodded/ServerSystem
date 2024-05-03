package me.fodded.serversystem.syncedworld.entities;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.fodded.serversystem.ServerSystem;
import me.fodded.serversystem.syncedworld.SyncedWorldManager;
import me.fodded.serversystem.syncedworld.entities.operations.EntityOperationRegistry;
import me.fodded.serversystem.syncedworld.entities.operations.impl.EntityAnimationOperation;
import me.fodded.serversystem.syncedworld.entities.operations.impl.EntityMovementOperation;
import me.fodded.serversystem.syncedworld.entities.states.EntityStateRegistry;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityBodyVisibleState;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntitySneakState;
import me.fodded.serversystem.syncedworld.entities.states.impl.EntityTabVisibilityState;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

@Getter
public class SyncedEntityPlayer extends EntityPlayer {

    private final SyncedWorldManager syncedWorldManager;
    private final EntityStateRegistry entityStateRegistry;
    private final EntityOperationRegistry entityOperationRegistry;

    public SyncedEntityPlayer(CraftWorld worldIn, GameProfile gameprofile) {
        super(MinecraftServer.getServer(), worldIn.getHandle(), gameprofile, new PlayerInteractManager(worldIn.getHandle()));

        this.syncedWorldManager = ServerSystem.getInstance().getSyncedWorldManager();
        this.syncedWorldManager.registerFakeEntity(gameprofile.getId(), this);

        this.entityStateRegistry = new EntityStateRegistry();
        this.entityOperationRegistry = new EntityOperationRegistry();

        this.initializeEntityStates();
        this.initializeEntityOperations();
    }

    private void initializeEntityStates() {
        this.entityStateRegistry.registerState(new EntitySneakState(this, entityStateRegistry));
        this.entityStateRegistry.registerState(new EntityTabVisibilityState(this, entityStateRegistry));
        this.entityStateRegistry.registerState(new EntityBodyVisibleState(this, entityStateRegistry));
    }

    private void initializeEntityOperations() {
        this.entityOperationRegistry.registerState(new EntityMovementOperation(this, entityOperationRegistry));
        this.entityOperationRegistry.registerState(new EntityAnimationOperation(this, entityOperationRegistry));
    }
}
