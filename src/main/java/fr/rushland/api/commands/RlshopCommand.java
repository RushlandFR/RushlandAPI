package fr.rushland.api.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;
import fr.rushland.api.utils.UUIDLib;

public class RlshopCommand implements CommandExecutor {

    private RushlandAPI api;

    public RlshopCommand(RushlandAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("§cCommande introuvable.");
            return true;
        }
        
        String action = args[0];
        UUID uuid = UUIDLib.getID(args[1]);
        if (uuid == null) {
            this.api.getRushland().getLogger().severe("Transaction failed. UUID is null. Data: " + action + " " + args[1] + " " + args[2]);
            return true;
        }
        try {
            Player player = Bukkit.getPlayer(uuid);
            PlayerInfo pInfo;
            if (player != null) {
                pInfo = PlayerInfo.get(player);
            } else {
                pInfo = new PlayerInfo(uuid);
            }
            
            if (action.equalsIgnoreCase("rank")) {
                String rank = args[2];
                
                if (pInfo.getKarmaRank().equalsIgnoreCase("emeraude")) {
                    this.api.getRushland().getLogger().severe("Transaction failed. Player is emerald. Data: " + action + " " + args[1] + " " + args[2]);
                    return true;
                }
                
                if (pInfo.getKarmaRank().equalsIgnoreCase(rank)) {
                    this.api.getDataManager().getPlayerDB().addMonth(uuid);
                    if (player != null) {
                        player.kickPlayer("§aVous avez bien renouvellé votre grade §2" + rank);
                    }
                } else {
                    pInfo.karma = rank;
                    this.api.getDataManager().getPlayerDB().setKarmaPlayer(uuid, rank);
                    if (player != null) {
                        player.kickPlayer("§aVous avez bien reçu votre grade §2" + rank);
                    }
                }
                
                if (pInfo.getKarmaRank().equalsIgnoreCase("player")) {
                    pInfo.karma = rank;
                    this.api.getDataManager().getPlayerDB().setKarmaPlayer(uuid, rank);
                    if (player != null) {
                        player.kickPlayer("§aVous avez bien reçu votre grade §2" + rank);
                    }
                } else if (pInfo.getKarmaRank().equalsIgnoreCase(rank)) {
                    if (pInfo.getKarmaRank().equalsIgnoreCase("emeraude")) {
                        this.api.getRushland().getLogger().severe("Transaction failed. Player is emerald. Data: " + action + " " + args[1] + " " + args[2]);
                        return true;
                    }
                    
                }
            } else if (action.equalsIgnoreCase("shopcoins")) {
                int amount = Integer.parseInt(args[2]);
                pInfo.shopcoins += amount;
                this.api.getDataManager().getMoneyAPI().setPlayermoney(uuid, "shopcoins", pInfo.shopcoins);
                if (player != null) {
                    player.kickPlayer("§aVous avez bien reçu vos §e" + amount + "§a ShopCoins.");
                }
            }
        } catch (Exception e) {
            this.api.getRushland().getLogger().severe("Transaction failed. Exception occured. Data: " + action + " " + args[1] + " " + args[2]);
            return true;
        }
        
        return true;
    }
}
