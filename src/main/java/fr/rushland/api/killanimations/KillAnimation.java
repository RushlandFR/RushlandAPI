package fr.rushland.api.killanimations;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
