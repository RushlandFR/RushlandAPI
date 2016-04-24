package fr.rushland.api.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import fr.rushland.api.RushlandAPI;

public class LeaveCommand implements CommandExecutor {

    private RushlandAPI pl;

    public LeaveCommand(RushlandAPI pl) {
        this.pl = pl;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
        	Player player = (Player) sender;
            pl.runBungeePlayerCommand(player, "hub");
        } else {
            sender.sendMessage(ChatColor.RED + "Player only");
        }
        return true;
    }
}
