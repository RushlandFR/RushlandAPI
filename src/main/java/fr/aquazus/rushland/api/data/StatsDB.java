package fr.aquazus.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

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

public class StatsDB {

    private BukkitInjector rushland;
    private RushlandAPI api;

    private String gameType;
    private boolean useKills;
    private boolean useDeaths;
    private boolean useWins;
    private boolean useLoses;

    private HashMap<String, Integer> kills = new HashMap<String, Integer>();
    private HashMap<String, Integer> deaths = new HashMap<String, Integer>();
    private ArrayList<String> wins = new ArrayList<String>();
    private ArrayList<String> loses = new ArrayList<String>();
    private HashMap<String, String> lastKill = new HashMap<>();
    private HashMap<String, String> lastDeath = new HashMap<>();

    public StatsDB(BukkitInjector rushland, RushlandAPI api) {
        this.api = api;
        this.rushland = rushland;
    }

    public void addCooldownKill(String kill, String death) {
        lastKill.put(kill, death);
        Bukkit.getScheduler().runTaskLater(api.getRushland(), new Runnable() {

            @Override
            public void run() {
                lastKill.remove(kill);
            }
        }, 20L * 15);
    }

    public void addCooldownDeath(String death, String kill) {
        lastDeath.put(death, kill);
        Bukkit.getScheduler().runTaskLater(api.getRushland(), new Runnable() {

            @Override
            public void run() {
                lastDeath.remove(death);
            }
        }, 20L * 15);
    }

    public void configStats(String gameType, boolean useKills, boolean useDeaths, boolean useWins, boolean useLoses) {
        this.gameType = gameType;
        this.useKills = useKills;
        this.useDeaths = useDeaths;
        this.useWins = useWins;
        this.useLoses = useLoses;
    }


    public boolean isInsert(String uuid) {
        try {
            PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("SELECT id FROM stats_" + gameType + " WHERE uuid = ?");
            queryStatement.setString(1, uuid);
            queryStatement.executeQuery();
            ResultSet resultSet = queryStatement.getResultSet();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addNewPlayer(String uuid) {
        try {
            PreparedStatement queryStatement = null;
            if (useKills && useWins) {
                queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, kills, deaths, wins, loses) VALUES (?, ?, ?, ?, ?)");

            } else if (useKills) {
                queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, kills, deaths) VALUES (?, ?, ?)");
            } else if (useWins) {
                queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, wins, loses) VALUES (?, ?, ?)");

            }

            queryStatement.setString(1, uuid);
            queryStatement.setInt(2, 0);
            queryStatement.setInt(3, 0);
            if (useKills && useWins) {
                queryStatement.setInt(4, 0);
                queryStatement.setInt(5, 0);
            }
            queryStatement.executeUpdate();
            queryStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getKills(String uuid) {
        if (kills.containsKey(uuid)) {
            return kills.get(uuid);
        } else {
            return 0;
        }
    }

    public void insertPlayer(String uuid) {
        try {
            rushland.getLogger().info("Inserting stats into SQL Server...");

            if (useKills && !kills.isEmpty() && kills.containsKey(uuid)) {
                int value = kills.get(uuid);
                PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET kills = kills + ? WHERE uuid = ?");
                queryStatement.setInt(1, value);
                queryStatement.setString(2, uuid);
                queryStatement.executeUpdate();
                queryStatement.close();
                kills.remove(uuid);
            }
            if (useDeaths && !deaths.isEmpty() && deaths.containsKey(uuid)) {
                int value = deaths.get(uuid);
                PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET deaths = deaths + ? WHERE uuid = ?");
                queryStatement.setInt(1, value);
                queryStatement.setString(2, uuid);
                queryStatement.executeUpdate();
                queryStatement.close();
                deaths.remove(uuid);
            }

            if (useWins && !wins.isEmpty() && wins.contains(uuid)) {
                PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET wins = wins + 1 WHERE uuid = ?");
                queryStatement.setString(1, uuid);
                queryStatement.executeUpdate();
                queryStatement.close();
            }
            if (useLoses && !loses.isEmpty() && loses.contains(uuid)) {
                PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET loses = loses + 1 WHERE uuid = ?");
                queryStatement.setString(1, uuid);
                queryStatement.executeUpdate();
                queryStatement.close();
            }
            clearPlayer(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert() {
        try {
            rushland.getLogger().info("Inserting stats into SQL Server...");

            if (useKills && !kills.isEmpty()) {
                for (Entry<String, Integer> entry : kills.entrySet()) {
                    String uuid = entry.getKey();
                    int value = entry.getValue();
                    PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET kills = kills + ? WHERE uuid = ?");
                    queryStatement.setInt(1, value);
                    queryStatement.setString(2, uuid);
                    queryStatement.executeUpdate();
                    queryStatement.close();
                }
            }
            if (useDeaths && !deaths.isEmpty()) {
                for (Entry<String, Integer> entry : deaths.entrySet()) {
                    String uuid = entry.getKey();
                    int value = entry.getValue();
                    PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET deaths = deaths + ? WHERE uuid = ?");
                    queryStatement.setInt(1, value);
                    queryStatement.setString(2, uuid);
                    queryStatement.executeUpdate();
                    queryStatement.close();
                }
            }
            if (useWins && !wins.isEmpty()) {
                for (String uuid : wins) {
                    PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET wins = wins + 1 WHERE uuid = ?");
                    queryStatement.setString(1, uuid);
                    queryStatement.executeUpdate();
                    queryStatement.close();
                }
            }
            if (useLoses && !loses.isEmpty()) {
                for (String uuid : loses) {
                    PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET loses = loses + 1 WHERE uuid = ?");
                    queryStatement.setString(1, uuid);
                    queryStatement.executeUpdate();
                    queryStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addKill(String uuid, String killed) {
        if (lastKill.containsKey(uuid)) {
            if (lastKill.get(uuid).equals(killed)) {
                return;
            }
        }
        addCooldownKill(uuid, killed);
        if (kills.containsKey(uuid)) {
            int oldKills = kills.get(uuid);
            kills.put(uuid, oldKills + 1);
        } else {
            kills.put(uuid, 1);
        }
    }

    private void clearPlayer(String uuid) {
        if (kills.containsKey(uuid)) {
            kills.remove(uuid);
        }
        if (deaths.containsKey(uuid)) {
            deaths.remove(uuid);
        }
        if (loses.contains(uuid)) {
            loses.remove(uuid);
        }
        if (wins.contains(uuid)) {
            wins.remove(uuid);
        }
    }

    public void addDeath(String uuid, String killer) {
        if (killer != null) {
            if (lastDeath.containsKey(uuid)) {
                if (lastDeath.get(uuid).equals(killer)) {
                    return;
                }
            }
            addCooldownDeath(uuid, killer);
        }
        if (deaths.containsKey(uuid)) {
            int oldDeaths = deaths.get(uuid);
            deaths.put(uuid, oldDeaths + 1);
        } else {
            deaths.put(uuid, 1);
        }
    }

    public void addWin(String uuid) {
        wins.add(uuid);
    }

    public void addLose(String uuid) {
        loses.add(uuid);
    }
}
