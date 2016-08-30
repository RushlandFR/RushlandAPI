
package fr.aquazus.rushland.api.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

    private String ip, user, passwd, db, host;
    private int port;

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
        loadValues();
    }

    public void loadValues() {
        this.configfile = new File(this.rushland.getDataFolder(),"config.yml");
        this.config = YamlConfiguration.loadConfiguration(configfile);
        this.ip = config.getString("database.host");
        this.user = config.getString("database.user");
        this.port = config.getInt("database.port");
        this.passwd = config.getString("database.password");
        this.db = config.getString("database.db");
        this.host = "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.db;
    }
    
    public void startTask() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.rushland, new Runnable() {
            public void run() {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                connect();
            }
        }, 20L*300, 20L*300);
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
        return this.connection;
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(host, user, passwd);
        } catch (SQLException e) {
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }
    }
}
