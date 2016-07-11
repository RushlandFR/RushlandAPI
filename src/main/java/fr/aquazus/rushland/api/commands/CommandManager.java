package fr.aquazus.rushland.api.commands;

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
