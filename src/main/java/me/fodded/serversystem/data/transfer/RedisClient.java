package me.fodded.serversystem.data.transfer;

import lombok.Getter;
import me.fodded.serversystem.data.transfer.listeners.PacketPlayerDisconnectListener;
import me.fodded.serversystem.data.transfer.listeners.PacketPlayerJoinListener;
import me.fodded.serversystem.data.transfer.listeners.PacketPlayerMoveListener;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CompletableFuture;

public class RedisClient {

    @Getter
    private final RedissonClient redissonClient;

    public RedisClient(String redisHost, int redisPort) {
        Config redisConfig = new Config();
        redisConfig.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);

        this.redissonClient = Redisson.create(redisConfig);
    }

    private void registerListener(IRedisListener redisListener, String topic) {
        RTopic sendPlayerToLobbyTopic = redissonClient.getTopic(topic);
        sendPlayerToLobbyTopic.addListener(String.class, redisListener::onMessage);
    }

    public void registerAllListeners() {
        registerListener(new PacketPlayerDisconnectListener(), "playerQuit");
        registerListener(new PacketPlayerMoveListener(), "playerMove");
        registerListener(new PacketPlayerJoinListener(), "playerJoined");
    }

    public void sendMessage(String topicName, String message) {
        CompletableFuture.runAsync(() -> {
            RTopic topic = redissonClient.getTopic(topicName);
            topic.publish(message);
        });
    }
}
