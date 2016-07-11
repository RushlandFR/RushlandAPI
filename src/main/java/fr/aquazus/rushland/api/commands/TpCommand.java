package fr.aquazus.rushland.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.data.PlayerInfo;

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
