package fr.rushland.api.utils;

import fr.rushland.api.Main;
import fr.rushland.api.RushlandAPI;

public class Config {

	private Main rushland;
	@SuppressWarnings("unused")
	private RushlandAPI api;


	public Config(Main rushland,RushlandAPI api){
		this.api = api;
		this.rushland = rushland;
	}

	public void initConf () {
		rushland.saveDefaultConfig();

	}


}
