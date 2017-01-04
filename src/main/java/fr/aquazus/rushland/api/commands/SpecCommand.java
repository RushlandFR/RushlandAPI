package fr.aquazus.rushland.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.data.PlayerInfo;
import fr.aquazus.rushland.api.utils.PermLevel;

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

public class SpecCommand implements CommandExecutor {

    @SuppressWarnings("unused")
    private RushlandAPI api;

    public SpecCommand(RushlandAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (PlayerInfo.get(player).getMaxPermLevel() >= 30) {
                if (player.getWorld().getName().equalsIgnoreCase("hub") && PlayerInfo.get(player).getMaxPermLevel() < 40) {
                    player.sendMessage("§cVous ne pouvez pas vous mettre en spec sur le hub.");
                    return true;
                }
                if (player.getGameMode() != GameMode.SPECTATOR) {
                    player.setGameMode(GameMode.SPECTATOR);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "Votre mode de jeu est maintenant spectateur");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (PlayerInfo.get(p).getMaxPermLevel() <= 5) {
                            p.hidePlayer(player);
                        }
                    }
                } else {
                    player.setGameMode(GameMode.ADVENTURE);
                    giveFly(player);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "Votre mode de jeu est maintenant aventure");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(player);
                    }
                }
            }
        }
        return true;
    }

    public void giveFly(Player player) {
        if (PlayerInfo.get(player).getMaxPermLevel() >= PermLevel.MODERATEUR.getLevel()) {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1f);
        } else if (PlayerInfo.get(player).getRank().equals("youtuber")) {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1f);
        } else if (PlayerInfo.get(player).getKarmaRank().equals("emeraude")) {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1f);
        } else if (PlayerInfo.get(player).getRank().equals("builder")) {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1f);
        }
    }
}
