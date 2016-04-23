package fr.rushland.api.redis;

import fr.rushland.api.Main;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class Publisher {


    private final Jedis publisherJedis;

    private final String channel;

    public Publisher(Jedis publisherJedis, String channel) {
        this.publisherJedis = publisherJedis;
        this.channel = channel;
    }

    public void publish(String message) {
        try {
            publisherJedis.publish(channel, message);
        } catch (Exception e) {
            Main.getApi().getRushland().getLogger().log(Level.SEVERE, "Failed to publish ", e);
        }
    }
/*
    public void init() {
        StringBuilder sb = new StringBuilder();
        sb.append("init");
        for (String game : Main.getApi().getRushland().getNpcManager().gameList) {
            sb.append("#" + game + "");


            this.publish(sb.toString().trim());

        }

    }
    */
}