package fr.aquazus.rushland.api;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Charsets;

import fr.aquazus.rushland.api.commands.CommandManager;
import fr.aquazus.rushland.api.data.DataManager;
import fr.aquazus.rushland.api.data.PlayerInfo;
import fr.aquazus.rushland.api.event.EventManager;
import fr.aquazus.rushland.api.redis.JedisFactory;
import fr.aquazus.rushland.api.redis.RedisDataSender;
import fr.aquazus.rushland.api.rushcoins.RushcoinsManager;
import fr.aquazus.rushland.api.utils.Config;

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

public class RushlandAPI {

    private BukkitInjector rushland;

    private DataManager datamanager;
    private CommandManager commandemanager;
    private Config config;
    private EventManager eventmanager;
    private RushcoinsManager rushcoinsManager;
    private ProtocolManager protocolManager;
    public boolean disableXpRecap = false;

    public ArrayList<PlayerInfo> playerList = new ArrayList<>();

    public RushlandAPI(BukkitInjector rushland){
        this.rushland = rushland;
        this.config = new Config(this.rushland, this);
        this.datamanager = new DataManager(this.rushland, this);
        this.commandemanager = new CommandManager(this, this.rushland);
        this.eventmanager = new EventManager(this.rushland, this);
        this.rushcoinsManager = new RushcoinsManager(this.rushland, this);
    }

    public BukkitInjector getRushland() {
        return this.rushland;
    }

    public RushcoinsManager getRushcoinsManager() {
        return this.rushcoinsManager;
    }

    public void enable() {
        JedisFactory.getInstance();
        File logsFolder = new File("logs/");
        File filesList[] = logsFolder.listFiles();
        for (int i = 0; i < filesList.length; i++) {
            String fileName = filesList[i].getName();
            if (fileName.endsWith(".log.gz")) {
                filesList[i].delete();
            }
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(rushland, "ExecConsoleCmd");
        Bukkit.getMessenger().registerOutgoingPluginChannel(rushland, "ExecPlayerCmd");

        this.commandemanager.load();
        this.config.initConf();
        this.datamanager.connect();
        this.datamanager.initData();
        this.eventmanager.registerEvent();
        this.rushcoinsManager.load();
        this.getDataManager().moneylist.add("shopcoins");
        this.getDataManager().moneylist.add("rushcoins");

        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(rushland, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    event.setCancelled(true);
                }
            }
        });
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    public void disable() {
        RedisDataSender.getPublisher.publish(RedisDataSender.serverId + "#delete#" + RedisDataSender.ports);
        try {
            this.datamanager.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void runBungeeConsoleCommand(Player player, String command) {
        player.sendPluginMessage(rushland, "ExecConsoleCmd", command.getBytes(Charsets.UTF_8));
    }

    public void runBungeePlayerCommand(Player player, String command) {
        String name = player.getName();
        String pluginMessage = name + "#" + command;
        player.sendPluginMessage(rushland, "ExecPlayerCmd", pluginMessage.getBytes(Charsets.UTF_8));
    }

    public DataManager getDataManager() {
        return this.datamanager;
    }



    public PlayerInfo getPlayerInfo(Player player) {
        return PlayerInfo.get(player);
    }

    public CommandManager getCommandeManager() {
        return this.commandemanager;
    }
    public ArrayList<PlayerInfo> getPlayerList() {
        return this.playerList;
    }


}
