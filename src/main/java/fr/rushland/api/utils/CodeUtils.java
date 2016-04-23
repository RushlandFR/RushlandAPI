package fr.rushland.api.utils;

import org.bukkit.Bukkit;

/**
 * Created by Zaral on 13/04/2016.
 */
public class CodeUtils {
    public static void logToConsole(String message) {
        Bukkit.getLogger().info("[RushlandAPI] " + message);
    }

}
