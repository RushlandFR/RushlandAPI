package fr.rushland.api.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;
import fr.rushland.api.data.PlayerInfo;

public class RankCommand implements CommandExecutor {

	private RushlandAPI api;

	public RankCommand(RushlandAPI api) {
		this.api = api;
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			int permlvl = PlayerInfo.get(player).getMaxPermLevel();
			if (permlvl < 40) {
				return true;
			}
		}

		if (args.length == 0) {
			sendCommandInfo(sender);
		} else if (args.length == 2) {
			String parametre1 = args[0];
			if (parametre1.equalsIgnoreCase("PLAYER")) {
				String pseudo = args[1];
				try {
					Player player = (Player) Bukkit.getOfflinePlayer(pseudo);
					PlayerInfo pInfo;
					if (player.isOnline()) {
						pInfo = PlayerInfo.get(player);
					} else {
						pInfo = new PlayerInfo(player);
					}
					String grade = pInfo.getRank();
					int permlevel = pInfo.getMaxPermLevel();
					sender.sendMessage("§2"+player.getName()+"§6 est §2"+grade+ "§6 avec une perm level de §2 "+permlevel);
					sender.sendMessage("§2"+player.getName()+"§6 a le karma §2"+ pInfo.getKarmaRank());
					return true;
				} catch (Exception e) {
					sender.sendMessage("§cLe joueur sélectionné n'éxiste pas !");
				}
			} else {
				sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
				sendCommandInfo(sender);
			}
		} else if (args.length == 3) {
			String parametre1 = args[0];
			if (parametre1.equalsIgnoreCase("GROUPE")) {
				String nomgroupe = args[1];
				String parametre2 = args[2];
				if (parametre2.equalsIgnoreCase("remove")) {
					boolean remove = this.api.getDataManager().getRankSystemDB().removeRank(nomgroupe);
					if (remove) {
						sender.sendMessage("§6Le grade §2"+nomgroupe+" §6vient d'être suprimer !");
						api.getDataManager().getRankSystemDB().getRankList().remove(nomgroupe);
					} else {
						sender.sendMessage("§6Le grade ne peut §tre remove ou n'existe pas !");
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} else if (parametre1.equalsIgnoreCase("KARMA")) {
				String nomgroupe = args[1];
				String parametre2 = args[2];
				if (parametre2.equalsIgnoreCase("remove")) {
					boolean remove = this.api.getDataManager().getKarmaDB().removeKarma(nomgroupe);
					if (remove) {
						sender.sendMessage("§6Le karma §2"+nomgroupe+" §6vient d'être suprimer !");
						api.getDataManager().getKarmaDB().getKarmaList().remove(nomgroupe);
					} else {
						sender.sendMessage("§6Le karma ne peut être remove ou n'existe pas !");
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} else {
				sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
				sendCommandInfo(sender);
			}

		} else if (args.length == 4) {
			String parametre1 = args[0];
			if (parametre1.equalsIgnoreCase("GROUPE")) {
				String nomgroupe = args[1];
				String parametre2 = args[2];
				if (parametre2.equalsIgnoreCase("setdefaultlevel")) {
					try {
						int level = Integer.valueOf(args[3]);
						boolean change = this.api.getDataManager().getRankSystemDB().setdefaultlevel(1, nomgroupe, level);
						if (change) {
							sender.sendMessage("§6Le grade §2"+nomgroupe+" §6a maintenant la perm level a §2"+level);
						} else {
							sender.sendMessage("§6Le grade §2"+nomgroupe+" §6ne peut être modifier ou n'existe pas !");
						}
					} catch (Exception e) {
						sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
						sendCommandInfo(sender);
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} else if (parametre1.equalsIgnoreCase("KARMA")) {
				String nomgroupe = args[1];
				String parametre2 = args[2];
				
				if (parametre2.equalsIgnoreCase("setdefaultlevel")) {
					try {
						int level = Integer.valueOf(args[3]);
						boolean change = this.api.getDataManager().getRankSystemDB().setdefaultlevel(2, nomgroupe, level);
						if (change) {
							sender.sendMessage("§6Le karma §2"+nomgroupe+" §6a maintenant la perm level a §2"+level);
						} else {
							sender.sendMessage("§6Le karma §2"+nomgroupe+" §6ne peut être modifier ou n'existe pas !");
						}
					} catch (Exception e){
						sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
						sendCommandInfo(sender);
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} else {
				sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
				sendCommandInfo(sender);
			}

		} else if (args.length == 5) {
			String parametre1 = args[0];
			if (parametre1.equalsIgnoreCase("GROUPE")) {
				String nomgroupe = args[1];
				String parametre2 = args[2];
				if (parametre2.equalsIgnoreCase("add")) {
					int defaultranklevel = Integer.valueOf(args[3]);
					boolean create = this.api.getDataManager().getRankSystemDB().addnewRank(nomgroupe, defaultranklevel);
					
					if (create) {
						sender.sendMessage("§6Vous avez add le grade §2"+nomgroupe+" §6avec la perm level § §2"+defaultranklevel);
						api.getDataManager().getRankSystemDB().getRankList().put(nomgroupe, defaultranklevel);
					} else {
						sender.sendMessage("§6Le grade ne peut §tre rajouter ou existe déjà !");
					}
				} else {
					sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
					sendCommandInfo(sender);
				}
			} else if (parametre1.equalsIgnoreCase("KARMA")){
				String nomgroupe = args[1];
				String parametre2 = args[2];
				if (parametre2.equalsIgnoreCase("add")) {
					if (args.length == 5) {
						int defaultranklevel = Integer.valueOf(args[3]);
						boolean create = this.api.getDataManager().getKarmaDB().addnewKarma(nomgroupe, defaultranklevel);

						if (create) {
							sender.sendMessage("§6Vous avez add le karma §2"+nomgroupe+" §6avec la perm level à §2"+defaultranklevel);
							api.getDataManager().getKarmaDB().getKarmaList().put(nomgroupe, defaultranklevel);
						} else {
							sender.sendMessage("§6Le karma ne peut être rajouter ou existe déjà !");
						}
					} else {
						sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
						sendCommandInfo(sender);
					}
				}
			} else if(parametre1.equalsIgnoreCase("PLAYER")) {
				String pseudo = args[1];
				try {
					Player player = (Player) Bukkit.getOfflinePlayer(pseudo);
					String parametre2 = args[2];
					
					if (parametre2.equalsIgnoreCase("setrank")) {
						String rankname = args[3];
						String female = args[4];
						if (female.equalsIgnoreCase("true") || female.equalsIgnoreCase("false")) {
							boolean isFemale = Boolean.parseBoolean(female);
							if (this.api.getDataManager().getRankSystemDB().getRankList().containsKey(rankname)) {
								this.api.getDataManager().getPlayerDB().setRankPlayer(player, rankname, isFemale);
								if (sender instanceof Player)
									sender.sendMessage("§6Vous avez ajouter le grade §2"+rankname+" §6a§2 "+player.getName());
								if (player.isOnline()) {
									player.sendMessage(ChatColor.GREEN + "Vous avez reçu le grade " + rankname + "!");
									PlayerInfo pInfo = PlayerInfo.get(player);
									pInfo.rank = rankname;
									pInfo.rankPermLevel = api.getDataManager().getRankSystemDB().getRankList().get(rankname);
									pInfo.isFemale = isFemale;
								}
							} else {
								sender.sendMessage("§cLe rank choisit n'existe pas !");
							}
						}
					} else if(parametre2.equalsIgnoreCase("setkarma")) {
						String rankname = args[3];
						if (this.api.getDataManager().getKarmaDB().getKarmaList().containsKey(rankname)) {
							this.api.getDataManager().getPlayerDB().setKarmaPlayer(player, rankname);
							sender.sendMessage("§6Vous avez ajouter le karma §2"+rankname+" §6a§2 "+player.getName());
							if (player.isOnline()) {
								player.sendMessage(ChatColor.GREEN + "Vous avez reçu le grade " + rankname + "!");
								PlayerInfo pInfo = PlayerInfo.get(player);
								pInfo.karma = rankname;
							}
						} else {
							sender.sendMessage("§cLe karma choisit n'existe pas !");
						}
					} else if (parametre2.equalsIgnoreCase("setPermLevel")) {
						int setPermLevel = Integer.parseInt(args[3]);
						if (api.getDataManager().getPlayerDB().isInsert(player)) {
							api.getDataManager().getPlayerDB().setPlayerPermLevel(player, setPermLevel);
							sender.sendMessage(ChatColor.GREEN + "Level permission ajouté !");
							if (player.isOnline()) {
								PlayerInfo.get(player).permLevel = setPermLevel;
							}
							return true;
						}
					} else if (parametre2.equalsIgnoreCase("delrank")) {
						this.api.getDataManager().getPlayerDB().setRankPlayer(player, "player", false);

						if (sender instanceof Player)
							api.runBungeeConsoleCommand((Player) sender, "pcord user " + player + " delete");
						else 
							sender.sendMessage(ChatColor.RED + "Il faut retirer les permissions depuis le jeu ! Le changement a été effectué, mais il lui reste les perms de modération");

						if (player.isOnline()) {
							player.sendMessage(ChatColor.GREEN + "Vous avez perdu votre grade !");
							PlayerInfo pInfo = PlayerInfo.get(player);
							pInfo.rank = "player";
							pInfo.permLevel = 0;
						}
					} else if (parametre2.equalsIgnoreCase("delkarma")) {
						this.api.getDataManager().getPlayerDB().deleteKarmaPlayer(player);

						if (player.isOnline()) {
							player.sendMessage(ChatColor.GREEN + "Vous avez perdu votre grade !");
							PlayerInfo pInfo = PlayerInfo.get(player);
							pInfo.karma = "player";
						}
					} else {
						sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
						sendCommandInfo(sender);
					}
				} catch (Exception e) {
					sender.sendMessage("§cLe joueur sélectionner n'existe pas !");
				}
			}
		} else {
			sender.sendMessage("§cLes arguments de la commande sont inexistant ou incomplet !");
			sendCommandInfo(sender);
		}
		return false;
	}

	private void sendCommandInfo(CommandSender sender) {
		if (sender instanceof Player) {
			sender.sendMessage("§6============== §2Grade  :  Help §6==============");
			sender.sendMessage("§2/grade PLAYER 'pseudo' §6:Connaitre le grade et la perme level d'un joueur !");
			sender.sendMessage("§2/grade PLAYER 'pseudo' setrank/setkarma 'nom du grade' true/false (Fille ou non) §6: Modifier le grade d'un jouer !");
			sender.sendMessage("§2/grade PLAYER 'pseudo' delrank/delkarma §6: retirer le grade d'un jouer !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' setprefix 'prefix' §6:Redéfinire le prefix d'un grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' setdefaultlevel 'level' §6:Redéfinire la perm level d'un grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' add 'leveldefault 'prefix' §6:Crée un nouveau grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' remove §6:Remove le grade !");
			sender.sendMessage("§6========================================");
		} else {
			sender.sendMessage("§6============== §2Grade  :  Help §6==============");
			sender.sendMessage("§2/grade PLAYER 'pseudo' §6:Connaitre le grade et la perme level d'un joueur !");
			sender.sendMessage("§2/grade PLAYER 'pseudo' setrank/setkarma 'nom du grade' true/false (Fille ou non) §6: Modifier le grade d'un jouer !");
			sender.sendMessage("§2/grade PLAYER 'pseudo' delrank/delkarma §6: retirer le grade d'un jouer !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' setprefix 'prefix' §6:Redéfinire le prefix d'un grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' setdefaultlevel 'level' §6:Redéfinire la perm level d'un grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' add 'leveldefault 'prefix' §6:Crée un nouveau grade !");
			sender.sendMessage("§2/grade GROUPE/KARMA 'nom groupe' remove §6:Remove le grade !");
			sender.sendMessage("§6============================================");
		}
	}
}
