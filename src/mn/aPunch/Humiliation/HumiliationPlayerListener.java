package mn.aPunch.Humiliation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


public class HumiliationPlayerListener extends PlayerListener {
	public Humiliation plugin;

	public HumiliationPlayerListener(Humiliation instance) {
		plugin = instance;
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		String leashedName = event.getPlayer().getName();
		Player leashedPlayer = plugin.getServer().getPlayer(leashedName);
		World world = event.getPlayer().getWorld();
		String worldName = world.getName();
		if (HumiliationCommandExecutor.leashMap.containsKey(leashedName)) {
			String masterName = HumiliationCommandExecutor.leashMap
					.get(leashedName);
			World leashedWorld = leashedPlayer.getWorld();
			String leashedWorldName = leashedWorld.getName();
			Vector leashedVec = plugin.getServer().getPlayer(leashedName)
					.getLocation().toVector();
			Vector masterVec = plugin.getServer().getPlayer(masterName)
					.getLocation().toVector();
			Vector masterToLeashed = leashedVec.subtract(masterVec);
			if (worldName.equals(leashedWorldName)) {
				if (masterToLeashed.lengthSquared() > Math.pow(
						Humiliation.leashRadius, 2)) {
					Vector masterToTP = masterToLeashed.normalize().multiply(
							Humiliation.leashRadius - 3);
					Vector teleportVector = masterVec.add(masterToTP);
					Location teleportLocation = teleportVector.toLocation(event
							.getPlayer().getWorld());
					findSafeTP(teleportLocation, leashedPlayer);
				}
			}
		}
	}

	private void findSafeTP(Location location, Player player) {
		int i = 0;
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		World world = location.getWorld();
		boolean testDown = true;
		boolean testUp = true;
		while (testDown || testUp) {
			System.out.println("y = " + y + "; i = " + i);
			y = y + i;
			if (y >= 128) {
				testUp = false;
				if (i > 0) {
					i = -i;
				}
				i--;
				continue;
			} else if (y < 1) {
				testDown = false;
				if (i < 0) {
					i = -1;
				}
				i++;
				continue;
			}

			if (safeToTeleport(x, y, z, world)) {
				location.setY(y);
				player.teleport(location);
				break;
			} else {
				if(testDown && testUp){
					i = -(Math.abs(i) + 1);
				} else if(testDown){
					y--;
					i = 0;
				} else {
					y++;
					i = 0;
				}
			}
		}
	}

	private boolean safeToTeleport(double x, double y, double z, World world) {
		Location tpA = new Location(world, x, y - 1, z);
		Location tpB = new Location(world, x, y, z);
		Location tpC = new Location(world, x, y + 1, z);
		Block blockA = tpA.getBlock();
		Block blockB = tpB.getBlock();
		Block blockC = tpC.getBlock();
		if (blockA.getType().equals(Material.AIR)) {
			return false;
		}
		if (!blockB.getType().equals(Material.AIR)) {
			return false;
		}
		if (!blockC.getType().equals(Material.AIR)) {
			return false;
		}
		return true;
	}
}
