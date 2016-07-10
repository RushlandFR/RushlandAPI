package fr.rushland.api.utils;

import java.util.Random;

import org.bukkit.Bukkit;

public class CodeUtils {

    public static void logToConsole(String message) {
        Bukkit.getLogger().info("[RushlandAPI] " + message);
    }
    
    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
