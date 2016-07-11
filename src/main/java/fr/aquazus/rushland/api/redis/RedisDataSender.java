package fr.aquazus.rushland.api.redis;

import org.bukkit.Bukkit;

import fr.aquazus.rushland.api.BukkitInjector;
import fr.aquazus.rushland.api.utils.CodeUtils;
import redis.clients.jedis.Jedis;

/*
 * Ce fichier est soumis à des droits d'auteur.
 * Dépot http://www.copyrightdepot.com/cd88/00056542.htm
 * Numéro du détenteur - 00056542
 * Le détenteur des copyrights publiés dans cette page n'autorise 
 * aucun usage de ses créations, en tout ou en partie. 
 * Les archives de CopyrightDepot.com conservent les documents 
 * qui permettent au détenteur de démontrer ses droits d'auteur et d’éventuellement
 * réclamer légalement une compensation financière contre toute personne ayant utilisé 
 * une de ses créations sans autorisation. Conformément à nos règlements, 
 * ces documents sont assermentés, à nos frais, 
 * en cas de procès pour violation de droits d'auteur.
 */

public class RedisDataSender {

    public static String serverId;
    public static int ports;
    public static String channelSub = "RLGameManager";
    public static Publisher getPublisher = null;
    public static String motd = "§cDémarrage...";

    public static void setup(String type,  int port) {
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
        Jedis jedis = null;
        try {
            jedis = JedisFactory.getInstance().getPool().getResource();
            jedis.set(key, value);
            jedis.expire(key, 3);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        final Subscriber subscriber = new Subscriber();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(BukkitInjector.getApi().getRushland(), new Runnable() {
            @Override
            public void run() {
                try {
                    CodeUtils.logToConsole("Subscribing to '" + channelSub + "' channel");
                    Jedis jedis = null;
                    try {
                        jedis = JedisFactory.getInstance().getPool().getResource();
                        jedis.subscribe(subscriber, channelSub);
                    } finally {
                        if (jedis != null) {
                            jedis.close();
                        }
                    }
                    CodeUtils.logToConsole("Subscription ended.");
                } catch (Exception e) {
                    CodeUtils.logToConsole("Subscribing failed." );
                    e.printStackTrace();
                }

            }
        });

        getPublisher = new Publisher(channelSub);
    }
}
