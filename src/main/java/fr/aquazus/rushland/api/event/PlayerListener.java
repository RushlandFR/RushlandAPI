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
        if (lowercaseCommand.equals("/plugins") || lowercaseCommand.equals("/pl") || lowercaseCommand.startsWith("/plugins ") || lowercaseCommand.startsWith("/pl ") || lowercaseCommand.equals("/bukkit:plugins") || lowercaseCommand.equals("/bukkit:pl") || lowercaseCommand.startsWith("/bukkit:plugins ") || lowercaseCommand.startsWith("/bukkit:pl ")) {
            event.setCancelled(true);
            player.sendMessage("§fPlugins (4): §aRushlandAPI§f, §aProtocolLib§f, §aRushlandAntiTriche§f, §aRLGame-BuildChampion");
        } else if (lowercaseCommand.equals("/help") || lowercaseCommand.equals("/?") || lowercaseCommand.startsWith("/help ") || lowercaseCommand.startsWith("/? ") || lowercaseCommand.equals("/bukkit:help") || lowercaseCommand.equals("/bukkit:?") || lowercaseCommand.startsWith("/bukkit:help ") || lowercaseCommand.startsWith("/bukkit:? ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/me") || lowercaseCommand.startsWith("/me ") || lowercaseCommand.equals("/minecraft:me") || lowercaseCommand.startsWith("/minecraft:me ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/tell") || lowercaseCommand.startsWith("/tell ") || lowercaseCommand.equals("/minecraft:tell") || lowercaseCommand.startsWith("/minecraft:tell ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/wdl") || lowercaseCommand.startsWith("/wdl ") || lowercaseCommand.equals("/worlddownloader") || lowercaseCommand.startsWith("/worlddownloader ") || lowercaseCommand.equals("/wdlcompanion:wdl") || lowercaseCommand.startsWith("/wdlcompanion:wdl ") || lowercaseCommand.equals("/wdlcompanion:worlddownloader") || lowercaseCommand.startsWith("/wdlcompanion:worlddownloader ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/viaversion") || lowercaseCommand.startsWith("/viaversion ") || lowercaseCommand.equals("/viaversion:viaversion") || lowercaseCommand.startsWith("/viaversion:viaversion ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/viaver") || lowercaseCommand.startsWith("/viaver ") || lowercaseCommand.equals("/viaversion:viaver") || lowercaseCommand.startsWith("/viaversion:viaver ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/nocheatplus") || lowercaseCommand.startsWith("/nocheatplus ") || lowercaseCommand.equals("/nocheatplus:nocheatplus") || lowercaseCommand.startsWith("/nocheatplus:nocheatplus ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        } else if (lowercaseCommand.equals("/ncp") || lowercaseCommand.startsWith("/ncp ") || lowercaseCommand.equals("/nocheatplus:ncp") || lowercaseCommand.startsWith("/nocheatplus:ncp ")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Commande introuvable.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerInfo pInfo = PlayerInfo.get(uuid);
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Bukkit.getScheduler().runTaskAsynchronously(rushland, new Runnable() {
            @Override
            public void run() {
                RedisDataSender.sendData();
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.api.getRushland(), new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                if (!api.getDataManager().getPlayerDB().isInsert(player)) {
                    api.getDataManager().getPlayerDB().playerInit(player);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginKick(final PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskLater(this.api.getRushland(), new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                PlayerInfo playerInfo = new PlayerInfo(player);
                if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
                    if (playerInfo.getMaxPermLevel() >= 10) {
                        event.setResult(PlayerLoginEvent.Result.ALLOWED);
                        event.allow();
                    } else {
                        playerInfo.remove();
                    }
                } else if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
                    if (playerInfo.getMaxPermLevel() >= 10) {
                        event.setResult(PlayerLoginEvent.Result.ALLOWED);
                        event.allow();
                    } else {
                        playerInfo.remove();
                    }
                }
            }
        }, 20L);
    }
}
