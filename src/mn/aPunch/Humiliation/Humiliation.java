package mn.aPunch.Humiliation;

import java.io.File;
import java.util.logging.Logger;


import org.bukkit.ChatColor;
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
	public static Configuration config;

	public static String nickColor = "WHITE";
	public static boolean autoKick = true;
	public static int throwHeight = 30;
	public static int leashRadius = 5;

	public static void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "========== Humiliation Help ==========");
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
				+ "/unleash (player) - unleash a specific player, or unleash all players at once");
		player.sendMessage(ChatColor.GOLD + "========== v0.5 by aPunch ==========");
	}

	private void loadConfig() {
		config.load();
		nickColor = config.getString("nickname-color", nickColor);
		autoKick = config.getBoolean("automatic-kick", autoKick);
		throwHeight = config.getInt("throw-height", throwHeight);
		leashRadius = config.getInt("leash-radius", leashRadius);
	}

	private void defaultConfig() {
		config.setProperty("nickname-color", nickColor);
		config.setProperty("automatic-kick", autoKick);
		config.setProperty("throw-height", throwHeight);
		config.setProperty("leash-radius", leashRadius);
		config.save();
	}

	public void onEnable() {
		HumiliationCommandExecutor commandExecutor = new HumiliationCommandExecutor(
				this);
		getCommand("hh").setExecutor(commandExecutor);
		getCommand("slap").setExecutor(commandExecutor);
		getCommand("throw").setExecutor(commandExecutor);
		getCommand("humiliate").setExecutor(commandExecutor);
		getCommand("leash").setExecutor(commandExecutor);
		getCommand("unleash").setExecutor(commandExecutor);
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

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is disabled! ");
	}
}
