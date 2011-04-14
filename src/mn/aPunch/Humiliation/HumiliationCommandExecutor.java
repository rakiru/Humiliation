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
	public static HumiliationPermissions permissions;
	public static HashMap<String, String> leashMap = new HashMap<String, String>();
	public static Player player;
	public static String noPermissionsMessage = ChatColor.RED
			+ "[Challenges] You do not have permission to use that command.";

	public HumiliationCommandExecutor(Humiliation instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Server server = plugin.getServer();
		String commandName = command.getName();
		permissions = new HumiliationPermissions(plugin);
		if (sender instanceof Player) {
			player = (Player) sender;
			if (commandName.equalsIgnoreCase("slap")) {
				if (permissions.canSlap(player) || (player.isOp())) {
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
					player.sendMessage(noPermissionsMessage);
					return true;
				}
			} else if (commandName.equalsIgnoreCase("throw")) {
				if (permissions.canThrow(player) || (player.isOp())) {
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
					player.sendMessage(noPermissionsMessage);
					return true;
				}
			} else if (commandName.equalsIgnoreCase("hh")) {
				if (permissions.canHelp(player) || (player.isOp())) {
					sendHelp(player);
					return true;
				}
			} else if (commandName.equalsIgnoreCase("humiliate")) {
				if (permissions.canHumiliate(player) || (player.isOp())) {
					if (args.length >= 2) {
						List<Player> players = server.matchPlayer(args[0]);
						String nickname = args[1];
						Player receiver = players.get(0);
						if (players.size() >= 1 && (receiver != null)
								&& (nickname != null)) {
							receiver.setDisplayName(ChatColor
									.valueOf(plugin.config.getString(
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
					player.sendMessage(noPermissionsMessage);
					return true;
				}
			} else if (commandName.equalsIgnoreCase("leash")) {
				if ((permissions.canLeash(player)) || (player.isOp())) {
					if (args.length == 1) {
						List<Player> players = server.matchPlayer(args[0]);
						Player leashedPlayer = players.get(0);
						String leashedPlayerName = leashedPlayer.getName();
						LeashTimer leashTimer = new LeashTimer(plugin,
								leashedPlayerName, player.getName());
						server.getScheduler().scheduleSyncRepeatingTask(plugin,
								leashTimer, 100, 100);
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
					player.sendMessage(noPermissionsMessage);
				}
			} else if (commandName.equalsIgnoreCase("unleash")) {
				if ((permissions.canLeash(player)) || (player.isOp())) {
					if (args.length == 1) {
						List<Player> players = server.matchPlayer(args[0]);
						Player leashedPlayer = players.get(0);
						if (leashMap.containsKey(leashedPlayer.getName())) {
							leashMap.remove(leashedPlayer.getName());
							player.sendMessage(ChatColor.YELLOW
									+ "[Humiliation] You have unleashed "
									+ ChatColor.RED + leashedPlayer.getName()
									+ ChatColor.YELLOW + ".");
							leashedPlayer
									.sendMessage(ChatColor.YELLOW
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
					player.sendMessage(noPermissionsMessage);
				}
			}
		}
		return true;
	}

	public static void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD
				+ "========== Humiliation Help ==========");
		player.sendMessage(ChatColor.BLUE + "/hh - displays this menu");
		player.sendMessage(ChatColor.BLUE
				+ "/slap [player] - deals 3 hearts damage");
		player.sendMessage(ChatColor.BLUE
				+ "/humiliate [player] [nickname] - changes the display name of a player");
		player.sendMessage(ChatColor.BLUE
				+ "/throw [player] - throw a player up in the air...and watch them fall to their death!");
		player.sendMessage(ChatColor.BLUE
				+ "/leash [player] - drag a player around with you");
		player.sendMessage(ChatColor.BLUE
				+ "/unleash [player] - unleash a player");
		player.sendMessage(ChatColor.GOLD
				+ "========== v0.5-alpha1 by aPunch ==========");
	}
}