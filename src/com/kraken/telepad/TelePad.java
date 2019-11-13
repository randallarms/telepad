// ========================================================================
// |TELEPAD v1.5.2.6
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin {
	
	public static String VERSION = "1.5.2.6";
	
	WeakHashMap<String, Boolean> options = new WeakHashMap<String, Boolean>();
	
	File optionsFile = new File("plugins/TelePad", "options.yml");
    FileConfiguration optionsConfig = YamlConfiguration.loadConfiguration(optionsFile);
	
	@Override
    public void onEnable() {
		
	  //Welcome
		getLogger().info("TelePad enabling...");

      //Checks for an options file, or creates a default version
		saveResource("options.yml", false);

      //Initial run options setting
        for ( String option : optionsConfig.getKeys(false) ) {
        	boolean value = optionsConfig.getBoolean(option);
	        options.put(option, value);
	        getLogger().info("TelePad " + option + " enabled: " + value);
        }
		
    }
    
    @Override
    public void onDisable() {
    	
      //Salutations
        getLogger().info("TelePad disabling...");
        
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
			System.out.println("TelePad could not properly set option '" + option + "', expect possible errors.");
		}
    	
    }
    
  //TelePad commands
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
    	Player player = Bukkit.getServer().getPlayerExact("Octopus__"); //Placeholder player; only used if isPlayer
    	boolean isPlayer = false;
		
		boolean opRequired = options.get("opRequired");
		boolean permsRequired = options.get("permsRequired");
        
      //Player commands
        if ( sender instanceof Player ) {
        	
        	player = (Player) sender;
        	isPlayer = true;
        	
        	if ( opRequired && !player.isOp() ) {
        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "You do not have the required 'op' permission level required.");
                return true;
        	}
        	
        } else {
        	
        	switch ( command.toLowerCase() ) {
        		case "jump":
        		case "tele":
	    			System.out.println("This is a player-only command, or TelePad does not support these console commands yet!");
	        		return true;
        	}
        	
        }
    	
    	Teleprocessing tp = new Teleprocessing(this);
    	
    	//Check for enable/disable arguments
    	boolean enableArg = false;
    	boolean disableArg = false;
    	
    	if (args.length > 0) {
    		
	    	List<String> enableArgs = new ArrayList<String>();
	    	enableArgs.add("on");
	    	enableArgs.add("enable");
	    	enableArgs.add("enabled");
	    	enableArgs.add("true");
	    	
	    	List<String> disableArgs = new ArrayList<String>();
	    	disableArgs.add("off");
	    	disableArgs.add("disable");
	    	disableArgs.add("disabled");
	    	disableArgs.add("false");
	    	
	    	enableArg = enableArgs.contains(args[0].toLowerCase());
	    	disableArg = disableArgs.contains(args[0].toLowerCase());
	    	
    	}
    	
    	//Execute the command
    	switch ( command.toLowerCase() ) {
    	
    	  //Command: telepad
    		case "telepad":
			  
    			if (isPlayer) {
    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin (" + VERSION + ") | More info @ github.com/randallarms/telepad");
    			} else {
    				System.out.println("TelePad | Teleports & warps plugin (v" + VERSION + ") | More info @ github.com/randallarms/telepad");
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
    			
    	  //Command: teleother
    		case "teleother":
    			
    			if (permsRequired && player.hasPermission("tp") && isPlayer) {
    				tp.teleOther(player, args);
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
    				tp.teleList(isPlayer, player);
    				return true;
    			}
    			
		  //Command: teleset
    		case "teleset":
    		case "tpset":

    			if (isPlayer) {
    				if (permsRequired && player.hasPermission("teleset")) {
	    				tp.teleSet(player, args);
			        	return true;
	    			}
    			} else {
    				tp.teleSetConsole(args);
    				return true;
    			}
    			
		  //Command: opReqTP
    	    case "oprequiredtp":
    	    case "opreqtp":
    	    		
    	    	if ( isPlayer && !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    	
    	    	if (enableArg) {
    				setOption("opRequired", true);
    				return true;
    	    	} else if (disableArg) {
    				setOption("opRequired", false);
    				return true;
    	    	} else {
    				if (isPlayer) {
    					player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/opReqTP <on/off>\".");
    				} else {
    					System.out.println("Try entering \"/opReqTP <on/off>\".");
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
    	    	
    	    	if (enableArg) {
    				setOption("permsRequired", true);
    				return true;
    	    	} else if (disableArg) {
    				setOption("permsRequired", false);
    				return true;
    	    	} else {
    				if (isPlayer) {
	    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/permsReqTP <on/off>\".");
    				} else {
    					System.out.println("Try entering \"/permsReqTP <on/off>\".");
    				}
        	    	return true;
    	    	}
	    		
	      //Command: sparkles
    	    case "sparklestp":
    	    	
    	    	if ( isPlayer && !player.isOp() ) {
    	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "This is an OP command.");
    	    		return true;
    	    	}
    	    	
    	    	if (enableArg) {
    				setOption("sparkles", true);
    				return true;
    	    	} else if (disableArg) {
    				setOption("sparkles", false);
    				return true;
    	    	} else {
    				if (isPlayer) {
	    				player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Try entering \"/sparklesTP <on/off>\".");
    				} else {
    					System.out.println("Try entering \"/sparklesTP <on/off>\".");
    				}
        	    	return true;
    	    	}
		        
	      //Command not recognized
		    default:
		    	
		    	if (isPlayer) {
		    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Command not recognized.");
		    	} else {
		    		System.out.println("Command not recognized.");
		    	}
		        return true;
		    
        }
        
    }
    
}
