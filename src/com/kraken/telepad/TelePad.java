// ========================================================================
// |TELEPAD v1.4.2
// | by Kraken | https://www.spigotmc.org/resources/telepad.34953/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being 
// | sold or re-branded, please let me know. Thanks! 
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
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin {
	
	public static String VERSION = "1.4.2";
	
	WeakHashMap<String, Boolean> options = new WeakHashMap<String, Boolean>();
	
	File optionsFile = new File("plugins/TelePad", "options.yml");
    FileConfiguration optionsConfig = YamlConfiguration.loadConfiguration(optionsFile);
	
	@Override
    public void onEnable() {
		
	  //Welcome
		getLogger().info("[TELEPAD] TelePad enabling...");
    	
	  //Plugin start-up
		PluginManager pm = getServer().getPluginManager();

      //Checks for an options file, or creates a default version
        if ( !optionsConfig.getBoolean("loaded") ) {
        	optionsConfig.set("loaded", true);
        	optionsConfig.set("opRequired", true);
        	optionsConfig.set("permsRequired", true);
        	optionsConfig.set("sparkles", false);
        	try {
        		optionsConfig.save(optionsFile);
    		} catch (IOException ioe) {
    			System.out.println("[TELEPAD] Could not properly initialize TelePad options file, expect possible errors.");
    		}
        }

      //Initial run options setting
        boolean opRequired = optionsConfig.getBoolean("opRequired");
        options.put("opRequired", opRequired);
        getLogger().info("[TELEPAD] TelePad opRequired enabled: " + opRequired);
        
        boolean permsRequired = optionsConfig.getBoolean("permsRequired");
        options.put("permsRequired", permsRequired);
        getLogger().info("[TELEPAD] TelePad permsRequired enabled: " + permsRequired);
        
        boolean sparkles = optionsConfig.getBoolean("sparkles");
        options.put("sparkles", sparkles);
        getLogger().info("[TELEPAD] TelePad sparkles enabled: " + sparkles);
        
      //Starts and registers the Listener
		TPListener listener = new TPListener(this, options);
		pm.registerEvents(listener, this);
		
    }
    
    @Override
    public void onDisable() {
    	
      //Salutations
        getLogger().info("[TELEPAD] TelePad disabling...");
        
    }
    
    public TelePad getInstance() {
    	return this;
    }
    
  //Setter for options on command
    public void setOption(String option, boolean value) {
    	options.put(option, value);
    	optionsConfig.set(option, value);
    	try {
        	optionsConfig.save(optionsFile);
		} catch (IOException ioe) {
			System.out.println("[TELEPAD] Could not properly set option '" + option + "', expect possible errors.");
		}
    }
    
  //TelePad commands
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
    	Player player = Bukkit.getServer().getPlayerExact("Octopus__");
    	boolean isPlayer = false;
		
		boolean opRequired = options.get("opRequired");
		boolean permsRequired = options.get("permsRequired");
        
      //Player commands
        if ( sender instanceof Player ) {
        	
        	player = (Player) sender;
        	isPlayer = true;
        	
        	if ( opRequired && !player.isOp() ) {
        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have teleport privileges.");
                return true;
        	}
        	
        } else {
        	
        	switch ( command.toLowerCase() ) {
        		case "jump":
        		case "tele":
        		case "telelist":
        		case "tplist":
        		case "teleset":
        		case "tpset":
	    			System.out.println("[TELEPAD] | This is a player-only command, or TelePad does not support these console commands yet!");
	        		return true;
        	}
        	
        }
    	
    	Teleprocessing tp = new Teleprocessing(this);
    	
    	switch ( command.toLowerCase() ) {
    	
    	  //Command: telepad
    		case "telepad":
			  
    			if (isPlayer) {
    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin (" + VERSION + ")");
    			} else {
    				System.out.println("[TELEPAD] | TelePad | Teleports & warps plugin (" + VERSION + ")");
    			}
    			return true;
    	
		  //Command: jump
    	    case "jump":

    			if (permsRequired && player.hasPermission("jump") && isPlayer) {
    				tp.jump(player);
		        	return true;
    			}
    			
		  //Command: tele
    		case "tele":

    			if (permsRequired && player.hasPermission("tp") && isPlayer) {
    				tp.teleport(player, args);
		    		return true; 
    			}
    			
		  //Command: teledel
    		case "teledel":
    		case "teledelete":
    		case "tpdel":
    		case "tpdelete":
		        
    			if ( (permsRequired && isPlayer && player.hasPermission("teledel")) || !isPlayer ) {
    				tp.teleDelete(isPlayer, player, args);
		        	return true;
    			}
		    
		  //Command: telelist
    		case "telelist":
    		case "tplist":

    			if (permsRequired && player.hasPermission("telelist")) {
    				tp.teleList(player);
    				return true;
    			}
    			
		  //Command: teleset
    		case "teleset":
    		case "tpset":

    			if (permsRequired && player.hasPermission("teleset")) {
    				tp.teleSet(player, args);
		        	return true;
    			}
    			
		  //Command: opReqTP
    	    case "oprequiredtp":
    	    case "opreqtp":
    	    		
    	    	if ( isPlayer && !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    	
	    		switch ( args[0].toLowerCase() ) {
	    		
	    			case "on":
	    			case "enable":
	    			case "enabled":
	    			case "true":
	    				setOption("opRequired", true);
	    				return true;
	    			case "off":
	    			case "disable":
	    			case "disabled":
	    			case "false":
	    				setOption("opRequired", false);
	    				return true;
	    			default:
	    				if (isPlayer) {
	    					player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/opReqTP <on/off>\".");
	    				} else {
	    					System.out.println("[TELEPAD] | Try entering \"/opReqTP <on/off>\".");
	    				}
	        	    	return true;
	        	    	
	    		}
	    		
		  //Command: permsReqTP
    	    case "permsrequiredtp":
    	    case "permsreqtp":
    	    	
    	    	if ( isPlayer && !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    		
	    		switch ( args[0].toLowerCase() ) {
	    		
	    			case "on":
	    			case "enable":
	    			case "enabled":
	    			case "true":
	    				setOption("permsRequired", true);
	    				return true;
	    			case "off":
	    			case "disable":
	    			case "disabled":
	    			case "false":
	    				setOption("permsRequired", false);
	    				return true;
	    			default:
	    				if (isPlayer) {
		    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/permsReqTP <on/off>\".");
	    				} else {
	    					System.out.println("[TELEPAD] | Try entering \"/permsReqTP <on/off>\".");
	    				}
	        	    	return true;
	        	    	
	    		}
	    		
	      //Command: sparkles
    	    case "sparkles":
    	    	
    	    	if ( isPlayer && !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    		
	    		switch ( args[0].toLowerCase() ) {
	    		
	    			case "on":
	    			case "enable":
	    			case "enabled":
	    			case "true":
	    				setOption("sparkles", true);
	    				return true;
	    			case "off":
	    			case "disable":
	    			case "disabled":
	    			case "false":
	    				setOption("sparkles", true);
	    				return true;
	    			default:
	    				if (isPlayer) {
		    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/sparkles <on/off>\".");
	    				} else {
	    					System.out.println("[TELEPAD] | Try entering \"/sparkles <on/off>\".");
	    				}
	        	    	return true;
	        	    	
	    		}
		        
	      //Command not recognized
		    default:
		    	
		    	if (isPlayer) {
		    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Command not recognized.");
		    	} else {
		    		System.out.println("[TELEPAD] Command not recognized.");
		    	}
		        return true;
		    
        }
        
    }
    
}
