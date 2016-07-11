package fr.aquazus.rushland.api.killanimations;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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

public enum KillAnimation {

    LOSTSOUL(LostSoulAnimation.class);

    private Class<?> animationClass;

    KillAnimation(Class<?> animationClass) {
        this.animationClass = animationClass;
    }

    public Class<?> play(Player player, Location killedLoc) {
        Class<?> animationClass = null;
        try {
            animationClass = (Class<?>) this.animationClass.getDeclaredConstructor(UUID.class, Location.class).newInstance(player == null ? null : player.getUniqueId(), killedLoc);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return animationClass;
    }
}
