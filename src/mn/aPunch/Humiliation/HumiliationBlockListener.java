package mn.aPunch.Humiliation;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HumiliationBlockListener extends BlockListener {
	public Humiliation plugin;
	public HumiliationPermissions permissions;

	public HumiliationBlockListener(Humiliation instance) {
		plugin = instance;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Server server = player.getServer();
		Block block = event.getBlock();
		permissions = new HumiliationPermissions(plugin);
		if (block.getType() == Material.TNT) {
			if(!permissions.isAdmin(player)){
				if (plugin.config.getBoolean(
						"automatic-kick", true)) {
					player.kickPlayer(null);
					block.setType(Material.AIR);
					server.broadcastMessage(ChatColor.RED + player.getName()
							+ ChatColor.YELLOW + " has been kicked for using "
							+ ChatColor.RED + block + ChatColor.YELLOW + ".");
				}
			}		
		}
	}
}
