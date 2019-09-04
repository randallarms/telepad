package com.kraken.telepad;

import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TPListener implements Listener {
	
	TelePad plugin;
	WeakHashMap<String, Boolean> options;
	
	public TPListener(TelePad plugin, WeakHashMap<String, Boolean> options) {
		
		this.plugin = plugin;
		this.options = options;
		
	}
	
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
		
		boolean sparkles = options.get("sparkles");
		
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if (from.distance(to) > 2 && sparkles) {
			
			//Animate sparkles from potions effects
				//...TBA
			
		}
		
	}
	
}
