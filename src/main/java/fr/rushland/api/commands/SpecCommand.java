package fr.rushland.api.commands;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;
import fr.rushland.api.utils.PermLevel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                        p.showPlayer(p);
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
