package mn.aPunch.Humiliation;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;

public class Humiliation extends JavaPlugin {
	private final HumiliationBlockListener blockListener = new HumiliationBlockListener(
			this);
	private final HumiliationPlayerListener playerListener = new HumiliationPlayerListener(
			this);
	public static PermissionHandler Permissions;
	Logger log = Logger.getLogger("Minecraft");
	Configuration config;

	public static String nickColor = "WHITE";
	public static boolean autoKick = true;
	public static int throwHeight = 30;

	private void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "===== Humiliation Help =====");
		player.sendMessage(ChatColor.BLUE + "/hh - displays this menu");
		player.sendMessage(ChatColor.BLUE
				+ "/slap [player] [noun] - deals 3 hearts damage");
		player.sendMessage(ChatColor.BLUE
				+ "/humiliate [player] [nickname] - changes the display name of a player");
		player.sendMessage(ChatColor.BLUE
				+ "/throw [player] - throw a player up in the air...and watch them fall to their death!");
		player.sendMessage(ChatColor.GOLD + "===== v0.3.1 by aPunch =====");
	}

	private void loadConfig() {
		config.load();
		nickColor = config.getString("nickname-color", nickColor);
		autoKick = config.getBoolean("automatic-kick", autoKick);
		throwHeight = config.getInt("throw-height", throwHeight);
	}

	private void defaultConfig() {
		config.setProperty("nickname-color", nickColor);
		config.setProperty("automatic-kick", autoKick);
		config.setProperty("throw-height", throwHeight);
		config.save();
	}

	public void onEnable() {
		HumiliationPermissions.initialize(getServer());
		config = getConfiguration();
		if (!new File(getDataFolder(), "config.yml").exists()) {
			defaultConfig();
		}
		loadConfig();
		PluginDescriptionFile pdfFile = getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Event.Priority.Normal, this);
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is enabled! ");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Server server = getServer();
		String commandName = command.getName();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (commandName.equals("slap")) {
				if (HumiliationPermissions.canSlap(player) || (player.isOp())) {
					if (args.length >= 2) {
						List<Player> players = getServer().matchPlayer(args[0]);
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
								+ "[Humiliation] You must specify a player and a noun!");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use this command.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "[Humiliation] Incorrect command. Type /hh to view possible commands.");
			}
			/*
			 * if (commandName.equals("leash")) { if
			 * (HumiliationPermissions.canLeash(player) || (player.isOp())) { if
			 * (args.length > 0) { List<Player> players =
			 * getServer().matchPlayer(args[0]); if (players.size() > 0) {
			 * HumiliationPlayerListener playerListener = new
			 * HumiliationPlayerListener( this); Player p =
			 * playerListener.getLeashingPlayer(); Player lp =
			 * playerListener.getLeashedPlayer(); Server s = p.getServer();
			 * playerListener.onPlayerMove(null); p.sendMessage(ChatColor.YELLOW
			 * + "You have leashed " + ChatColor.RED + lp.getName() +
			 * ChatColor.YELLOW + "."); lp.sendMessage(ChatColor.YELLOW +
			 * "You have been leashed by " + ChatColor.RED + p.getName() +
			 * ChatColor.YELLOW + "."); s.broadcastMessage(ChatColor.RED +
			 * p.getName() + ChatColor.YELLOW + " has leashed " + ChatColor.RED
			 * + lp.getName()); } } } }
			 */
			if (commandName.equals("hh")) {
				if (HumiliationPermissions.canHelp(player) || (player.isOp())) {
					sendHelp(player);
					return true;
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use that command.");
				}
			}
			if (commandName.equals("humiliate")) {
				if (HumiliationPermissions.canHumiliate(player)
						|| (player.isOp())) {
					if (args.length >= 2) {
						List<Player> players = getServer().matchPlayer(args[0]);
						String nickname = args[1];
						Player receiver = players.get(0);
						if (players.size() >= 1 && (receiver != null)
								&& (nickname != null)) {
							receiver.setDisplayName(ChatColor.valueOf(config
									.getString("nickname-color", "WHITE"))
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

						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player and a new nickname.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[Humiliation] You do not have permission to use this command.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "[Humiliation] Incorrect command. Type /hh to view possible commands.");
			}
			if (commandName.equalsIgnoreCase("throw")) {
				if (HumiliationPermissions.canThrow(player) || (player.isOp())) {
					if (args.length >= 1) {
						List<Player> players = getServer().matchPlayer(args[0]);
						Player receiver = players.get(0);
						double x = receiver.getLocation().getX();
						double y = receiver.getLocation().getY();
						double z = receiver.getLocation().getZ();
						double newY = y + throwHeight;
						Location loc = receiver.getLocation();
						if (players.size() >= 1) {
							loc.setX(x);
							loc.setY(newY);
							loc.setZ(z);
						} else {
							player.sendMessage(ChatColor.RED
									+ "[Humiliation] You must specify a player.");
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[Humiliation] You must specify a player.");
					}
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "[Humiliation] Incorrect command. Type /hh to view possible commands.");
			}
		}
		return true;
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is disabled! ");
	}
}
