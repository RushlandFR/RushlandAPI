package fr.rushland.api.commands;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class CommandManager {

    private RushlandAPI api;
    private BukkitInjector rushland;

    public CommandManager(RushlandAPI api, BukkitInjector rushland) {
        this.api = api;
        this.rushland = rushland;
    }

    public void load() {
        this.rushland.getCommand("grade").setExecutor(new RankCommand(this.api));
        this.rushland.getCommand("spec").setExecutor(new SpecCommand(this.api));
        this.rushland.getCommand("tp").setExecutor(new TpCommand(this.api));
        this.rushland.getCommand("leave").setExecutor(new LeaveCommand(this.api));
        this.rushland.getCommand("aacautoreport").setExecutor(new ReportCommand(this.api));
        this.rushland.getCommand("rlshop").setExecutor(new RlshopCommand(this.api));
        this.rushland.getCommand("addshopcoins").setExecutor(new AddShopCoinsCommand(this.api));
        this.rushland.getCommand("addrushcoins").setExecutor(new AddRushCoinsCommand(this.api));
    }
}
