package fr.rushland.api.utils;

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
