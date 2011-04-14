package mn.aPunch.Humiliation;

import java.io.File;
import java.util.logging.Logger;

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
	public Configuration config;

	public static String nickColor = "WHITE";
	public static boolean autoKick = true;
	public static int throwHeight = 30;
	public static int leashRadius = 5;
	
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
