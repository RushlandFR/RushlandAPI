package fr.rushland.api.event;

import java.util.UUID;

import fr.rushland.api.redis.RedisDataSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;

public class PlayerJoin implements Listener{

    private BukkitInjector rushland;
    private RushlandAPI api;

    public PlayerJoin (BukkitInjector rushland,RushlandAPI api) {
        this.rushland = rushland;
        this.api = api;
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().contains("is not")) {
            event.setCancelled(true);
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
        Bukkit.getScheduler().runTaskAsynchronously(rushland, new Runnable() {
            @Override
            public void run() {
                RedisDataSender.sendData();
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.api.getRushland(), new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                if (!api.getDataManager().getPlayerDB().isInsert(player)) {
                    api.getDataManager().getPlayerDB().playerInit(player);
                }
                PlayerInfo playerInfo = new PlayerInfo(player);

                if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
                    if (playerInfo.getMaxPermLevel() >= 10) {
                        event.setResult(PlayerLoginEvent.Result.ALLOWED);
                    } else {
                        playerInfo.remove();
                    }
                } else if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
                    if (playerInfo.getMaxPermLevel() >= 10) {
                        event.setResult(PlayerLoginEvent.Result.ALLOWED);
                    } else {
                        playerInfo.remove();
                    }
                }
            }
        });
    }
}
