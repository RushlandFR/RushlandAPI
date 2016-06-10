package fr.rushland.api.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;
import fr.rushland.api.utils.UUIDLib;

public class AddRushCoinsCommand implements CommandExecutor {

    private RushlandAPI api;

    public AddRushCoinsCommand(RushlandAPI api) {
        this.api = api;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInfo pInfo = PlayerInfo.get(player.getUniqueId());
            if (pInfo.getMaxPermLevel() < 40) {
                player.sendMessage("§cVous n'avez pas la permission de faire ceci.");
                return true;
            }
        }
        if (args.length == 2) {
            String pseudo = args[0];
            UUID uuid = UUIDLib.getID(pseudo);
            if (uuid == null) {
                sender.sendMessage("§cLe joueur sélectionné n'éxiste pas !");
                return true;
            }
            if (!this.api.getDataManager().getPlayerDB().isInsert(uuid)) {
                sender.sendMessage("§cCe joueur ne s'est jamais connecté sur Rushland, impossible d'ajouter les RushCoins.");
                return true;
            }
            int rushcoins;
            try {
                rushcoins = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[1] + " n'est pas un nombre valide.");
                return true;
            }
            this.api.getDataManager().getMoneyAPI().addPlayermoney(uuid, "rushcoins", rushcoins);
            sender.sendMessage("§aVous avez ajouté §2" + rushcoins + "§a RushCoins à §2" + pseudo + "§a.");
        } else {
            sender.sendMessage("§cUtilisation: /addrushcoins <pseudo> <quantité>");
        }
        return true;
    }

}
