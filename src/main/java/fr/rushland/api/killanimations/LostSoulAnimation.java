package fr.rushland.api.killanimations;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.utils.CodeUtils;

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
