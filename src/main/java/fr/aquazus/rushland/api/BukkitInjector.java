package fr.aquazus.rushland.api;

import org.bukkit.plugin.java.JavaPlugin;

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

public class BukkitInjector extends JavaPlugin {

    private static RushlandAPI api;

    public void onLoad() {
        api = new RushlandAPI(this);
    }

    @Override
    public void onEnable() {
        api.enable();
    }

    @Override
    public void onDisable() {
        api.disable();
    }

    public static RushlandAPI getApi() {
        return api;
    }
}
