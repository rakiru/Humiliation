package mn.aPunch.Humiliation;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HumiliationBlockListener extends BlockListener {
	private static final Humiliation Humiliation = new Humiliation();
	public Humiliation plugin;

	public HumiliationBlockListener(Humiliation instance) {
		plugin = instance;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		Logger log = Logger.getLogger("Minecraft");
		Player player = event.getPlayer();
		Server server = player.getServer();
		Block block = event.getBlock();
		if (HumiliationPermissions.isAdmin(player)) {
			log.info("[Humiliation] " + player.getName() + " has placed "
					+ block.getType()
					+ ", but has permission to do so! Just sayin'.");
		} else if (block.getType() == Material.TNT) {
			if(Humiliation.config.getBoolean("automatic-kick", true)){
				player.kickPlayer(null);
				block.setType(Material.AIR);
				server.broadcastMessage(ChatColor.RED + player.getName()
						+ ChatColor.YELLOW + " has been kicked for using "
						+ ChatColor.RED + block + ChatColor.YELLOW + ".");
			}
		}
	}
}
