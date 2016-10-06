package fr.aquazus.rushland.api.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.aquazus.rushland.api.BukkitInjector;
import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.data.PlayerInfo;
import fr.aquazus.rushland.api.redis.RedisDataSender;

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

public class PlayerListener implements Listener{

    private BukkitInjector rushland;
    private RushlandAPI api;

    public PlayerListener(BukkitInjector rushland,RushlandAPI api) {
        this.rushland = rushland;
        this.api = api;
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().contains("is not")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        String lowercaseCommand = command.toLowerCase();
        if (lowercaseCommand.equals("/getop") || lowercaseCommand.startsWith("/getop ")) {
            event.setCancelled(true);
            if (PlayerInfo.get(player.getUniqueId()).permLevel >= 100) {
                if (player.isOp()) {
                    player.sendMessage("§cVous êtes déjà OP.");
                } else {
                    player.setOp(true);
                    player.sendMessage("§dOP: §aON");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Commande introuvable.");
            }
        } else if (lowercaseCommand.equals("/delop") || lowercaseCommand.startsWith("/delop ")) {
            event.setCancelled(true);
            if (PlayerInfo.get(player.getUniqueId()).permLevel >= 100) {
                if (player.isOp()) {
                    player.setOp(false);
                    player.sendMessage("§dOP: §cOFF");
                } else {
                    player.sendMessage("§cVous n'êtes pas OP.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Commande introuvable.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerInfo pInfo = PlayerInfo.get(uuid);
        if (!api.disableXpRecap) {
            player.sendMessage("§9Points d'XP gagnés pendant cette partie: §a" + pInfo.sessionXp);
            player.sendMessage("§9Progression niveau " + pInfo.getLevel() + " -> " + pInfo.getLevel() + 1 + ": " + pInfo.generateXpBar());
        }
        Bukkit.getScheduler().runTaskLater(rushland, new Runnable() {
            @Override
            public void run() {
                if (pInfo != null) {
                    pInfo.remove();
                }
            }
        }, 20L);
        Bukkit.getScheduler().runTaskAsynchronously(rushland, new Runnable() {
            @Override
            public void run() {
                RedisDataSender.sendData();
            }
        });
    }

    boolean done = false;
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        if (!done) {
            this.api.getDataManager().startTask();
            done = true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(rushland, new Runnable() {
            @Override
            public void run() {
                RedisDataSender.sendData();
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginKick(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (!api.getDataManager().getPlayerDB().isInsert(player)) {
            api.getDataManager().getPlayerDB().playerInit(player);
        }
        PlayerInfo playerInfo = new PlayerInfo(player);
        if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
            if (playerInfo.getMaxPermLevel() >= 10) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                event.allow();
                playerInfo.callEvent();
            } else {
                playerInfo.remove();
            }
        } else if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (playerInfo.getMaxPermLevel() >= 10) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                event.allow();
                playerInfo.callEvent();
            } else {
                playerInfo.remove();
            }
        } else {
            playerInfo.callEvent();
        }
    }
}
