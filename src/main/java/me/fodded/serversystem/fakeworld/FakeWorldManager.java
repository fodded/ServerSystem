package me.fodded.serversystem.fakeworld;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeWorldManager {

    private final Map<UUID, FakeEntityPlayer> registeredFakeEntities = new HashMap<>();

    public void registerFakeEntity(UUID uuid, FakeEntityPlayer entityPlayer) {
        registeredFakeEntities.put(uuid, entityPlayer);
    }

    public void unregisterFakeEntity(UUID uuid) {
        registeredFakeEntities.remove(uuid);
    }

    public FakeEntityPlayer getFakeEntityPlayer(UUID uuid) {
        return registeredFakeEntities.get(uuid);
    }

    public Collection<FakeEntityPlayer> getRegisteredFakeEntities() {
        return registeredFakeEntities.values();
    }
}
