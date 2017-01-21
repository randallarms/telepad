// ========================================================================
// |TELEPAD v1.1
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/voicebox
// ========================================================================

package com.kraken.telepad;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin {

	public static TelePad plugin;
  	
	@Override
    public void onEnable() {
    	
    	getLogger().info("TelePad has been enabled.");
		PluginManager pm = getServer().getPluginManager();
		TPListener listener = new TPListener();
		pm.registerEvents(listener, this);
		
    }
    
    @Override
    public void onDisable() {
    	
        getLogger().info("TelePad has been disabled.");
        saveConfig();
        
    }
    
  //TelePad commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
		Player player = (Player) sender;
		
      //Command: telepad
        if ( cmd.getName().equalsIgnoreCase("telepad") && sender instanceof Player ) {
            player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin (v1.1)");
            return true;
        }
        
      //OP commands
        if ( sender instanceof Player && player.isOp() ) {
        
        	Teleprocessing tp = new Teleprocessing(this);
        	
        	switch (command) {
        	
			  //Command: jump
        	    case "jump":
        			  
        			tp.jump(player);
			        return true;
			    
			  //Command: tele
        		case "tele":
        			
			    	tp.teleport(player, args);
			    	return true; 
			    
			  //Command: teledel
        		case "teledel":
			        
			    	tp.teleDelete(player, args);
			        return true;
			    
			  //Command: telelist
        		case "telelist":
			    	
			    	tp.teleList(player);
			    	return true;
			    
			  //Command: teleset
        		case "teleset":
			        
			    	tp.teleSet(player, args);
			        return true;
			        
			    default:
			    	  
			        player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
			        return true;
			    
	        }
        
        }
        
        player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
        return true;
        
    }
    
}
