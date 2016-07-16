
package fr.aquazus.rushland.api.data;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mysql.jdbc.Connection;

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


    int dataId;

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

    public Connection getConnection() {
        if(isConnected()){
            return this.connection;
        } else {
            disconnect();
            connect();
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

    public void connect() {
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

                dataId = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.rushland, new Runnable() {
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

    public void disconnect() {
        if (isConnected()) {
            try {
                this.connection.close();
                Bukkit.getScheduler().cancelTask(dataId);
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
                disconnect();
                connect();
            } else {
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }
}
