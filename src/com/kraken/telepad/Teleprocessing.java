package com.kraken.telepad;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Teleprocessing {
	
	TelePad plugin;
	
	String openStr = ChatColor.RED + "[TP]" + ChatColor.GRAY + " | ";
	
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
        		player.sendMessage(openStr + "Teleport " + args[0] + " was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");        			
    		}
    		
    		return;
    		
    	} else {
    		player.sendMessage(openStr + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/tele <name>");
    		return;
    	}
		
	}
	
  //"teleother" command processing
	@SuppressWarnings("deprecation")
	public void teleOther(Player player, String[] args) {
		
		if ( !player.hasPermission("telepad.teleother") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		boolean targetFound = Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getServer().getPlayerExact(args[0]));
		
		if (args.length > 2 || args.length < 1) {
    		player.sendMessage(openStr + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teleother <player> {go/bring}");
    		return;
    	}
    		
    	if (targetFound) {

    		Player target = player.getServer().getPlayerExact(args[0]);
    		
    		if (args.length == 1) {
    			//Bring teleport
    			target.teleport(player.getLocation());
    		} else if (args[1].equalsIgnoreCase("go")) {
    			//Go teleport
    			player.teleport(target.getLocation());
    		} else if (args[1].equalsIgnoreCase("bring")) {
    			//Bring teleport
    			target.teleport(player.getLocation());
    		} else {
    			player.sendMessage(openStr + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teleother <player> {go/bring}");
    		}
    		
    		return;
    		
    	} else {
    		player.sendMessage(openStr + "Player " + args[0] + " was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");
    		return;
    	}
		
	}
	
  //"teleset" command processing
	public void teleSet(Player player, String[] args) {
		
		if ( !player.hasPermission("telepad.teleset") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		if (args.length == 0 || args.length > 1) {
			
        	player.sendMessage(openStr + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teleset <name>");
        	return;
        	
        } else {
        	
        	String teleName = new String(args[0]);
        	Location location = player.getLocation();
        	
        	//Set teleport to config
        	if (!plugin.getConfig().contains(teleName)) {

    			plugin.getConfig().set(teleName, LocSerialization.getStringFromLocation(location));
    			plugin.saveConfig();
    			
    			//Check for successful teleport creation
    			String successTxt = ChatColor.RED + "not";
				if (plugin.getConfig().contains(teleName)) {
					successTxt = ChatColor.GREEN + "successfully";
				}
				
				player.sendMessage(openStr + "Teleport \"" + teleName + "\" was " + successTxt + ChatColor.GRAY + " created.");
				return;
        	
        	} else {
        		player.sendMessage(openStr + "Teleport name \"" + teleName + "\" is " + ChatColor.RED + "already in use" + ChatColor.GRAY + "!");
        	}
        	
        	return;
        	
        }
		
	}
	
  //"teleset" console command processing
	public void teleSetConsole(String[] args) {
		
		if (args.length == 5) {
			
			//Get teleport name
			String teleName = new String(args[0]);
			
			//Create default placeholder variables
			World world = Bukkit.getServer().getWorlds().get(0);
			Location location = new Location(world, 0, 0, 0);
			double xCoord = 0;
			double yCoord = 0;
			double zCoord = 0;
			
			//Try to parse world name
			try {
				world = Bukkit.getServer().getWorld(args[1]);
			} catch (NullPointerException e) {
				System.out.println("World name not found!");
				return;
			}
        	
			//Try to parse coordinates
        	try {
    			xCoord = Double.parseDouble(args[2]);
    			yCoord = Double.parseDouble(args[3]);
    			zCoord = Double.parseDouble(args[4]);
        	} catch (NumberFormatException e) {
				System.out.println("Coordinates must be numeric!");
				return;
        	}
        	
        	//Set location
        	location = new Location(world, xCoord, yCoord, zCoord);
        	
        	//Create teleport if name not already taken
        	if ( !plugin.getConfig().contains(teleName) ) {

    			//Set teleport to config
    			plugin.getConfig().set( teleName, LocSerialization.getStringFromLocation(location) );
    			plugin.saveConfig();
    			
    			//Check if teleport was successfully created
    			String successTxt = "not";
    			
				if (plugin.getConfig().contains(teleName)) {
					successTxt = "successfully";
				}
				
				System.out.println("Teleport \"" + teleName + "\" was " + successTxt + " created.");
				return;
        	
        	} else {
        		System.out.println("Teleport name \"" + teleName + "\" is already in use!");
        		return;
        	}
        	
        } else {
        	System.out.println("Incorrect format. Please use: /teleset <teleName> <worldName> <x-coord> <y-coord> <z-coord>");
        	return;
        }
		
	}
	
  //"teledel" command processing
	public void teleDelete(boolean isPlayer, Player player, String[] args) {
		
		if ( isPlayer && !player.hasPermission("telepad.teledel") && plugin.options.get("permsRequired") ) {
			return;
		}
		
		if (args.length == 0 || args.length < 1) {
			if (isPlayer) {
				player.sendMessage(openStr + ChatColor.RED + "Incorrect format. Please use: " + ChatColor.GRAY + "/teledel <name>");
			} else {
				System.out.println("Incorrect format. Please use: \"/teledel <name>\"");
			}
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
						if (isPlayer) {
							player.sendMessage(openStr + "Teleport \"" + args[0] + "\" was " + ChatColor.GREEN + "successfully" + ChatColor.GRAY + " deleted.");
						} else {
							System.out.println("Teleport \"" + args[0] + "\" was successfully deleted.");
						}
					} else {
						if (isPlayer) {
							player.sendMessage(openStr + "Teleport \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " deleted.");
						} else {
							System.out.println("Teleport \"" + args[0] + "\" was not deleted.");
						}
					}
					
					return;
					
	    	} else {
	    		if (isPlayer) {
	    			player.sendMessage(openStr + "Teleport name \"" + args[0] + "\" was " + ChatColor.RED + "not" + ChatColor.GRAY + " found.");
	    		}  else {
					System.out.println("Teleport name \"" + args[0] + "\" was not found.");
				}
	    		return;
	    	}
	    	
	    }
		
	}
	
  //"telelist" command processing
	public void teleList(boolean isPlayer, Player player) {
		
		if ( isPlayer && !player.hasPermission("telepad.telelist") && plugin.options.get("permsRequired") ) {
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
    			teleList += teleName;
    		} else {
    			if (isPlayer) {
    				teleList += teleName + ChatColor.GRAY + ", " + ChatColor.GREEN;
    			} else {
    				teleList += teleName + ", ";
    			}
    		}
    		
    	}
    	
    	if (isPlayer) {
    		player.sendMessage(openStr + "Here is a list of teleports:\n" + ChatColor.GREEN + teleList);
    	} else {
    		System.out.println("Here is a list of teleports: " + teleList);
    	}
    	
    	return;
		
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
    	player.sendMessage(openStr + "You have jumped " + ChatColor.GREEN + (int)teleportLocation.distance(originalLocation) + ChatColor.GRAY + " meters.");
    	
    	return;
		
	}
    
}
