// ========================================================================
// |TELEPAD v1.2
// | by Kraken | https://www.spigotmc.org/resources/telepad.34953/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/telepad
// ========================================================================

package com.kraken.telepad;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin {
  	
    private File optionsFile = new File("plugins/TelePad", "options.yml");
    private FileConfiguration options = YamlConfiguration.loadConfiguration(optionsFile);
	
	boolean opRequired = true;
	
	@Override
    public void onEnable() {
    	
    	getLogger().info("TelePad has been enabled.");
		PluginManager pm = getServer().getPluginManager();
		TPListener listener = new TPListener();
		pm.registerEvents(listener, this);
		
	  //Initialize the censor with a dummy value
        if ( !options.getBoolean("loaded") ) {
        	options.set("loaded", true);
        	options.set("opRequired", true);
        }
        
        try {
        	options.save(optionsFile);
		} catch (IOException ioe1) {
			System.out.println("Could not properly initialize TelePad options file, expect possible errors.");
		}

        opRequired = options.getBoolean("opRequired");
		
    }
    
    @Override
    public void onDisable() {
    	
        getLogger().info("TelePad has been disabled.");
        saveConfig();
        
    }
    
    public TelePad getInstance() {
    	return this;
    }
    
  //TelePad commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
		Player player = (Player) sender;
		
      //Command: telepad
        if ( cmd.getName().equalsIgnoreCase("telepad") && sender instanceof Player ) {
            player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin (v1.2)");
            return true;
        }
        
      //Player commands
        if ( sender instanceof Player ) {
        
        	if ( opRequired && !player.isOp() ) {
        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
                return true;
        	}
        	
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
			        
			  //Command: opRequired
        	    case "opRequired":
        	    case "oprequired":
        	    case "opReq":
        	    case "opreq":
        			  
        	    	if ( args.length == 1 ) {
        	    		switch ( args[0].toLowerCase() ) {
        	    			case "on":
        	    			case "enable":
        	    			case "enabled":
        	    			case "true":
        	    				options.set("opRequired", true);
        	    				opRequired = true;
	    	    				try {
	    	    			        options.save(optionsFile);
	    	    				} catch (IOException ioe2) {
	    	    					// No need to fuss!
	    	    				}
        	    				return true;
        	    			case "off":
        	    			case "disable":
        	    			case "disabled":
        	    			case "false":
        	    				options.set("opRequired", false);
        	    				opRequired = false;
        	    				try {
	    	    			        options.save(optionsFile);
	    	    				} catch (IOException ioe3) {
	    	    					// No need to fuss!
	    	    				}
        	    				return true;
        	    			default:
        	    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/opRequired <on/off>\".");
        	        	    	return true;
        	    		}
        	    	}
        	    	
        	    	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/opRequired <on/off>\".");
        	    	return true;
			        
			    default:
			    	  
			        player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Command not recognized.");
			        return true;
			    
	        }
        
        }
        
        player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
        return true;
        
    }
    
}
