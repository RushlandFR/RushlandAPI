package fr.aquazus.rushland.api.killanimations;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aquazus.rushland.api.BukkitInjector;

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

public class LostSoulAnimation {

    public LostSoulAnimation(UUID uuid, Location killedLoc) {
        Player player = Bukkit.getPlayer(uuid);
        Bat bat = player.getWorld().spawn(killedLoc, Bat.class);
        ArmorStand ghost = bat.getWorld().spawn(bat.getLocation(), ArmorStand.class);

        bat.setPassenger(ghost);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 160, 1));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BukkitInjector.getApi().getRushland(), new Runnable() {
            @Override
            public void run() {
                //Bukkit.getScheduler().cancelTask(task);
            }
        }, 0L, 2L);
    }
}
