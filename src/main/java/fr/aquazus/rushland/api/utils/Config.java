package fr.aquazus.rushland.api.utils;

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

public class Config {

    private BukkitInjector rushland;
    @SuppressWarnings("unused")
    private RushlandAPI api;

    public Config(BukkitInjector rushland,RushlandAPI api) {
        this.api = api;
        this.rushland = rushland;
    }

    public void initConf() {
        rushland.saveDefaultConfig();
    }
}
