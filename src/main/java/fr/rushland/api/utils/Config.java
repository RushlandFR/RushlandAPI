package fr.rushland.api.utils;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class Config {

	private BukkitInjector rushland;
	@SuppressWarnings("unused")
	private RushlandAPI api;


	public Config(BukkitInjector rushland,RushlandAPI api){
		this.api = api;
		this.rushland = rushland;
	}

	public void initConf () {
		rushland.saveDefaultConfig();

	}


}
