package fr.rushland.api.data;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class PlayerInfo {

    private Player player;
    private UUID uuid;
    private RushlandAPI api = BukkitInjector.getApi();

    public int permLevel = 0;
    public int rankPermLevel = 0;
    public int rushcoins = 0;
    public int shopcoins = 0;

    /**
     * @return "player" if the target is not rank/karma
     */
    public String rank;
    public String karma;

    public boolean isFemale = false;
    public Date expire, now ;

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
            if (playerInfo.getPlayer().equals(player)) {
                return playerInfo;
            }
        }
        return null;
    }

    public PlayerInfo(Player player) {
        this.player = player;
        this.uuid = this.player.getUniqueId();
        ResultSet resultSet = api.getDataManager().getPlayerDB().getPlayerInfo(player);
        try {
            rank = resultSet.getString("playerRank");
            karma = resultSet.getString("playerKarma");
            permLevel = resultSet.getInt("permLevel");
            rushcoins = resultSet.getInt("rushcoins");
            shopcoins = resultSet.getInt("shopcoins");
            if (!rank.equalsIgnoreCase("player")) {
                if (api.getDataManager().getRankSystemDB().getRankList().containsKey(rank)) {
                    rankPermLevel = api.getDataManager().getRankSystemDB().getRankList().get(rank);
                }
                isFemale = resultSet.getBoolean("rankFemale");
            }
            if (!karma.equalsIgnoreCase("player") && !karma.equalsIgnoreCase("emeraude")) {
                expire = resultSet.getDate("expire");
                now = resultSet.getDate("now");
                if (expire.before(now)) {
                    api.getDataManager().getPlayerDB().deleteKarmaPlayer(player);
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

    public void remove() {
        api.getDataManager().getMoneyAPI().updateMoney(this);
        this.player = null;
        this.uuid = null;
        api.getPlayerList().remove(this);
    }
}
