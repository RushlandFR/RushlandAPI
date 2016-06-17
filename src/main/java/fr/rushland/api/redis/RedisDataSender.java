package fr.rushland.api.redis;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.utils.CodeUtils;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class RedisDataSender {

    public static String serverId;
    public static int ports;
    public static String channelSub = "RLGameManager";
    public static Publisher getPublisher = null;

    public static String motd = "§cDémarrage...";

    public static void setup( String type,  int port) {
        serverId = type;
        ports = port;
        subscribeChannels();
        sendData();
        refreshTimer();
        getPublisher.publish(type + "#wakeup#" + port);
    }

    public static void sendData() {
        String key = serverId + ports;
        String value = ports + "#" + motd  + "#" + Bukkit.getServer().getOnlinePlayers().size() + "#" + Bukkit.getServer().getMaxPlayers();
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        jedis.set(key, value);
        jedis.expire(key, 3);
        jedis.close();
    }

    public static void refreshTimer() {
        Bukkit.getScheduler().runTaskLater(BukkitInjector.getApi().getRushland(), new Runnable() {
            @Override
            public void run() {
                sendData();
                refreshTimer();
            }
        }, 20L);
    }


    private static void subscribeChannels() {
        final Jedis subscriberJedis = JedisFactory.getInstance().getJedisPool().getResource();

        final Subscriber subscriber = new Subscriber(); //permet de reçevoir les données

        Bukkit.getServer().getScheduler().runTaskAsynchronously(BukkitInjector.getApi().getRushland(), new Runnable() {
            @Override
            public void run() {
                try {
                    CodeUtils.logToConsole("Subscribing to '" + channelSub + "' channel");
                    subscriberJedis.subscribe(subscriber, channelSub);
                    CodeUtils.logToConsole("Subscription ended.");
                } catch (Exception e) {
                    CodeUtils.logToConsole("Subscribing failed." );
                    e.printStackTrace();
                }

            }
        });

        final Jedis publisherJedis = JedisFactory.getInstance().getJedisPool().getResource();

        getPublisher = new Publisher(publisherJedis, channelSub);
        // getPublisher.init();

    }
}
