package fr.rushland.api.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.rushland.api.BukkitInjector;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(RedisDataSender.channelSub)) {
            return;
        }
        String[] packet = message.split("#");
        if (!packet[0].equals(RedisDataSender.serverId)) {
            return;
        }
        if (packet[1].equals("shutdown")) {
            System.out.println("[Subscriber] Received shutdown packet");
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage("Votre partie a été arrêtée pour des raisons techniques.");
                BukkitInjector.getApi().runBungeePlayerCommand(players, "hub");
            }
            Bukkit.getServer().shutdown();
        }
    }
}
