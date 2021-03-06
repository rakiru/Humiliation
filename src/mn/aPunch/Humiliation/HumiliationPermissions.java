package mn.aPunch.Humiliation;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class HumiliationPermissions {
	public Humiliation plugin;
	private static Permissions permissionsPlugin;
	private static boolean permissionsEnabled = false;
	
	public HumiliationPermissions(Humiliation instance){
		plugin = instance;
	}

	public static void initialize(Server server) {
		Plugin test = server.getPluginManager().getPlugin("Permissions");
		if (test != null) {
			Logger log = Logger.getLogger("Minecraft");
			permissionsPlugin = ((Permissions) test);
			permissionsEnabled = true;
			log.log(Level.INFO, "[Humiliation] Permissions enabled.");
		} else {
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.INFO,
					"[Humiliation] Permissions not found. Defaulting to Op.");
		}
	}

	@SuppressWarnings("static-access")
	private static boolean permission(Player player, String string) {
		return permissionsPlugin.Security.permission(player, string);
	}

	public boolean isAdmin(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.admin");
		} else {
			return player.isOp();
		}
	}

	public boolean canHelp(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.help");
		} else {
			return true;
		}
	}

	public boolean canHumiliate(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.humiliate");
		} else {
			return true;
		}
	}

	public boolean canSlap(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.slap");
		} else {
			return true;
		}
	}

	public boolean canThrow(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.throw");
		} else {
			return true;
		}
	}

	public boolean canLeash(Player player) {
		if (permissionsEnabled) {
			return permission(player, "humiliation.leash");
		} else {
			return true;
		}
	}
}
