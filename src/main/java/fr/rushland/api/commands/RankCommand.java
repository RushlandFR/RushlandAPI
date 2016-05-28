package fr.rushland.api.commands;

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

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int permlvl = PlayerInfo.get(player).getMaxPermLevel();
            if (permlvl < 40) {
                sender.sendMessage("§cVous n'avez pas la permission.");
                return true;
            }
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("§aListe des rangs:");
                sender.sendMessage("§7guide, builder, administrateur, moderateur, respcomm, developpeur, gerant, respbuild, graphiste, youtuber, streamer, developpeurweb");
                sender.sendMessage("§aListe des rangs boutique:");
                sender.sendMessage("§7or, diamant, emeraude");
                return true;
            }
            String pseudo = args[0];
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
                sender.sendMessage("§a" + player.getName() + "§f est §a" + grade + "§f avec un perm-level de §a " + permlevel);
                if (!pInfo.getKarmaRank().equals("player")) {
                    sender.sendMessage("§a" + player.getName() + "§f possède le grade boutique §a" + pInfo.getKarmaRank());
                }
                return true;
            } catch (Exception e) {
                sender.sendMessage("§cLe joueur sélectionné n'éxiste pas !");
            }
        } else if (args.length >= 3) {
            try {
                String pseudo = args[0];
                Player player = (Player) Bukkit.getOfflinePlayer(pseudo);
                String option = args[1];
                if (option.equalsIgnoreCase("set")) {
                    String rankname = args[2].toLowerCase();
                    if (rankname.equalsIgnoreCase("player")) {
                        sender.sendMessage("§aSouhaitez vous dérank un joueur? Utilisez '/rank <pseudo> remove' à la place !");
                        return true;
                    }
                    boolean isFemale = false;
                    if (args.length > 3) {
                        if (args[3].toLowerCase().startsWith("f")) {
                            isFemale = true;
                        }
                    }
                    if (this.api.getDataManager().getRankSystemDB().getRankList().containsKey(rankname)) {
                        if (sender instanceof Player) {
                            Player staff = (Player) sender;
                            PlayerInfo staffInfo = PlayerInfo.get(staff);
                            boolean god = false;
                            if (staffInfo.getMaxPermLevel() >= 100) {
                                god = true;
                            }
                            if (!hasPermission(staffInfo.getRank(), rankname, god)) {
                                staff.sendMessage("§cVous n'avez pas la permission de donner ce grade.");
                                return true;
                            }
                        }
                        if (sender instanceof Player) {
                            if (getPcordRank(rankname) != null) {
                                this.api.runBungeeConsoleCommand((Player) sender, "pcord user " + player.getName() + " group set " + getPcordRank(rankname));
                            }
                        } else {
                            Player randomPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                            if (randomPlayer == null) {
                                sender.sendMessage("§cImpossible de donner un grade via la console, aucun joueur n'est connecté pour faire la liaison Bungee.");
                                return true;
                            }
                            if (getPcordRank(rankname) != null) {
                                this.api.runBungeeConsoleCommand(randomPlayer, "pcord user " + player.getName() + " group set " + getPcordRank(rankname));
                            }
                        }
                        this.api.getDataManager().getPlayerDB().setRankPlayer(player, rankname, isFemale);
                        sender.sendMessage("§aVous avez donné le grade §2" + rankname + " §aà§2 " + player.getName());
                        if (player.isOnline()) {
                            if (isFemale) {
                                player.sendMessage("§aVous avez reçue le grade §2" + rankname + " §a!");
                            } else {
                                player.sendMessage("§aVous avez reçu le grade §2" + rankname + " §a!");
                            }
                            PlayerInfo pInfo = PlayerInfo.get(player);
                            pInfo.rank = rankname;
                            pInfo.rankPermLevel = api.getDataManager().getRankSystemDB().getRankList().get(rankname);
                            pInfo.isFemale = isFemale;
                        }
                    } else {
                        sender.sendMessage("§cLe grade '" + rankname + "' est introuvable. Utilisez '/rank list' pour obtenir la liste des grades.");
                    }
                    return true;
                } else if (option.equalsIgnoreCase("setpaid")) {
                    String rankname = args[2];
                    if (this.api.getDataManager().getKarmaDB().getKarmaList().containsKey(rankname)) {
                        if (sender instanceof Player) {
                            Player staff = (Player) sender;
                            PlayerInfo staffInfo = PlayerInfo.get(staff);
                            boolean god = false;
                            if (staffInfo.getMaxPermLevel() >= 100) {
                                god = true;
                            }
                            if (!hasPermission(staffInfo.getRank(), rankname, god)) {
                                staff.sendMessage("§cVous n'avez pas la permission de donner ce grade.");
                                return true;
                            }
                        }
                        this.api.getDataManager().getPlayerDB().setKarmaPlayer(player, rankname);
                        sender.sendMessage("§aVous avez ajouté le grade boutique §2" + rankname + "§a à §2" + player.getName());
                        if (player.isOnline()) {
                            PlayerInfo pInfo = PlayerInfo.get(player);
                            if (pInfo.isFemale) {
                                player.sendMessage("§aVous avez reçue le grade §2" + rankname + " §a!");
                            } else {
                                player.sendMessage("§aVous avez reçu le grade §2" + rankname + " §a!");
                            }
                            pInfo.karma = rankname;
                        }
                    } else {
                        sender.sendMessage("§cLe karma choisit n'existe pas !");
                    }
                    return true;
                } else if (option.equalsIgnoreCase("remove")) {
                    if (sender instanceof Player) {
                        Player staff = (Player) sender;
                        PlayerInfo staffInfo = PlayerInfo.get(staff);
                        boolean god = false;
                        if (staffInfo.getMaxPermLevel() >= 100) {
                            god = true;
                        }
                        if (!hasPermission(staffInfo.getRank(), PlayerInfo.get(player).getRank(), god)) {
                            staff.sendMessage("§cVous n'avez pas la permission de retirer ce grade.");
                            return true;
                        }
                    }
                    if (sender instanceof Player) {
                        this.api.runBungeeConsoleCommand((Player) sender, "pcord user " + player.getName() + " delete");
                    } else {
                        Player randomPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                        if (randomPlayer == null) {
                            sender.sendMessage("§cImpossible de retirer un grade via la console, aucun joueur n'est connecté pour faire la liaison Bungee.");
                            return true;
                        }
                        this.api.runBungeeConsoleCommand(randomPlayer, "pcord user " + player.getName() + " delete");
                    }
                    this.api.getDataManager().getPlayerDB().setRankPlayer(player, "player", false);
                    if (player.isOnline()) {
                        player.kickPlayer("§cVotre grade vous à été enlevé, vous pouvez désormais vous reconnecter.");
                    }
                    return true;
                } else if (option.equalsIgnoreCase("removepaid")) {
                    if (PlayerInfo.get(player).getKarmaRank().equalsIgnoreCase("player")) {
                        sender.sendMessage("§cCe joueur ne possède pas de grade boutique.");
                        return true;
                    }
                    if (sender instanceof Player) {
                        Player staff = (Player) sender;
                        PlayerInfo staffInfo = PlayerInfo.get(staff);
                        boolean god = false;
                        if (staffInfo.getMaxPermLevel() >= 100) {
                            god = true;
                        }
                        if (!hasPermission(staffInfo.getRank(), PlayerInfo.get(player).getKarmaRank(), god)) {
                            staff.sendMessage("§cVous n'avez pas la permission de retirer ce grade boutique.");
                            return true;
                        }
                    }
                    this.api.getDataManager().getPlayerDB().deleteKarmaPlayer(player);
                    if (player.isOnline()) {
                        player.kickPlayer("§cVotre grade boutique vous à été enlevé, vous pouvez désormais vous reconnecter.");
                    }
                    return true;
                } else {
                    sendHelp(sender);
                    return true;
                }
            } catch (Exception e) {
                sender.sendMessage("§cLe joueur sélectionné n'éxiste pas !");
            }
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§a/rank <pseudo> set <grade> [m/f] §7Définir le grade d'un joueur. m/f signifie le sexe");
        sender.sendMessage("§a/rank <pseudo> setpaid <grade> §7Définir le grade boutique d'un joueur.");
        sender.sendMessage("§a/rank <pseudo> remove §7Retirer le grade d'un joueur.");
        sender.sendMessage("§a/rank <pseudo> removepaid §7Retirer le grade boutique d'un joueur.");
        sender.sendMessage("§a/rank list §7Affiche la liste des grades pour le format commande.");
    }

    private boolean hasPermission(String rank, String targetRank, boolean god) {
        if (god || rank.equalsIgnoreCase("administrateur")) {
            return true;
        } else if (rank.equalsIgnoreCase("respbuild")) {
            if (targetRank.equalsIgnoreCase("builder")) {
                return true;
            }
        } else if (rank.equalsIgnoreCase("gerant")) {
            if (targetRank.equalsIgnoreCase("guide") || targetRank.equalsIgnoreCase("moderateur")) {
                return true;
            }
        } else if (rank.equalsIgnoreCase("respcomm")) {
            if (targetRank.equalsIgnoreCase("youtuber") || targetRank.equalsIgnoreCase("streamer") || targetRank.equalsIgnoreCase("or") || targetRank.equalsIgnoreCase("diamant") || targetRank.equalsIgnoreCase("emeraude")) {
                return true;
            }
        }
        return false;
    }

    private String getPcordRank(String rank) {
        if (rank.equalsIgnoreCase("builder") || rank.equalsIgnoreCase("graphiste")) {
            return "staff";
        } else if (rank.equalsIgnoreCase("guide")) {
            return "guide";
        } else if (rank.equalsIgnoreCase("moderateur")) {
            return "moderateur";
        } else if (rank.equalsIgnoreCase("gerant") || rank.equalsIgnoreCase("respbuild") || rank.equalsIgnoreCase("respcomm")) {
            return "gerant";
        } else if (rank.equalsIgnoreCase("administrateur") || rank.equalsIgnoreCase("developpeur") || rank.equalsIgnoreCase("developpeurweb")) {
            return "admin";
        } else {
            return null;
        }
    }
}
