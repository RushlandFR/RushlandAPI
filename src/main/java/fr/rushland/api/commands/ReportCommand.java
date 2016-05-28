package fr.rushland.api.commands;


import fr.rushland.api.RushlandAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    private RushlandAPI pl;

    public ReportCommand(RushlandAPI pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Vous n'êtes pas autorisé à faire cette commande.");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String message = sb.toString().trim();
        pl.runBungeeConsoleCommand(player, message);
        return true;
    }
}
