**PROJECT IDEA**
This is a spigot plugin made to synchronize players across all the minecraft servers the plugin is installed on. Basically, every player is seen to another even if they are located on a two completely different Minecraft server instances. They can contact each other, chat with each other and more features are being in progress right now.

**LIMITATIONS**
Theoretically you could break minecraft players limits by having multiple minecraft server instances. For example, if you have 10 minecraft servers and 100 players on each of them, then with the plugin on EACH server you will see 1000 players (10x100). This is going to happen so because all of the players are seen to each other on every server. Since it's pretty hard to keep many players on a single server without any lag, this plugin could help you with having as many players as you want. The only limit supposingly is the amount of packets which could be sent

**USE CASE**
In case you want to use the plugin for a survival server to synchronize players it might be not the best idea, since you will feel the impact on the server performance. To sycnhronize data in a survival server the plugin would need to send changed blocks to every server and the worlds must be the same everywhere, otherwise it will be desynced. To clarify, in case you have different worlds on 2 servers, on player will look like he is going through blocks for another player.

On the other hand, if your intention is to use the plugin somewhere where players can not interact with blocks or entities from all servers do not have to be synced, then it's a good point. As an example you could think of a server lobby which could handle a few thousand players at one moment.

**REQUIREMENTS**
There is no requirement for Proxy being installed, only to have one redis server and connect all of the minecraft servers you have to that redis server with the plugin. Also, you will need _ProtocolLib_ installed.

**SUPPORTED VERSION** 
Right now the plugin works exceptionally with 1.8.8
