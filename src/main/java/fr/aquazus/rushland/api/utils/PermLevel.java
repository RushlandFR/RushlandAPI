package fr.aquazus.rushland.api.utils;

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

public enum PermLevel {

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

    JOUEUR(0),
    GUIDE(20),
    MODERATEUR(30),
    BUILDER(10),
    GERANT(40),
    RESPBUILDER(40),
    DEVELOPPEUR(60),
    DEVELOPPEURWEB(60),
    RESPCOMM(50),
    ADMINISTRATEUR(100),
    YOUTUBEUR(5),
    STREAMER(5),
    GRAPHISTE(10);

    PermLevel(int level) {
        this.level = level;
    }

    private int level = 0;

    public int getLevel() {
        return this.level;
    }
}
