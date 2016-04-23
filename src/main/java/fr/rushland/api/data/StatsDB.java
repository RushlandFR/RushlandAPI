/*
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt'.
 * Copyright (C) 2016 by DOCQUIER B. and RUSHLAND . 
 * All right reserved.  
 */

package fr.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.rushland.api.Main;
import fr.rushland.api.RushlandAPI;

public class StatsDB {

	private Main rushland;
	private RushlandAPI api;

	private String gameType;
	private boolean useKills;
	private boolean useDeaths;
	private boolean useWins;
	private boolean useLoses;

	private HashMap<String, Integer> kills = new HashMap<String, Integer>();
	private HashMap<String, Integer> deaths = new HashMap<String, Integer>();
	private ArrayList<String> wins = new ArrayList<String>();
	private ArrayList<String> loses = new ArrayList<String>();

	public StatsDB(Main rushland, RushlandAPI api) {
		this.api = api;
		this.rushland = rushland;
	}

	public void configStats(String gameType, boolean useKills, boolean useDeaths, boolean useWins, boolean useLoses) {
		this.gameType = gameType;
		this.useKills = useKills;
		this.useDeaths = useDeaths;
		this.useWins = useWins;
		this.useLoses = useLoses;
	}


	public boolean isInsert(String uuid) {
		try {
			PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("SELECT id FROM stats_" + gameType + " WHERE uuid = ?");
			queryStatement.setString(1, uuid);
			queryStatement.executeQuery();
			ResultSet resultSet = queryStatement.getResultSet();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}



	public void add(String uuid) {
		try {
			PreparedStatement queryStatement = null;
			if (useKills && useWins) {
				queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, kills, deaths, wins, loses) VALUES (?, ?, ?, ?, ?)");

			} else if (useKills) {
				queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, kills, deaths) VALUES (?, ?, ?)");
			} else if (useWins) {
				queryStatement = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO stats_" +  gameType + "(uuid, wins, loses) VALUES (?, ?, ?)");

			}

			queryStatement.setString(1, uuid);
			queryStatement.setInt(2, 0);
			queryStatement.setInt(3, 0);
			if (useKills && useWins) {
				queryStatement.setInt(4, 0);
				queryStatement.setInt(5, 0);
			}
			queryStatement.executeUpdate();
			queryStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public  void insertPlayer(String uuid) {
		try {
			rushland.getLogger().info("Inserting stats into SQL Server...");

			if (useKills && !kills.isEmpty() && kills.containsKey(uuid)) {

				int value = kills.get(uuid);
				PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET kills = kills + ? WHERE uuid = ?");
				queryStatement.setInt(1, value);
				queryStatement.setString(2, uuid);
				queryStatement.executeUpdate();
				queryStatement.close();
				kills.remove(uuid);

			}
			if (useDeaths && !deaths.isEmpty() && deaths.containsKey(uuid)) {
				int value = deaths.get(uuid);
				PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET deaths = deaths + ? WHERE uuid = ?");
				queryStatement.setInt(1, value);
				queryStatement.setString(2, uuid);
				queryStatement.executeUpdate();
				queryStatement.close();
				deaths.remove(uuid);


			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert() {
		try {
			rushland.getLogger().info("Inserting stats into SQL Server...");

			if (useKills && !kills.isEmpty()) {
				for (Entry<String, Integer> entry : kills.entrySet()) {
					String uuid = entry.getKey();
					int value = entry.getValue();
					PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET kills = kills + ? WHERE uuid = ?");
					queryStatement.setInt(1, value);
					queryStatement.setString(2, uuid);
					queryStatement.executeUpdate();
					queryStatement.close();
				}
			}
			if (useDeaths && !deaths.isEmpty()) {
				for (Entry<String, Integer> entry : deaths.entrySet()) {
					String uuid = entry.getKey();
					int value = entry.getValue();
					PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET deaths = deaths + ? WHERE uuid = ?");
					queryStatement.setInt(1, value);
					queryStatement.setString(2, uuid);
					queryStatement.executeUpdate();
					queryStatement.close();
				}
			}
			if (useWins && !wins.isEmpty()) {
				for (String uuid : wins) {
					PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET wins = wins + 1 WHERE uuid = ?");
					queryStatement.setString(1, uuid);
					queryStatement.executeUpdate();
					queryStatement.close();
				}
			}
			if (useLoses && !loses.isEmpty()) {
				for (String uuid : loses) {
					PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("UPDATE stats_" + gameType + " SET loses = loses + 1 WHERE uuid = ?");
					queryStatement.setString(1, uuid);
					queryStatement.executeUpdate();
					queryStatement.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
