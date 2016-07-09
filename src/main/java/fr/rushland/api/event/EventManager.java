package fr.rushland.api.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class EventManager {

    private BukkitInjector rushland;
    private RushlandAPI api;
    private PluginManager pm;

    public EventManager(BukkitInjector rushland, RushlandAPI api){
        this.rushland = rushland;
        this.api = api;
        this.pm = Bukkit.getPluginManager();
    }

    public void registerEvent() {
        this.pm.registerEvents(new PlayerListener(this.rushland,this.api), this.rushland);
    }
}
