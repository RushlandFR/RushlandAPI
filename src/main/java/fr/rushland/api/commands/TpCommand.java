package fr.rushland.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;


public class TpCommand implements CommandExecutor {

    @SuppressWarnings("unused")
    private RushlandAPI api;

    public TpCommand(RushlandAPI api) {
        this.api = api;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (PlayerInfo.get(player).getMaxPermLevel() >= 30) {
                if (args.length == 0) {
                    player.sendMessage("§cUtilisation: /tp <pseudo>");
                    return true;
                }
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    player.sendMessage("§cJoueur '" + targetName + "' introuvable !");
                    return true;
                }
                player.teleport(new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
                player.sendMessage("§5Vous vous êtes téléporté à " + targetName);
            }
        }
        return true;
    }
}
