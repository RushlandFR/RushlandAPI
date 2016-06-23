package fr.rushland.api.redis;

import fr.rushland.api.BukkitInjector;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class Publisher {

    private final String channel;
    private JedisFactory jedisFactory;

    public Publisher(String channel) {
        this.channel = channel;
        this.jedisFactory = JedisFactory.getInstance();
    }

    public void publish(String message) {
        try {
            Jedis jedis = null;
            try {
                jedis = jedisFactory.getPool().getResource();
                jedis.publish(channel, message);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } catch (Exception e) {
            BukkitInjector.getApi().getRushland().getLogger().log(Level.SEVERE, "Failed to publish ", e);
        }
    }
    /*
    public void init() {
        StringBuilder sb = new StringBuilder();
        sb.append("init");
        for (String game : BukkitInjector.getApi().getRushland().getNpcManager().gameList) {
            sb.append("#" + game + "");


            this.publish(sb.toString().trim());

        }

    }
     */
}