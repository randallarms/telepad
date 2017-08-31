// ========================================================================
// |TELEPAD v1.4.1
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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin {
	
	public static String VERSION = "1.4.1";
	
	boolean opRequired = true;
	boolean permsRequired = true;
	
	File optionsFile = new File("plugins/TelePad", "options.yml");
    FileConfiguration options = YamlConfiguration.loadConfiguration(optionsFile);
	
	@Override
    public void onEnable() {
		
	  //Welcome
		getLogger().info("[TELEPAD] TelePad enabling...");
    	
	  //Plugin start-up
		PluginManager pm = getServer().getPluginManager();
		TPListener listener = new TPListener();
		pm.registerEvents(listener, this);

      //Checks for an options file, or creates a default version
        if ( !options.getBoolean("loaded") ) {
        	options.set("loaded", true);
        	options.set("opRequired", true);
        	options.set("permsRequired", true);
        	try {
            	options.save(optionsFile);
    		} catch (IOException ioe) {
    			System.out.println("[TELEPAD] Could not properly initialize TelePad options file, expect possible errors.");
    		}
        }

      //Initial run options setting
        opRequired = options.getBoolean("opRequired");
        getLogger().info("[TELEPAD] TelePad opRequired enabled: " + opRequired);
        
        permsRequired = options.getBoolean("permsRequired");
        getLogger().info("[TELEPAD] TelePad permsRequired enabled: " + permsRequired);
		
    }
    
    @Override
    public void onDisable() {
    	
      //Salutations
        getLogger().info("[TELEPAD] TelePad disabling...");
        
    }
    
    public TelePad getInstance() {
    	return this;
    }
    
  //Setter for op requirement on command
    public void setOpRequired(boolean opRequired) {
    	this.opRequired = opRequired;
    	options.set("opRequired", opRequired);
    	try {
        	options.save(optionsFile);
		} catch (IOException ioe) {
			System.out.println("[TELEPAD] Could not properly set opReq, expect possible errors.");
		}
    }
    
  //Setter for permissions requirement on command
    public void setPermsRequired(boolean permsRequired) {
    	this.permsRequired = permsRequired;
    	options.set("permsRequired", permsRequired);
    	try {
        	options.save(optionsFile);
		} catch (IOException ioe) {
			System.out.println("[TELEPAD] Could not properly set permsReq, expect possible errors.");
		}
    }
    
  //TelePad commands
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
    	Player player = Bukkit.getServer().getPlayerExact("Octopus__");
		boolean isPlayer = false;
        
      //Player commands
        if ( sender instanceof Player ) {
        	player = (Player) sender;
        	isPlayer = true;
        }
        
    	if ( opRequired && !player.isOp() || isPlayer == false ) {
    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
            return true;
    	}
    	
    	Teleprocessing tp = new Teleprocessing(this);
    	
    	switch ( command.toLowerCase() ) {
    	
    	  //Command: telepad
    		case "telepad":
			  
    			player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin (" + VERSION + ")");
    			return true;
    	
		  //Command: jump
    	    case "jump":
    			  
    			tp.jump(player);
		        return true;
		    
		  //Command: tele, tp
    		case "tele":
    		case "tp":
    			
		    	tp.teleport(player, args);
		    	return true; 
		    
		  //Command: teledel
    		case "teledel":
    		case "teledelete":
    		case "tpdel":
    		case "tpdelete":
		        
		    	tp.teleDelete(player, args);
		        return true;
		    
		  //Command: telelist
    		case "telelist":
    		case "tplist":
		    	
		    	tp.teleList(player);
		    	return true;
		    
		  //Command: teleset
    		case "teleset":
    		case "tpset":
		        
		    	tp.teleSet(player, args);
		        return true;
		        
		  //Command: opReqTP
    	    case "oprequiredtp":
    	    case "opreqtp":
    	    		
    	    	if ( !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    	
	    		switch ( args[0].toLowerCase() ) {
	    		
	    			case "on":
	    			case "enable":
	    			case "enabled":
	    			case "true":
	    				setOpRequired(true);
	    				return true;
	    			case "off":
	    			case "disable":
	    			case "disabled":
	    			case "false":
	    				setOpRequired(false);
	    				return true;
	    			default:
	    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/opReqTP <on/off>\".");
	        	    	return true;
	        	    	
	    		}
	    		
		  //Command: permsReqTP
    	    case "permsrequiredtp":
    	    case "permsreqtp":
    	    	
    	    	if ( !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    		
	    		switch ( args[0].toLowerCase() ) {
	    		
	    			case "on":
	    			case "enable":
	    			case "enabled":
	    			case "true":
	    				setPermsRequired(true);
	    				return true;
	    			case "off":
	    			case "disable":
	    			case "disabled":
	    			case "false":
	    				setPermsRequired(false);
	    				return true;
	    			default:
	    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/permsReqTP <on/off>\".");
	        	    	return true;
	        	    	
	    		}
		        
	      //Command not recognized
		    default:
		    	  
		        player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Command not recognized.");
		        return true;
		    
        }
        
    }
    
}
