/*
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt'.
 * Copyright (C) 2016 by DOCQUIER B. and RUSHLAND . 
 * All right reserved.  
 */

package fr.rushland.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;

public class MoneyCommand implements CommandExecutor {

	private RushlandAPI api;

	public MoneyCommand(RushlandAPI api) {
		this.api = api;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PlayerInfo pInfo = PlayerInfo.get(player.getUniqueId());
			if (pInfo.getRank().equals("player")) {
				player.sendMessage(ChatColor.RED + "Vous possédez: ");
				player.sendMessage(" - "+ pInfo.getRushcoins() + " rushcoins.");
				player.sendMessage(" - "+ pInfo.getShopcoins() + " shopcoins.");
				return true;
			}
		}

		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				PlayerInfo pInfo = PlayerInfo.get(player.getUniqueId());
				player.sendMessage(ChatColor.RED + "Vous possédez: ");
				player.sendMessage(" - "+ pInfo.getRushcoins() + " rushcoins.");
				player.sendMessage(" - "+ pInfo.getShopcoins() + " shopcoins.");
			} else {
				this.sendCommandInfo(sender);
			}
		} else if (args.length == 1) {
			String argument1 = args[0];
			if (argument1.equalsIgnoreCase("help")) {
				this.sendCommandInfo(sender);
			} else {
				if (sender instanceof Player) {
					Player pl = (Player) sender;
					PlayerInfo pInfo = PlayerInfo.get(pl);

					int permlvl = pInfo.getMaxPermLevel();
					if (permlvl < 10) {
						return true;
					}
				} try {
					Player player = (Player) Bukkit.getOfflinePlayer(args[0]);
					if (this.api.getDataManager().getPlayerDB().isInsert(player)) {
						sender.sendMessage(ChatColor.RED + "Le joueur: " + ChatColor.WHITE +  player.getName() + ChatColor.RED + " possède:"); //TODO Request indiv rushcoins and shopcoins.
						sender.sendMessage("  - "+ this.api.getDataManager().getMoneyAPI().getPlayerMoney(player, "rushcoins")+" rushcoins !");
						sender.sendMessage("  - "+ this.api.getDataManager().getMoneyAPI().getPlayerMoney(player, "shopcoins")+" shopcoins !");
					} else {
						sender.sendMessage(ChatColor.RED + "Le joueur sélectionné n'éxiste pas !");
					}
				} catch (Exception e){
					sender.sendMessage(ChatColor.RED + "Le joueur sélectionné n'éxiste pas !");
				}
			}
		} else if(args.length == 4) {
			String pseudo = args[0];
			String parametre1 = args[1];
			int coins = Integer.valueOf(args[2]);
			String parametre2 = args[3];

			if (sender instanceof Player) {
				Player player = (Player) sender;
				PlayerInfo pInfo = PlayerInfo.get(player);

				int permlvl = pInfo.getMaxPermLevel();

				if (permlvl < 60) {
					player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'éxecuter cette commande !");
					return true;
				}
			}
			try {
				Player pl = (Player) Bukkit.getOfflinePlayer(pseudo);
				if (parametre1.equalsIgnoreCase("addcoins")) {
					if (parametre2.equals("rushcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage(ChatColor.GOLD + "Vous avez ajouté §2"+coins+" §c"+parametre2+"§6 § §2"+pseudo+" §6!");
					} else if (parametre2.equals("shopcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage("§6Vous avez ajouté §2"+coins+" §c"+parametre2+"§6 à §2"+pseudo+" §6!");
					} else {
						sender.sendMessage("§cCette monnaie n'existe pas !");
					}

				} else if (parametre1.equalsIgnoreCase("removecoins")) {
					if (parametre2.equals("rushcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage("§6Vous avez retiré §2"+coins+" §c"+parametre2+"§6 à §2"+pseudo+" §6!");
					} else if (parametre2.equals("shopcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage("§6Vous avez retiré §2"+coins+" §c"+parametre2+"§6 à §2"+pseudo+" §6!");
					} else {
						sender.sendMessage("§cCette monnaie n'existe pas !");
					}
				} else if (parametre1.equalsIgnoreCase("setcoins")) {
					if (parametre2.equals("rushcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage("§6Vous avez set le compte de §2"+pseudo+"§6 à §2"+coins+" §c"+parametre2+" §6!");
					} else if (parametre2.equals("shopcoins")) {
						this.api.getDataManager().getMoneyAPI().addPlayermoney(pl, parametre2, coins);
						sender.sendMessage("§6Vous avez set le compte de §2"+pseudo+"§6 à §2"+coins+" §c"+parametre2+" §6!");
					} else {
						sender.sendMessage("§cCette monnaie n'existe pas !");
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} catch (Exception e) {
				sender.sendMessage("§cLe joueur sélectionné n'éxiste pas !");
			}
		} else {
			sendCommandInfo(sender);
		}
		return true;
	}

	private void sendCommandInfo(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PlayerInfo pInfo = PlayerInfo.get(player);
			if (pInfo.getMaxPermLevel() < 30) return;
			player.sendMessage("§6============== §2Coins  :  Help §6==============");
			player.sendMessage("§2/money §6: Connaitre votre nombre de coins");
			player.sendMessage("§2/money 'pseudo' §6:Connaitre le nombre de coins d'un joueur !");
			player.sendMessage("§2/money 'pseudo' addcoins/removecoins/setcoins 'nb coins' rushcoins/shopcoins §6: Modifier le nombre de coins !");
			player.sendMessage("§6========================================");
		} else {
			sender.sendMessage("§6============== §2Coins  :  Help §6==============");
			sender.sendMessage("§2/money 'pseudo' §6:Connaitre le nombre de coins d'un joueur !");
			sender.sendMessage("§2/money 'pseudo' addcoins/removecoins/setcoins 'nb coins' rushcoins/shopcoins §6: Modifier le nombre de coins !");
			sender.sendMessage("§6============================================");
		}
	}
}
