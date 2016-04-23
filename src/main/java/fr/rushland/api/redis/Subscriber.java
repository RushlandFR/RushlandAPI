package fr.rushland.api.redis;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by Zaral on 10/04/2016.
 */
public class Subscriber extends JedisPubSub {



    @Override
    public void onMessage(String channel, String message) {
        //Reçois les messages.
        if (channel.equals(RedisDataSender.channelSub)) {


        }
    }
}
