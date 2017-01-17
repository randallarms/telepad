// ========================================================================
// |TELEPAD v1.0
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/voicebox
// ========================================================================

package com.kraken.telepad;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TelePad extends JavaPlugin implements Listener {

	public static TelePad plugin;
  	
	@Override
    public void onEnable() {
    	
    	getLogger().info("TelePad has been enabled.");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		
    }
    
    @Override
    public void onDisable() {
    	
        getLogger().info("TelePad has been disabled.");
        saveConfig();
        
    }
    
    //To serialize & deserialize locations...
    public static String getStringFromLocation(Location l) {    	
	    if (l == null) {
	        return "";
	    }
	    return l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getYaw() + ":" + l.getPitch() ;	    
	}
    
    public static Location getLocationFromString(String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 6) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }
        return null;
    }
    
  //TelePad commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	String command = cmd.getName();
		Player player = (Player) sender;
		
      //Command: telepad
        if ( cmd.getName().equalsIgnoreCase("telepad") && sender instanceof Player ) {
            player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | TelePad | Teleports & warps plugin.");
            return true;
        }
        
      //OP commands
        if ( sender instanceof Player && player.isOp() ) {
        
        	switch (command) {
        	
			    //Command: jump
        		  case "jump":
        			  
			        	Location originalLocation = player.getLocation();
			        	Random random = new Random();
			        	Location teleportLocation = null;
			        	
			        	int x = random.nextInt(90) + 1;
			        	int y = 255;
			        	int z = random.nextInt(90) + 1;
			        	boolean isOnLand = false;
			        	while (isOnLand == false) {
			        		teleportLocation = new Location(player.getWorld(), x, y, z);
			        		if (teleportLocation.getBlock().getType() != Material.AIR) {
			        			isOnLand = true;
			        		} else y--;
			        	}
			        	
			        	player.teleport(new Location(player.getWorld(), teleportLocation.getX(), teleportLocation.getY() + 1, teleportLocation.getZ()));
			        	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | You have jumped " + ChatColor.GREEN + (int)teleportLocation.distance(originalLocation) + ChatColor.GRAY + " meters.");
			        	return true;
			    
			  //Command: tele
        		case "tele":
			    	
			    	if (args.length != 0) {	
			    		
			    		String teleName = new String("teleName not properly set");
			    		
			        	for (String key : getConfig().getKeys(false)) {
			        		if (args[0].equalsIgnoreCase(key)) {
			        			teleName = key;
			        			break;
			        		}
			        	}
			
			    		if (getConfig().contains(teleName)) {
			    			String teleLocation = getConfig().get(teleName).toString();
			    			player.teleport(getLocationFromString(teleLocation));
			    		} else {
			        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport " + args[0] + " was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");        			
			    		}
			    	} else {
			    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/tele <name>");
			    	}
			    	
			    	return true; 
			    
			  //Command: teledel
        		case "teledel":
			        
			    	if (args.length == 0 || args.length < 1) {
			        	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teledel <name>");
			        } else {
			        	
			    		String teleName = new String("teleName not properly set");
			    		
			        	for (String key : getConfig().getKeys(false)) {
			        		if (args[0].equalsIgnoreCase(key)) {
			        			teleName = key;
			        			break;
			        		}
			        	}
			        	
			        	if (getConfig().contains(teleName)) {
			
								getConfig().set(teleName, null);
								saveConfig();
								if (!getConfig().contains(teleName)) {
									player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + args[0] + "\" was " + ChatColor.GREEN + "successfully" + ChatColor.GRAY + " deleted.");
								} else {
									player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " deleted.");
								}
			        	
			        	} else {
			        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport name \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");
			        	}
			        	
			        }
			    	
			        return true;
			    
			  //Command: telelist
        		case "telelist":
			    	
			    	String teleList = "";
			    	int teleListLength = 0;
			    	
					int telesLength = 0;
			    	for (@SuppressWarnings("unused") String key : getConfig().getKeys(false)) {
			    		telesLength++;
			    	}
			    	
			    	for (String key : getConfig().getKeys(false)) {
			    		String teleName = key;    		
			    		teleListLength++;
			    		if (teleListLength == telesLength) {
			    			teleList+= teleName;
			    		} else {
			    			teleList+= teleName + ChatColor.GRAY + ", " + ChatColor.GREEN;
			    		}
			    		
			    	}
			    	
			    	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Here is a list of teleports:\n" + ChatColor.GREEN + teleList);
			    	
			    	return true;
			    
			  //Command: teleset
        		case "teleset":
			        
			    	if (args.length == 0 || args.length > 1) {
			        	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teleset <name> (with 3 characters or more in the name)");
			        } else {
			        	
			        	String teleName = new String(args[0]);
			        	Location location = player.getLocation();
			        	
			        	if (!getConfig().contains(teleName)) {
			
								getConfig().set(teleName, getStringFromLocation(location));
								saveConfig();
								if (getConfig().contains(teleName)) {
									player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + teleName + "\" was " + ChatColor.GREEN + "successfully" + ChatColor.GRAY + " created.");
								} else {
									player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + teleName + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " created.");
								}
			        	
			        	} else {
			        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport name \"" + teleName + "\" is " + ChatColor.RED + "already in use" + ChatColor.GRAY + "!");
			        	}
			        	
			        }
			    	
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
