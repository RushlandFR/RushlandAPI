
package fr.rushland.api.data;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mysql.jdbc.Connection;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class DataManager {

    private BukkitInjector rushland;
    private RushlandAPI api;
    private RankDB rankdb;

    private File configfile;
    private FileConfiguration config;

    private Money money;
    private StatsDB statsDB;
    private PlayerDB playerdb;
    private KarmaDB karmaDB;

    private Connection connection;

    public ArrayList<String> moneylist;


    int DataId;

    /**
     * Gestion des PermLevel:
     * 0 = joueur
     * 5 = Streamer/Youtuber
     * 10 = builder
     * 20 = guide
     * 30 = modo
     * 40 = resp. Gerant/Builder
     * 50 = Resp. Com 
     * 60 = Developpeur
     * 100 = Admin
     * 
     */

    public DataManager(BukkitInjector rushland, RushlandAPI api){
        this.rushland = rushland;
        this.api = api;
        this.money = new Money(this.rushland, this.api);
        this.moneylist = new ArrayList<String>();
        this.rankdb = new RankDB(this.rushland, this.api);
        this.statsDB = new StatsDB(this.rushland, this.api);
        this.playerdb = new PlayerDB(this.api);
        this.karmaDB = new KarmaDB(this.rushland, this.api);
    }

    public void initData() {
        getKarmaDB().insertKarmaList();
        getRankSystemDB().insertRankList();
    }

    public Money getMoneyAPI() {
        return this.money;
    }

    public KarmaDB getKarmaDB() {
        return this.karmaDB;
    }

    public RankDB getRankSystemDB() { 
        return this.rankdb;
    }

    public StatsDB getStatsDB() {
        return this.statsDB;
    }

    public PlayerDB getPlayerDB() {
        return this.playerdb;
    }

    public Connection getconnection() {
        if(isConnected()){
            return this.connection;
        } else {
            deconnection();
            connection();
            return this.connection;
        }
    }

    public boolean isConnected() {
        try {
            if((this.connection == null) || (this.connection.isClosed())) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void connection() {
        this.configfile = new File(this.rushland.getDataFolder(),"config.yml");
        this.config = YamlConfiguration.loadConfiguration(configfile);

        if (!isConnected()) {
            try {
                String ip = config.getString("database.host");
                String user = config.getString("database.user");
                String passwd = config.getString("database.password");
                String db = config.getString("database.db");
                String host="jdbc:mysql://"+ip+"/"+db, username = user, passeword = passwd;
                this.connection = (Connection) DriverManager.getConnection(host,username,passeword);

                DataId = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.rushland, new Runnable() {
                    public void run() {
                        refreshConnection();
                    }
                }, 20L*300);
            } catch (SQLException e) {
                Bukkit.getServer().shutdown();
                e.printStackTrace();
            }
        }
    }

    public void deconnection() {
        if (isConnected()) {
            try {
                this.connection.close();
                Bukkit.getScheduler().cancelTask(DataId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            this.rushland.getLogger().info("MariaDB pool is already disconnected...");
        }
    }

    public void refreshConnection() {
        this.rushland.getLogger().info("Refreshing MariaDB pool...");
        try {
            if (isConnected()) {
                deconnection();
                connection();
            } else {
                connection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }
}
