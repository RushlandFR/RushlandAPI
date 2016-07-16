package fr.aquazus.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import fr.aquazus.rushland.api.BukkitInjector;
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

public class KarmaDB {

    @SuppressWarnings("unused")
    private BukkitInjector rushland;
    private RushlandAPI api;
    private HashMap<String, Integer> karmaList = new HashMap<>();

    public HashMap<String, Integer> getKarmaList() {
        return karmaList;
    }

    public KarmaDB(BukkitInjector rushland, RushlandAPI api){
        this.api = api;
        this.rushland = rushland;
    }

    public void insertKarmaList() {
        int karmaSize = getAllKarmaRank();
        for (int i = 1; i <= karmaSize; i++) {
            String karma = getKarmaName(i);
            karmaList.put(karma, getKarmaLevel(karma));
        }
    }

    public int getAllKarmaRank()  {
        int maxSub = 0;

        try {
            PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("SELECT MAX(id) FROM KarmaSystem;");
            queryStatement.executeQuery();
            ResultSet resultSet = queryStatement.getResultSet();
            while (resultSet.next()) {
                maxSub = resultSet.getInt(1);
            }
            return maxSub;		
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxSub;
    }

    public String getKarmaName(int id) {
        try {
            String rank;

            PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT karma FROM KarmaSystem WHERE id = ?");
            pst.setInt(1, id);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();

            if (result.next()) {
                rank = result.getString(1);
            } else {
                rank = "null";
            }

            return rank;
        } catch (SQLException e){
            e.printStackTrace();
            return "null";
        }
    }

    public int getKarmaLevel(String karma) {
        try {
            PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT permLevel FROM KarmaSystem WHERE karma = ?");
            pst.setString(1, karma);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if(result.next()) {
                return result.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isKarmaExist(String karmaname) {
        try {
            PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT id FROM KarmaSystem WHERE karma = ?");

            pst.setString(1, karmaname);
            ResultSet result = pst.executeQuery();

            if (result.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addnewKarma(String karmaname, int defaultKarmaLevel) {
        try {
            if (!karmaList.containsKey(karmaname)) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO KarmaSystem (karma, permLevel) VALUES(?, ?)");

                pst.setString(1, karmaname);
                pst.setInt(2, defaultKarmaLevel);
                pst.executeUpdate();
                pst.close();
                karmaList.put(karmaname, defaultKarmaLevel);

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeKarma(String karmaName) {
        try {
            if (!karmaList.containsKey(karmaName)) {
                PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("DELETE FROM KarmaSystem WHERE karma= ?");
                pst.setString(1, karmaName);
                pst.executeUpdate();
                pst.close();
                karmaList.remove(karmaName);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
