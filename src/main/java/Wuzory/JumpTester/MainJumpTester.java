package Wuzory.JumpTester;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MainJumpTester extends JavaPlugin implements Listener{
	
	HashMap<String, Location> allTesters = new HashMap<String,Location>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	String[] commands = {"settestloc"
						,"stoptesting"
						,"testitem"};
	
	String[] comment = {"starts testing on location that player stands",
						"stops testing",
						"gives item that is used to teleport back to tested location"};

	Material backItem = Material.GLOWSTONE_DUST;
	
	void SendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"List of commands in JumpTester");
		for(int i=0;i<commands.length && i<comment.length;i++) {
			sender.sendMessage(ChatColor.BLUE+""+(i+1)+" "+ChatColor.RED+commands[i]+" - "+ChatColor.AQUA+comment[i]+".");
		}
	}
	
	public void givePlayerTeleportItem(Player player) {
		player.getInventory().setItemInMainHand(new ItemStack(backItem));
		player.sendMessage(ChatColor.GREEN+"Teleport item given.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("This plugin can be used only by player!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase(commands[0])) {
			
			if(!(allTesters.containsKey(player.getName()))) {
				givePlayerTeleportItem(player);
			}
			
			
			allTesters.put(player.getName(),player.getLocation());
			sender.sendMessage(ChatColor.GREEN+"Test location set.");
		}
		else if(cmd.getName().equalsIgnoreCase(commands[1])) {
			allTesters.remove(player.getName());
			sender.sendMessage(ChatColor.GREEN+"Your test ended!");
		}
		else if(cmd.getName().equalsIgnoreCase(commands[2])) {
			givePlayerTeleportItem(player);
		}
		else {
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnPlayerUseItem(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(allTesters.containsKey(player.getName()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getInventory().getItemInHand().getType().equals(backItem)) {
				player.teleport(allTesters.get(player.getName()));	
		}
	}
}
