package fr.rushland.api.rushcoins;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class RushcoinsManager {

    private BukkitInjector plugin;
    private RushlandAPI api;
    private HashMap<String, HashMap<String, RushcoinsAchievement>> achievements;

    public RushcoinsManager(BukkitInjector plugin, RushlandAPI api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void load() {
        achievements = new HashMap<String, HashMap<String, RushcoinsAchievement>>();
    }
    
    public void addAchievement(Player player, RushcoinsAchievement achievement) {
        achievements.get(player.getUniqueId().toString()).put(achievement.getId(), achievement);
    }

    public void giveRewards(Player player) {
        if (!achievements.containsKey(player.getUniqueId().toString())) {
            return;
        }

        player.sendMessage("§6§m§l---------------------");
        player.sendMessage("§e§lGains de RushCoins :");
        player.sendMessage(" ");
        for (RushcoinsAchievement achievement : achievements.get(player.getUniqueId()).values()) {
            int totalAmount = achievement.getReward() * achievement.getQuantity();
            if (achievement.getQuantity() > 1) {
                player.sendMessage(" §6§l> §e§l+" + totalAmount + "§e (x" + achievement.getQuantity() + " " + achievement.getDisplayName() + ")");
            } else {
                player.sendMessage(" §6§l> §e§l+" + totalAmount + "§e (" + achievement.getDisplayName() + ")");
            }
        }
        player.sendMessage("§6§m§l---------------------");
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for (RushcoinsAchievement achievement : achievements.get(player.getUniqueId()).values()) {
                    int totalAmount = achievement.getReward() * achievement.getQuantity();
                    api.getDataManager().getMoneyAPI().addPlayermoney(player, "rushcoins", totalAmount);
                }
            }
        });
    }
}
