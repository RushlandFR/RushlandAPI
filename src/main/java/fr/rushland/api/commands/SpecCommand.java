package fr.rushland.api.commands;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;
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
                        if (PlayerInfo.get(p).getMaxPermLevel() <= 5)
                            p.hidePlayer(player);
                    }
                } else {
                    player.setGameMode(GameMode.ADVENTURE);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "Votre mode de jeu est maintenant aventure");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(p);
                    }
                }
            }
        }
        return true;
    }

}
