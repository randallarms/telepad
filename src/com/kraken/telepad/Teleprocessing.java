package com.kraken.telepad;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Teleprocessing {
	
	TelePad plugin;
	
  //Constructor
	public Teleprocessing(TelePad plugin) {
		this.plugin = plugin;
	}
	
  //"tele" command processing
	public void teleport(Player player, String[] args) {
		
		if ( !player.hasPermission("telepad.tp") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		if (args.length != 0) {	
    		
    		String teleName = new String("teleName not properly set");
    		
        	for (String key : plugin.getConfig().getKeys(false)) {
        		if (args[0].equalsIgnoreCase(key)) {
        			teleName = key;
        			break;
        		}
        	}

    		if (plugin.getConfig().contains(teleName)) {
    			String teleLocation = plugin.getConfig().get(teleName).toString();
    			player.teleport(LocSerialization.getLocationFromString(teleLocation));
    		} else {
        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport " + args[0] + " was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");        			
    		}
    	} else {
    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/tele <name>");
    	}
		
	}
	
  //"teleset" command processing
	public void teleSet(Player player, String[] args) {
		
		if ( !player.hasPermission("telepad.teleset") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		if (args.length == 0 || args.length > 1) {
        	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teleset <name> (with 3 characters or more in the name)");
        } else {
        	
        	String teleName = new String(args[0]);
        	Location location = player.getLocation();
        	
        	if (!plugin.getConfig().contains(teleName)) {

        			plugin.getConfig().set(teleName, LocSerialization.getStringFromLocation(location));
        			plugin.saveConfig();
					if (plugin.getConfig().contains(teleName)) {
						player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + teleName + "\" was " + ChatColor.GREEN + "successfully" + ChatColor.GRAY + " created.");
					} else {
						player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + teleName + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " created.");
					}
        	
        	} else {
        		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport name \"" + teleName + "\" is " + ChatColor.RED + "already in use" + ChatColor.GRAY + "!");
        	}
        	
        }
		
	}
	
  //"teledel" command processing
	public void teleDelete(Player player, String[] args) {
		
		if ( !player.hasPermission("telepad.teledel") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		if (args.length == 0 || args.length < 1) {
	    	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teledel <name>");
	    } else {
	    	
			String teleName = new String("teleName not properly set");
			
	    	for (String key : plugin.getConfig().getKeys(false)) {
	    		if (args[0].equalsIgnoreCase(key)) {
	    			teleName = key;
	    			break;
	    		}
	    	}
	    	
	    	if ( plugin.getConfig().contains(teleName) ) {

	    			plugin.getConfig().set(teleName, null);
					plugin.saveConfig();
					if (!plugin.getConfig().contains(teleName)) {
						player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + args[0] + "\" was " + ChatColor.GREEN + "successfully" + ChatColor.GRAY + " deleted.");
					} else {
						player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " deleted.");
					}
	    	
	    	} else {
	    		player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Teleport name \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");
	    	}
	    	
	    }
		
	}
	
  //"telelist" command processing
	public void teleList(Player player) {
		
		if ( !player.hasPermission("telepad.telelist") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		String teleList = "";
    	int teleListLength = 0;
    	
		int telesLength = 0;
    	for (@SuppressWarnings("unused") String key : plugin.getConfig().getKeys(false)) {
    		telesLength++;
    	}
    	
    	for (String key : plugin.getConfig().getKeys(false)) {
    		String teleName = key;    		
    		teleListLength++;
    		if (teleListLength == telesLength) {
    			teleList+= teleName;
    		} else {
    			teleList+= teleName + ChatColor.GRAY + ", " + ChatColor.GREEN;
    		}
    		
    	}
    	
    	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | " + "Here is a list of teleports:\n" + ChatColor.GREEN + teleList);
		
	}
	
  //"jump" command processing
	public void jump(Player player) {
		
		if ( !player.hasPermission("telepad.jump") && plugin.options.get("permsRequired") ) {
			return;
		}
		
    	Location originalLocation = player.getLocation();
    	Random random = new Random();
    	Location teleportLocation = null;
    	
    	int x = random.nextInt(150) + 100;
    	int y = 255;
    	int z = random.nextInt(150) + 100;
    	boolean isOnLand = false;
    	while (isOnLand == false) {
    		teleportLocation = new Location(player.getWorld(), x, y, z);
    		if (teleportLocation.getBlock().getType() != Material.AIR) {
    			isOnLand = true;
    		} else y--;
    	}
    	
    	player.teleport(new Location(player.getWorld(), teleportLocation.getX(), teleportLocation.getY() + 1, teleportLocation.getZ()));
    	player.sendMessage(ChatColor.RED + "[TP]" + ChatColor.GRAY + " | You have jumped " + ChatColor.GREEN + (int)teleportLocation.distance(originalLocation) + ChatColor.GRAY + " meters.");
		
	}
    
}
