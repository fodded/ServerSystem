package me.fodded.serversystem.data.impl;

import lombok.Getter;

import java.util.Random;
import java.util.UUID;

@Getter
public class PlayerData {

    private final UUID uuid;
    private int randomNumber;

    // Should not be used, left here for the sake of POJO
    protected PlayerData() {
        this.uuid = UUID.randomUUID();
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.randomNumber = new Random().nextInt();
    }
}
