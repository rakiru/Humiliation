package mn.aPunch.Humiliation;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
//import org.bukkit.event.player.PlayerMoveEvent;
//import org.bukkit.util.Vector;

public class HumiliationPlayerListener extends PlayerListener {
	public Humiliation plugin;
	private Player player;
	private Player leashedPlayer;

	public HumiliationPlayerListener(Humiliation instance) {
		plugin = instance;
	}

	public Player getLeashedPlayer() {
		return this.leashedPlayer;
	}

	public Player getLeashingPlayer() {
		return this.player;
	}

	/*
	 * public void onPlayerMove(PlayerMoveEvent event) { Player player =
	 * event.getPlayer(); Player leashedPlayer = event.getPlayer(); Vector
	 * playerVector = player.getLocation().toVector(); Vector
	 * leashedPlayerVector = leashedPlayer.getLocation().toVector();
	 * leashedPlayer.setVelocity(leashedPlayerVector.subtract(playerVector)); }
	 */
}
