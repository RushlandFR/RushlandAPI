package fr.rushland.api.utils;

import org.bukkit.Bukkit;

public class CodeUtils {
	
    public static void logToConsole(String message) {
        Bukkit.getLogger().info("[RushlandAPI] " + message);
    }
}
