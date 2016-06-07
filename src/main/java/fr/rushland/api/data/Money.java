
package fr.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class Money {

    private RushlandAPI api;

    public Money(BukkitInjector rushland, RushlandAPI api) {
        this.api = api;
    }

    public boolean updateMoney(PlayerInfo pInfo) {
        setPlayermoney(pInfo.getPlayer(), "rushcoins", pInfo.getRushcoins());
        setPlayermoney(pInfo.getPlayer(), "shopcoins", pInfo.getShopcoins());
        return true;
    }

    public boolean addPlayermoney(Player player, String moneyname, int addvalue) {
        try {
            if (!this.api.getDataManager().moneylist.contains(moneyname)) {
                return false;
            }
            if (player.isOnline()) {
                PlayerInfo pInfo = PlayerInfo.get(player);
                switch (moneyname) {
                    case ("rushcoins"):
                        pInfo.rushcoins += addvalue;
                    break;
                    case ("shopcoins"):
                        pInfo.shopcoins += addvalue;
                    break;
                }
            } else {
                if (!api.getDataManager().getPlayerDB().isInsert(player)) {
                    return false;
                }
            }
            if (moneyname.equalsIgnoreCase("rushcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET rushcoins = rushcoins + ? WHERE uuid = ?");

                pst.setDouble(1, addvalue);
                pst.setString(2, player.getUniqueId().toString());

                pst.executeUpdate();
                pst.close();
            } else if (moneyname.equalsIgnoreCase("shopcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET shopcoins = shopcoins + ? WHERE uuid = ?");
                pst.setInt(1, addvalue);
                pst.setString(2, player.getUniqueId().toString());
                pst.executeUpdate();
                pst.close();
            } else {
                return false;
            }
        } catch (SQLException exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public int getPlayerMoney (Player player, String moneyname) {
        try {
            if (moneyname.equalsIgnoreCase("rushcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT rushcoins FROM PlayerInfo WHERE uuid = ?");

                pst.setString(1, player.getUniqueId().toString());
                pst.executeQuery();
                ResultSet result = pst.getResultSet();
                if (result.next()) {
                    return result.getInt(1);
                }
                pst.close();
            } else if (moneyname.equalsIgnoreCase("shopcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT shopcoins FROM PlayerInfo WHERE uuid = ?");
                pst.setString(1, player.getUniqueId().toString());
                pst.executeQuery();
                ResultSet result = pst.getResultSet();
                if (result.next()) {
                    return result.getInt(1);
                }
                pst.close();
            } 
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }
    public boolean removePlayermoney(Player player, String moneyname, int removevalue) {
        try {
            if (!this.api.getDataManager().moneylist.contains(moneyname)) {
                return false;
            }
            if (player.isOnline()) {
                PlayerInfo pInfo = PlayerInfo.get(player);
                switch (moneyname) {
                    case ("rushcoins"):
                        pInfo.rushcoins -= removevalue;
                    break;
                    case ("shopcoins"):
                        pInfo.shopcoins -= removevalue;
                    break;
                }
            }
            if (moneyname.equalsIgnoreCase("rushcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET rushcoins = rushcoins - ? WHERE uuid = ?");

                pst.setInt(1, removevalue);
                pst.setString(2, player.getUniqueId().toString());

                pst.executeUpdate();
                pst.close();
            } else if(moneyname.equalsIgnoreCase("shopcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET shopcoins = shopcoins - ? WHERE uuid = ?");

                pst.setInt(1, removevalue);
                pst.setString(2, player.getUniqueId().toString());

                pst.executeUpdate();
                pst.close();
            } else {
                return false;
            }
        } catch (SQLException exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setPlayermoney (Player player, String moneyname, int setvalue) {
        if (setvalue < 0) {
            return true;
        }
        try {
            if (!this.api.getDataManager().moneylist.contains(moneyname)) {
                return false;
            }
            if (player.isOnline()) {
                PlayerInfo pInfo = PlayerInfo.get(player);
                switch (moneyname) {
                    case ("rushcoins"):
                        pInfo.rushcoins = setvalue;
                    break;
                    case ("shopcoins"):
                        pInfo.shopcoins = setvalue;
                    break;
                }
            }
            if (moneyname.equalsIgnoreCase("rushcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET rushcoins = ? WHERE uuid = ?");

                pst.setInt(1, setvalue);
                pst.setString(2, player.getUniqueId().toString());

                pst.executeUpdate();

            } else if (moneyname.equalsIgnoreCase("shopcoins")) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET shopcoins = ? WHERE uuid = ?");
                pst.setInt(1, setvalue);
                pst.setString(2, player.getUniqueId().toString());

                pst.executeUpdate();
            } else {
                return false;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
