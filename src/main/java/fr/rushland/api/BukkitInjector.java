package fr.rushland.api;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitInjector extends JavaPlugin {

    private static RushlandAPI api;

    public void onLoad() {
        api = new RushlandAPI(this);
    }

    public void onEnable() {
        api.enable();
    }

    public void onDisable() {
        api.disable();
    }

    public static RushlandAPI getApi() {
        return api;
    }
}
