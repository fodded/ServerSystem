package me.fodded.serversystem.data.transfer;

public interface IRedisListener {
    void onMessage(CharSequence channel, Object msg);
}
