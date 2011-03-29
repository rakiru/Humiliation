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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Humiliation extends JavaPlugin{
	private final HumiliationBlockListener blockListener = new HumiliationBlockListener(this);
	public static PermissionHandler Permissions;
	Logger log = Logger.getLogger("Minecraft");
	Configuration config;
	
	//config variables
	public String nickColor = "WHITE";
	
	public void setupPermissions(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
		if(Humiliation.Permissions == null){
			if(test != null){
				Humiliation.Permissions = ((Permissions)test).getHandler();
				log.info("[Humiliation] Permissions system detected!");
			}else{
				log.info("[Humiliation] Permissions system not detected. Defaulting to Op.");
			}
		}
	}
	private void loadConfig(){
		config.load();
		nickColor = config.getString("nickname-color", nickColor);
	}
	private void defaultConfig(){
		config.setProperty("nickname-color", nickColor);
		config.save();
	}
	public void onEnable(){
		config = getConfiguration();
		if(!(new File(getDataFolder(), "config.yml")).exists()){
			defaultConfig();
		}
		loadConfig();
		setupPermissions();
		PluginDescriptionFile pdfFile = getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACED, blockListener, Event.Priority.Normal, this);
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is enabled! ");
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		Server server = getServer();
		String commandName = command.getName();
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(Humiliation.Permissions.has(player, "humiliation.slap") || (player.isOp())){
				try{
				if(commandName.equalsIgnoreCase("slap")){
					if(args.length > 0){
						List<Player> players = getServer().matchPlayer(args[0]);
						if(players.size() > 0){
							Player receiver = players.get(0);
							int health = receiver.getHealth();
							if(receiver.getHealth() >= 6){
								receiver.setHealth(health - 6);
								String reason = args[1];
								player.sendMessage(ChatColor.YELLOW + "You have slapped " + ChatColor.RED + receiver.getName() 
										+ ChatColor.YELLOW + " for being a(n) " + reason + ".");
								receiver.sendMessage(ChatColor.YELLOW + "You have been slapped by " + ChatColor.RED + player.getName()
										+ ChatColor.YELLOW + " for being a(n) " + reason + ".");
								server.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has slapped " + ChatColor.RED
										+ receiver.getName() + ChatColor.YELLOW + " for being a(n) " + reason + ".");
								return true;
							}else{
								player.sendMessage(ChatColor.RED + "[Humiliation] You cannot slap a player when they are very low on health!");
							}
						}
					}
			}else{
				player.sendMessage(ChatColor.RED + "[Humiliation] You do not have permission to use this command.");
				return true;
			}
			}catch(ArrayIndexOutOfBoundsException e){
				player.sendMessage(ChatColor.RED + "Bad Command.");
			}
			if(Humiliation.Permissions.has(player, "humiliation.leash")){
				if(commandName.equalsIgnoreCase("leash")){
					if(args.length > 0){
						List<Player> players = getServer().matchPlayer(args[0]);
						if(players.size() > 0){
							Player leashedPlayer = players.get(0);
							leashedPlayer.teleportTo(player);
							leashedPlayer.sendMessage(ChatColor.YELLOW + "You have been leashed by " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ".");
							Location playerLocation = player.getLocation();
							Location leashedPlayerLocation = leashedPlayer.getLocation();
							if(playerLocation != leashedPlayerLocation){
								leashedPlayer.sendMessage(ChatColor.YELLOW + "You are leashed to " + ChatColor.RED + player.getName() 
										+ ChatColor.YELLOW + ". You cannot move freely until you are released.");
								leashedPlayer.teleportTo(player);
								return true;
								}
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Humiliation] You must specify a player to leash.");
						}
					}
				}else{
					player.sendMessage(ChatColor.RED + "[Humiliation] You do not have permission to use this command.");
					return true;
				}
			}
			if(Humiliation.Permissions.has(player, "humiliation.help") || (player.isOp())){
				if(commandName.equalsIgnoreCase("hh")){
					player.sendMessage(ChatColor.GOLD + "===== Humiliation Help =====");
					player.sendMessage(ChatColor.BLUE + "/hh - displays this menu");
					player.sendMessage(ChatColor.BLUE + "/slap [player] [noun] - deals 3 hearts damage");
					player.sendMessage(ChatColor.BLUE + "/humiliate [player] [nickname] - changes the display name of a player");
					player.sendMessage(ChatColor.GOLD + "===== v0.2.2 by aPunch =====");
					return true;
				}
			}else{
				player.sendMessage(ChatColor.RED + "[Humiliation] You do not have permission to use this command.");
				return true;
			}
			if(Humiliation.Permissions.has(player, "humiliation.humiliate") || (player.isOp())){
				if(commandName.equalsIgnoreCase("humiliate")){
					if(args.length > 0){
						List<Player> players = getServer().matchPlayer(args[0]);
						String nickname = args[1];
						if(players.size() > 0){
							Player receiver = players.get(0);
							receiver.setDisplayName(ChatColor.valueOf(config.getString("nickname-color", "WHITE")) + nickname + ChatColor.WHITE);
							player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.RED + receiver.getName() 
									+ ChatColor.YELLOW + " the nickname: " + ChatColor.RED + nickname + ChatColor.YELLOW + ".");
							receiver.sendMessage(ChatColor.YELLOW + "You were renamed to " + ChatColor.RED + nickname
									+ ChatColor.YELLOW + " by " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ".");
							server.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " has renamed " + ChatColor.RED
									+ receiver.getName() + ChatColor.YELLOW + " to " + ChatColor.RED + nickname + ChatColor.YELLOW + ".");
							return true;
						}
					}
				}
			}else{
				player.sendMessage(ChatColor.RED + "[Humiliation] You do not have permission to use this command.");
				return true;
			}
		}
		return true;
	}
	public void onDisable(){
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is disabled! ");
	}
}
