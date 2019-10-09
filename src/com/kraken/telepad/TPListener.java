package com.kraken.telepad;

import java.util.WeakHashMap;

import org.bukkit.event.Listener;

public class TPListener implements Listener {
	
	TelePad plugin;
	WeakHashMap<String, Boolean> options;
	
	public TPListener(TelePad plugin, WeakHashMap<String, Boolean> options) {
		
		this.plugin = plugin;
		this.options = options;
		
	}
	
}
