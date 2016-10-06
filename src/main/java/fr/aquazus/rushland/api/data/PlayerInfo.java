package fr.aquazus.rushland.api.data;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.aquazus.rushland.api.BukkitInjector;
import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.events.PlayerLoadedEvent;
import fr.aquazus.rushland.api.utils.Title;

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

public class PlayerInfo {

    private Player player;
    private UUID uuid;
    private RushlandAPI api = BukkitInjector.getApi();

    public int permLevel = 0;
    public int rankPermLevel = 0;
    public int rushcoins = 0;
    public int shopcoins = 0;
    public int level = 0;
    public int xp = 0;
    public int sessionXp = 0;

    /**
     * @return "player" if the target is not rank/karma
     */
    public String rank;
    public String karma;

    public boolean isFemale = false;
    public Date expire;

    public static PlayerInfo get(UUID uuid) {
        for (PlayerInfo playerInfo : BukkitInjector.getApi().getPlayerList()) {
            if (playerInfo.getUUID().equals(uuid)) {
                return playerInfo;
            }
        }
        return null;
    }

    public static PlayerInfo get(Player player) {
        for (PlayerInfo playerInfo : BukkitInjector.getApi().getPlayerList()) {
            if (playerInfo.getUUID().equals(player.getUniqueId())) {
                return playerInfo;
            }
        }
        return null;
    }

