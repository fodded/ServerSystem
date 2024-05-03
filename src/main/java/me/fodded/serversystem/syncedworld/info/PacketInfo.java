package me.fodded.serversystem.syncedworld.info;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;

public abstract class PacketInfo {

    @Expose
    private static Gson GSON = new Gson();

    public <T> PacketInfo deserializePacketInfo(String serializedString, T packetInfoType) {
        return GSON.fromJson(serializedString, (Type) packetInfoType);
    }

    public String serializePacketInfo() {
        return GSON.toJson(this);
    }
}
