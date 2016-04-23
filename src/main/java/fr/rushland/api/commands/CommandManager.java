/*
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt'.
 * Copyright (C) 2016 by DOCQUIER B. and RUSHLAND . 
 * All right reserved.  
*/

package fr.rushland.api.commands;

import fr.rushland.api.Main;
import fr.rushland.api.RushlandAPI;

public class CommandManager {

	private RushlandAPI api;
	private Main rushland;
	
	
	public CommandManager(RushlandAPI api, Main rushland) {
		this.api = api;
		this.rushland = rushland;
	}
	
	public void load() {
		
        this.rushland.getCommand("money").setExecutor(new MoneyCommand(this.api));
        this.rushland.getCommand("grade").setExecutor(new RankCommand(this.api));
        this.rushland.getCommand("spec").setExecutor(new SpecCommand(this.api));
        this.rushland.getCommand("tp").setExecutor(new TpCommand(this.api));
        this.rushland.getCommand("leave").setExecutor(new LeaveCommand(this.api));
        this.rushland.getCommand("aacautoreport").setExecutor(new ReportCommand(this.api));

	}
}