    public PlayerInfo(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.rank = "player";
        ResultSet resultSet = api.getDataManager().getPlayerDB().getPlayerInfo(uuid);
        try {
            rank = resultSet.getString("playerRank");
            karma = resultSet.getString("playerKarma");
            permLevel = resultSet.getInt("permLevel");
            rushcoins = resultSet.getInt("rushcoins");
            shopcoins = resultSet.getInt("shopcoins");
            level = resultSet.getInt("level");
            xp = resultSet.getInt("xp");
            if (!rank.equalsIgnoreCase("player")) {
                if (api.getDataManager().getRankSystemDB().getRankList().containsKey(rank)) {
                    rankPermLevel = api.getDataManager().getRankSystemDB().getRankList().get(rank);
                }
                isFemale = resultSet.getBoolean("rankFemale");
            }
            if (!karma.equalsIgnoreCase("player") && !karma.equalsIgnoreCase("emeraude")) {
                expire = resultSet.getDate("expire");
                if (expire.getTime() < System.currentTimeMillis()) {
                    api.getDataManager().getPlayerDB().deleteKarmaPlayer(uuid);
                    karma = "player";
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        api.playerList.add(this);
    }

    public PlayerInfo(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.uuid = uuid;
        this.rank = "player";
        ResultSet resultSet = api.getDataManager().getPlayerDB().getPlayerInfo(uuid);
        try {
            rank = resultSet.getString("playerRank");
            karma = resultSet.getString("playerKarma");
            permLevel = resultSet.getInt("permLevel");
            rushcoins = resultSet.getInt("rushcoins");
            shopcoins = resultSet.getInt("shopcoins");
            level = resultSet.getInt("level");
            xp = resultSet.getInt("xp");
            if (!rank.equalsIgnoreCase("player")) {
                if (api.getDataManager().getRankSystemDB().getRankList().containsKey(rank)) {
                    rankPermLevel = api.getDataManager().getRankSystemDB().getRankList().get(rank);
                }
                isFemale = resultSet.getBoolean("rankFemale");
            }
            if (!karma.equalsIgnoreCase("player") && !karma.equalsIgnoreCase("emeraude")) {
                expire = resultSet.getDate("expire");
                if (expire.getTime() < System.currentTimeMillis()) {
                    api.getDataManager().getPlayerDB().deleteKarmaPlayer(uuid);
                    karma = "player";
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        api.playerList.add(this);
    }

    public int getMaxPermLevel() {
        if (permLevel > rankPermLevel) {
            return permLevel;
        } else {
            return rankPermLevel;
        }
    }

    public void addXp(int amount) {
        Player player = Bukkit.getPlayer(uuid);
        int next = level + 1;
        Leveling currentLevel = Leveling.valueOf("LEVEL_" + level);
        Leveling nextLevel = Leveling.valueOf("LEVEL_" + next);
        xp += amount;
        sessionXp += amount;
        int total = currentLevel.getRequiredCumulatedXp() + xp;
        if (total >= nextLevel.getRequiredCumulatedXp()) {
            level++;
            xp = total - nextLevel.getRequiredCumulatedXp();
            if (nextLevel.getRewardedRushcoins() > 0) {
                rushcoins += nextLevel.getRewardedRushcoins();
                player.sendMessage("§eVous avez gagné " + nextLevel.getRewardedRushcoins() + " RushCoins.");
            } else {
                player.sendMessage(nextLevel.getCustomLevelUpMessage());
            }
            player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            new Title(" ", "§eNiveau supérieur !").send(player);
            Bukkit.getServer().broadcastMessage("§e" + player.getName() + " est passé niveau §e§l" + level + "§e !");
            Firework f = (Firework) player.getPlayer().getWorld().spawn(player.getLocation(), Firework.class);
            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(FireworkEffect.builder()
                    .flicker(false)
                    .trail(false)
                    .with(Type.BALL_LARGE)
                    .withColor(Color.YELLOW)
                    .build());
            fm.setPower(0);
            f.setFireworkMeta(fm);
            Bukkit.getScheduler().runTaskLater(BukkitInjector.getApi().getRushland(), new Runnable() {
                @Override
                public void run() {
                    f.detonate();
                }
            }, 1L);
        }
    }
    
    public String generateXpBar() {
        int next = level + 1;
        Leveling currentLevel = Leveling.valueOf("LEVEL_" + level);
        Leveling nextLevel = Leveling.valueOf("LEVEL_" + next);
        int total = currentLevel.getRequiredCumulatedXp() + xp;
        int percent = (total / nextLevel.getRequiredCumulatedXp()) * 100;
        int greenAmount = percent / 10;
        int redAmount = 10 - greenAmount;
        String bar = "§a";
        for (int i = 0; i < greenAmount + 1; i++) {
            bar = bar + "█";
        }
        if (redAmount > 0) {
            bar = bar + "§c";
            for (int i = 0; i < redAmount + 1; i++) {
                bar = bar + "█";
            }
        }
        return "§e" + percent + "% " + bar;
    }
    
    public int getRequiredXp() {
        int next = level + 1;
        Leveling currentLevel = Leveling.valueOf("LEVEL_" + level);
        Leveling nextLevel = Leveling.valueOf("LEVEL_" + next);
        int total = currentLevel.getRequiredCumulatedXp() + xp;
        return nextLevel.getRequiredCumulatedXp() - total;
    }

    public String getRank() {
        return this.rank;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public String getKarmaRank() {
        return karma;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getRushcoins() {
        return this.rushcoins;
    }

    public int getShopcoins() {
        return this.shopcoins;
    }

    public int getLevel() {
        return this.level;
    }

    public int getXp() {
        return this.xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void callEvent() {
        PlayerLoadedEvent event = new PlayerLoadedEvent(this.player, this);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public void remove() {
        api.getDataManager().getMoneyAPI().updateMoney(this);
        final UUID finalUuid = uuid;
        Bukkit.getScheduler().runTaskAsynchronously(this.api.getRushland(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement pst = api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET xp = ? WHERE uuid = ?");
                    pst.setInt(1, xp);
                    pst.setString(2, finalUuid.toString());
                    pst.executeUpdate();

                    PreparedStatement pst2 = api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET level = ? WHERE uuid = ?");
                    pst2.setInt(1, level);
                    pst2.setString(2, finalUuid.toString());
                    pst2.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.player = null;
        this.uuid = null;
        api.getPlayerList().remove(this);
    }
}
