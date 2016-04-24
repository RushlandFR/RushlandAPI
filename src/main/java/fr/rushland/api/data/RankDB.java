/*
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt'.
 * Copyright (C) 2016 by DOCQUIER B. and RUSHLAND . 
 * All right reserved.  
 */

package fr.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import fr.rushland.api.BukkitInjector;
import fr.rushland.api.RushlandAPI;

public class RankDB {

	@SuppressWarnings("unused")
	private BukkitInjector rushland;
	private RushlandAPI api;
	private HashMap<String, Integer> rankList = new HashMap<>();

	public HashMap<String, Integer> getRankList() {
		return rankList;
	}

	public RankDB(BukkitInjector rushland, RushlandAPI api){
		this.api = api;
		this.rushland = rushland;
	}

	public void insertRankList() {
		int karmaSize = getAllRank();
		for (int i = 1; i <= karmaSize; i++) {
			String karma = getRankName(i);
			rankList.put(karma, getRankLevel(karma));
		}
	}
	
	public int getAllRank()  {
		int maxSub = 0;

		try {
			PreparedStatement queryStatement = this.api.getDataManager().getconnection().prepareStatement("SELECT MAX(id) FROM RankSystem");
			queryStatement.executeQuery();
			ResultSet resultSet = queryStatement.getResultSet();
			while (resultSet.next()) {
				maxSub = resultSet.getInt(1);
			}
			return maxSub;		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxSub;
	}

	public int getRankLevel(String karma) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT permLevel FROM RankSystem WHERE rank = ?");
			pst.setString(1, karma);
			pst.executeQuery();
			ResultSet result = pst.getResultSet();
			if (result.next()) {
				return result.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e){
			e.printStackTrace();
			return 0;
		}
	}

	public String getRankName(int id) {
		try {
			String rank;
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT rank FROM RankSystem WHERE id = ?");
			pst.setInt(1, id);
			pst.executeQuery();
			ResultSet result = pst.getResultSet();

			if(result.next()) {
				rank = result.getString(1);
			} else {
				rank = "null";
			}

			return rank;
		} catch (SQLException e){
			e.printStackTrace();
			return "null";
		}
	}

	public boolean isRankExist(String rankname) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT id FROM RankSystem WHERE rank = ?");

			pst.setString(1, rankname);
			pst.executeQuery();
			ResultSet result = pst.getResultSet();

			if (result.next()) {			
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addnewRank(String rankname, int defaultranklevel) {
		try {
			if (!isRankExist(rankname)) {
				PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO RankSystem(rank, permLevel) VALUES(?, ?)");

				pst.setString(1, rankname);
				pst.setInt(2, defaultranklevel);	

				pst.executeUpdate();
				pst.close();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeRank(String rankname) {
		try {
			if (isRankExist(rankname)) {
				PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("DELETE FROM RankSystem WHERE rank = ?");

				pst.setString(1, rankname);

				pst.executeUpdate();
				pst.close();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setdefaultlevel(int ID, String name, int level){
		if (ID == 1) {
			try {
				if (isRankExist(name)) {
					PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE RankSystem SET permLevel = ? WHERE rank = ?");

					pst.setInt(1, level);
					pst.setString(2, name);

					pst.executeUpdate();
					pst.close();

					return true;
				} else {
					return false;
				}
			} catch (SQLException e){
				e.printStackTrace();
				return false;
			}
		} else if (ID == 2) {
			try {
				if (api.getDataManager().getKarmaDB().getKarmaList().containsKey(name)) {
					PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE KarmaSystem SET permLevel = ? WHERE karma = ?");

					pst.setInt(1, level);
					pst.setString(2, name);

					pst.executeUpdate();
					pst.close();
					return true;
				} else {
					return false;
				}
			} catch (SQLException e){
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
