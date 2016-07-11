package fr.aquazus.rushland.api.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aquazus.rushland.api.RushlandAPI;
import fr.aquazus.rushland.api.data.PlayerInfo;
import fr.aquazus.rushland.api.utils.UUIDLib;

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
