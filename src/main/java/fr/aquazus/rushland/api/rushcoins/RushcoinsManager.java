package fr.aquazus.rushland.api.rushcoins;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.BukkitInjector;
import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.data.PlayerInfo;

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
        if (player == null) {
            return;
        }
        if (player.getUniqueId() == null) {
            return;
        }
        if (!achievements.containsKey(player.getUniqueId().toString())) {
            HashMap<String, RushcoinsAchievement> newHashmap = new HashMap<>();
            newHashmap.put(achievement.getId(), achievement);
            achievements.put(player.getUniqueId().toString(), newHashmap);
        } else {
            achievements.get(player.getUniqueId().toString()).put(achievement.getId(), achievement);
        }
    }

    public HashMap<String, RushcoinsAchievement> getAchievements(String uuid) {
        return achievements.get(uuid);
    }

    public void setAchievements(String uuid, HashMap<String, RushcoinsAchievement> achievements) {
        this.achievements.put(uuid, achievements);
    }

    public void giveRewards(Player player) {
        if (player == null) {
            return;
        }
        if (player.getUniqueId() == null) {
            return;
        }
        PlayerInfo pInfo = PlayerInfo.get(player.getUniqueId());
        if (pInfo != null) {
            pInfo.showRecap();
        }
        if (!achievements.containsKey(player.getUniqueId().toString())) {
            return;
        }
        if (achievements.get(player.getUniqueId().toString()) == null) {
            return;
        }
        if (achievements.get(player.getUniqueId().toString()).isEmpty()) {
            return;
        }
        
        Double multiplicator = (double) 1;
        Double displayMultiplicator = (double) 1;
        String rank = "player";
        if (pInfo != null) {
            if (pInfo.getKarmaRank().equalsIgnoreCase("or")) {
                multiplicator = (double) 5;
                displayMultiplicator = (double) 5;
                rank = "§e§lOr";
            } else if (pInfo.getKarmaRank().equalsIgnoreCase("diamant")) {
                multiplicator = (double) 10;
                displayMultiplicator = (double) 10;
                rank = "§b§lDiamant";
            } else if (pInfo.getKarmaRank().equalsIgnoreCase("emeraude")) {
                multiplicator = (double) 15;
                displayMultiplicator = (double) 15;
                rank = "§a§lEmeraude";
            }
        }
        
        multiplicator += 900;

        player.sendMessage("§6§m§l---------------------");
        player.sendMessage("§e§lGains de RushCoins :");
        player.sendMessage(" ");
        for (RushcoinsAchievement achievement : achievements.get(player.getUniqueId().toString()).values()) {
            Double totalAmount = (double) achievement.getReward() * achievement.getQuantity();
            if (multiplicator > 1) {
                Double totalAmountMultiplied = totalAmount;
                totalAmountMultiplied += totalAmount * (multiplicator / 100);
                if (achievement.getQuantity() > 1) {
                    player.sendMessage(" §6§l> §a§m+" + totalAmount.intValue() + "§r §a§l+" + totalAmountMultiplied.intValue() + "§e (x" + achievement.getQuantity() + " " + achievement.getDisplayName() + ")");
                } else {
                    player.sendMessage(" §6§l> §a§m+" + totalAmount.intValue() + "§r §a§l+" + totalAmountMultiplied.intValue() + "§e (" + achievement.getDisplayName() + ")");
                }
            } else {
                if (achievement.getQuantity() > 1) {
                    player.sendMessage(" §6§l> §a§l+" + totalAmount.intValue() + "§e (x" + achievement.getQuantity() + " " + achievement.getDisplayName() + ")");
                } else {
                    player.sendMessage(" §6§l> §a§l+" + totalAmount.intValue() + "§e (" + achievement.getDisplayName() + ")");
                }
            }
        }
        if (multiplicator > 1) {
            player.sendMessage(" ");
            player.sendMessage("§aBonus de §a§l+" + displayMultiplicator + "%§a actif (Grade " + rank + "§a)");
            player.sendMessage("§aBonus de §c§l+900%§a actif (Compensation technique)");
        }
        player.sendMessage("§6§m§l---------------------");

        final Double finalMultiplicator = multiplicator;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for (RushcoinsAchievement achievement : achievements.get(player.getUniqueId().toString()).values()) {
                    Double totalAmount = (double) achievement.getReward() * achievement.getQuantity();
                    if (finalMultiplicator > 1) {
                        totalAmount += totalAmount * (finalMultiplicator / 100);
                    }
                    api.getDataManager().getMoneyAPI().addPlayerMoney(player, "rushcoins", totalAmount.intValue());
                }
            }
        });
    }
}
