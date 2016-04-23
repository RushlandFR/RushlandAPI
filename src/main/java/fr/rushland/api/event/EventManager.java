/*
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt'.
 * Copyright (C) 2016 by DOCQUIER B. and RUSHLAND . 
 * All right reserved.  
*/

package fr.rushland.api.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.rushland.api.Main;
import fr.rushland.api.RushlandAPI;

public class EventManager {

	private Main rushland;
	private RushlandAPI api;
	private PluginManager pm;
	 
	public EventManager (Main rushland, RushlandAPI api){
		this.rushland = rushland;
		this.api = api;
		this.pm = Bukkit.getPluginManager();
	}
	
	public void registerEvent() {
		
		this.pm.registerEvents(new PlayerJoin(this.rushland,this.api), this.rushland);
		
		
	}
	
}
