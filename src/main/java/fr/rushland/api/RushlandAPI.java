package fr.rushland.api;

import java.io.File;
import java.util.ArrayList;

import fr.rushland.api.redis.RedisDataSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Charsets;

import fr.rushland.api.commands.CommandManager;
import fr.rushland.api.data.DataManager;
import fr.rushland.api.data.PlayerInfo;
import fr.rushland.api.event.EventManager;
import fr.rushland.api.utils.Config;

public class RushlandAPI {

    private BukkitInjector rushland;

    private DataManager datamanager;
    private CommandManager commandemanager;
    private Config config;
    private EventManager eventmanager;
    private ProtocolManager protocolManager;

    public ArrayList<PlayerInfo> playerList = new ArrayList<>();

    public RushlandAPI(BukkitInjector rushland){

        this.rushland = rushland;
        this.config = new Config(this.rushland, this);
        this.datamanager = new DataManager(this.rushland, this);
        this.commandemanager = new CommandManager(this, this.rushland);
        this.eventmanager = new EventManager(this.rushland,this);

    }

    public BukkitInjector getRushland() {
        return this.rushland;
    }
    public void enable() {
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
        this.datamanager.connection();
        this.datamanager.initData();
        this.eventmanager.registerEvent();
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
        this.datamanager.deconnection();
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
