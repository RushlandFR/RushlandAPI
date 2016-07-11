package fr.aquazus.rushland.api.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.BukkitInjector;
import redis.clients.jedis.JedisPubSub;

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
