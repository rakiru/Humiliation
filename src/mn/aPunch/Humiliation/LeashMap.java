package mn.aPunch.Humiliation;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class LeashMap {
	protected HashMap<Player, List<Player>> players = new HashMap<Player, List<Player>>();
	
	public boolean isLeashing(Player player){
		return true;
	}
	public boolean isLeashed(Player player){
		return true;
	}
}
