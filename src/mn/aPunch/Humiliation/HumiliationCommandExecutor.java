package mn.aPunch.Humiliation;

import java.util.HashMap;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HumiliationCommandExecutor implements CommandExecutor {
	public Humiliation plugin;
	public static HashMap<String, String> leashMap = new HashMap<String, String>();
	public static Player player;

	public HumiliationCommandExecutor(Humiliation instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Server server = plugin.getServer();
		String commandName = command.getName();
		if (sender instanceof Player) {
			player = (Player) sender;
			if (commandName.equalsIgnoreCase("slap")) {
				if (HumiliationPermissions.canSlap(player) || (player.isOp())) {
					if (args.length >= 1) {
						List<Player> players = server.matchPlayer(args[0]);
						if (players.size() >= 1) {
							Player receiver = players.get(0);
							int health = receiver.getHealth();
							if (receiver.getHealth() >= 6) {
								receiver.setHealth(health - 6);
								player.sendMessage(ChatColor.YELLOW
										+ "You have slapped " + ChatColor.RED
										+ receiver.getName() + ChatColor.YELLOW
										+ ".");
								receiver.sendMessage(ChatColor.YELLOW
										+ "You have been slapped by "
										+ ChatColor.RED + player.getName()
										+ ChatColor.YELLOW + ".");
								server.broadcastMessage(ChatColor.RED
										+ player.getName() + ChatColor.YELLOW
										+ " has slapped " + ChatColor.RED
										+ receiver.getName() + ChatColor.YELLOW
										+ ".");
								return true;
							} else {
								player.sendMessage(ChatColor.RED
										+ "[Humiliation] You cannot slap a player when they are very low on health!");
							}
						} else {
							player.sendMessage("You must specify a player's name!");
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player's name!");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
					return true;
				}
			} else if (commandName.equalsIgnoreCase("throw")) {
				if (HumiliationPermissions.canThrow(player) || (player.isOp())) {
					if (args.length >= 1) {
						List<Player> players = server.matchPlayer(args[0]);
						Player receiver = players.get(0);
						double x = receiver.getLocation().getBlockX();
						double y = receiver.getLocation().getBlockY();
						double z = receiver.getLocation().getBlockZ();
						double newY = y + Humiliation.throwHeight;
						Location loc = new Location(receiver.getWorld(), x,
								newY, z);
						if (players.size() >= 1) {
							receiver.teleport(loc);
							player.sendMessage(ChatColor.YELLOW
									+ "You have thrown " + ChatColor.RED
									+ receiver.getName() + ChatColor.YELLOW
									+ " high into the air!");
							receiver.sendMessage(ChatColor.YELLOW
									+ "You were thrown high into the air by "
									+ ChatColor.RED + player.getName()
									+ ChatColor.YELLOW + ".");

						} else {
							player.sendMessage(ChatColor.RED
									+ "[Humiliation] You must specify a player.");
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
					return true;
				}
			} else if (commandName.equalsIgnoreCase("hh")) {
				if (HumiliationPermissions.canHelp(player) || (player.isOp())) {
					Humiliation.sendHelp(player);
					return true;
				}
			} else if (commandName.equalsIgnoreCase("humiliate")) {
				if (HumiliationPermissions.canHumiliate(player)
						|| (player.isOp())) {
					if (args.length >= 2) {
						List<Player> players = server.matchPlayer(args[0]);
						String nickname = args[1];
						Player receiver = players.get(0);
						if (players.size() >= 1 && (receiver != null)
								&& (nickname != null)) {
							receiver.setDisplayName(ChatColor
									.valueOf(Humiliation.config.getString(
											"nickname-color", "WHITE"))
									+ nickname + ChatColor.WHITE);
							player.sendMessage(ChatColor.YELLOW
									+ "You have given " + ChatColor.RED
									+ receiver.getName() + ChatColor.YELLOW
									+ " the nickname: " + ChatColor.RED
									+ nickname + ChatColor.YELLOW + ".");
							receiver.sendMessage(ChatColor.YELLOW
									+ "You were renamed to " + ChatColor.RED
									+ nickname + ChatColor.YELLOW + " by "
									+ ChatColor.RED + player.getName()
									+ ChatColor.YELLOW + ".");
							server.broadcastMessage(ChatColor.RED
									+ player.getName() + ChatColor.YELLOW
									+ " has renamed " + ChatColor.RED
									+ receiver.getName() + ChatColor.YELLOW
									+ " to " + ChatColor.RED + nickname
									+ ChatColor.YELLOW + ".");
							return true;
						} else {
							player.sendMessage(ChatColor.RED
									+ "[Humiliation] You must specify a player and a new nickname.");
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player and a new nickname.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
					return true;
				}
			} else if (commandName.equalsIgnoreCase("leash")) {
				if ((HumiliationPermissions.canLeash(player))
						|| (player.isOp())) {
					if (args.length == 1) {
						List<Player> players = server.matchPlayer(args[0]);
						Player leashedPlayer = players.get(0);
						leashMap.put(leashedPlayer.getName(), player.getName());
						player.sendMessage(ChatColor.YELLOW
								+ "[Humiliation] You have leashed "
								+ ChatColor.RED + leashedPlayer.getName()
								+ ChatColor.YELLOW + ".");
						leashedPlayer.sendMessage(ChatColor.YELLOW
								+ "[Humiliation] You have been leashed by "
								+ ChatColor.RED + player.getName()
								+ ChatColor.YELLOW + ".");
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player to leash.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
				}
			} else if (commandName.equalsIgnoreCase("unleash")) {
				if ((HumiliationPermissions.canLeash(player))
						|| (player.isOp())) {
					if (args.length == 1) {
						List<Player> players = server.matchPlayer(args[0]);
						Player leashedPlayer = players.get(0);
						if (leashMap.containsKey(leashedPlayer.getName())) {
							leashMap.remove(leashedPlayer.getName());
							player.sendMessage(ChatColor.YELLOW
									+ "[Humiliation] You have unleashed "
									+ ChatColor.RED + leashedPlayer.getName()
									+ ChatColor.YELLOW + ".");
							leashedPlayer.sendMessage(ChatColor.YELLOW
									+ "[Humiliation] You have been unleashed by "
									+ ChatColor.RED + player.getName()
									+ ChatColor.YELLOW + ".");
						} else {
							player.sendMessage(ChatColor.RED
									+ "[Humiliation] That player isn't leashed by you.");
						}
					} else if (args.length == 0) {
						leashMap.remove(player.getName());
						player.sendMessage(ChatColor.YELLOW
								+ "[Humiliation] You are no longer leashing any players.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
				}
			}
		}
		return true;
	}
}
