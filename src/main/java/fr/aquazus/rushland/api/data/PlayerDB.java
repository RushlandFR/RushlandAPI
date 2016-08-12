package fr.aquazus.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.RushlandAPI;

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

public class PlayerDB {

    private RushlandAPI api;

    public PlayerDB(RushlandAPI api){
        this.api = api;
    }

    public boolean isInsert(Player player) {
        boolean insert = false;
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, player.getUniqueId().toString());
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return insert;
    }

    public boolean isInsert(UUID uuid) {
        boolean insert = false;
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid.toString());
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return insert;
    }

    public ResultSet getPlayerInfo(Player player) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("SELECT *, NOW() as now FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, player.getUniqueId().toString());
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getPlayerInfo(UUID uuid) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("SELECT *, NOW() as now FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid.toString());
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void setPlayerPermLevel(Player player , int rankLevel){
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET permLevel = ? WHERE uuid=?");

            pst.setInt(1, rankLevel);
            pst.setString(2, player.getUniqueId().toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRankPlayer(Player player, String rank, boolean isFemale) {
        try {
            if (this.api.getDataManager().getRankSystemDB().rankExist(rank)) {
                PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerRank = ?, rankFemale = ? WHERE uuid = ?");

                pst.setString(1, rank);
                pst.setBoolean(2, isFemale);
                pst.setString(3, player.getUniqueId().toString());

                pst.executeUpdate();
                pst.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setRankPlayer(UUID uuid, String rank, boolean isFemale) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerRank = ?, rankFemale = ? WHERE uuid = ?");

            pst.setString(1, rank);
            pst.setBoolean(2, isFemale);
            pst.setString(3, uuid.toString());

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setKarmaPlayer(Player player, String karma ) {
        try {
            PreparedStatement pst;
            if (karma.equalsIgnoreCase("diamant")) {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 31 DAY) WHERE uuid = ?");
            } else if (karma.equalsIgnoreCase("emeraude")) {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 100 YEAR) WHERE uuid = ?");
            } else {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 31 DAY) WHERE uuid = ?");
            }

            pst.setString(1, karma);
            pst.setString(2, player.getUniqueId().toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setKarmaPlayer(UUID uuid, String karma ) {
        try {
            PreparedStatement pst;
            if (karma.equalsIgnoreCase("diamant")) {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 31 DAY) WHERE uuid = ?");
            } else if (karma.equalsIgnoreCase("emeraude")) {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 100 YEAR) WHERE uuid = ?");
            } else {
                pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 31 DAY) WHERE uuid = ?");
            }

            pst.setString(1, karma);
            pst.setString(2, uuid.toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void set24hGold(UUID uuid) {
        try {
            PreparedStatement pst;
            pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 24 HOUR) WHERE uuid = ?");
            pst.setString(1, "or");
            pst.setString(2, uuid.toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addMonth(UUID uuid) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET expire = DATE_ADD(expire, INTERVAL 31 DAY) WHERE uuid = ?");
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteKarmaPlayer(Player player) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = player, expire = null WHERE uuid = ?");

            pst.setString(1, player.getUniqueId().toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteKarmaPlayer(UUID uuid) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = player, expire = null WHERE uuid = ?");

            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playerInit(Player player) {
        try {
            PreparedStatement pst = this.api.getDataManager().getConnection().prepareStatement("INSERT INTO PlayerInfo(uuid, playerName, permLevel, playerRank, playerKarma, rushcoins, shopcoins) VALUES(?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, player.getUniqueId().toString());
            pst.setString(2, player.getName());
            pst.setInt(3, 0);
            pst.setString(4, "player");
            pst.setString(5, "player");
            pst.setInt(6, 0);
            pst.setInt(7, 0);
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
