package fr.rushland.api.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

import fr.rushland.api.RushlandAPI;

public class PlayerDB {

	private RushlandAPI api;

	public PlayerDB(RushlandAPI api){
		this.api = api;
	}

	public boolean isInsert(Player player) {
		boolean insert = false;
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
			pst.setString(1, player.getUniqueId().toString());
			pst.executeQuery();
			ResultSet result = pst.getResultSet();
			insert = result.next();
			pst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return insert;
	}
	
	public ResultSet getPlayerInfo(Player player) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("SELECT *, NOW() as now FROM PlayerInfo WHERE uuid = ?");
			pst.setString(1, player.getUniqueId().toString());
			pst.executeQuery();
			ResultSet result = pst.getResultSet();
			if (result.next())
			return result;
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public void setPlayerPermLevel(Player player , int ranklevel){
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET permLevel = ? WHERE uuid=?");

			pst.setInt(1, ranklevel);
			pst.setString(2, player.getUniqueId().toString());
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setRankPlayer(Player player, String rank, boolean isFemale) {
		try {
			if (this.api.getDataManager().getRankSystemDB().isRankExist(rank)) {
				PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET playerRank = ?, rankFemale = ? WHERE uuid = ?");

				pst.setString(1, rank);
				pst.setBoolean(2, isFemale);
				pst.setString(3, player.getUniqueId().toString());

				pst.executeUpdate();
				pst.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void setKarmaPlayer(Player player, String karma ) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = ?, expire = DATE_ADD(NOW(), INTERVAL 31 DAY) WHERE uuid = ?");

			pst.setString(1, karma);
			pst.setString(2, player.getUniqueId().toString());
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void deleteKarmaPlayer(Player player) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("UPDATE PlayerInfo SET playerKarma = player, expire = null WHERE uuid = ?");

			pst.setString(1, player.getUniqueId().toString());
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void playerInit(Player player) {
		try {
			PreparedStatement pst = this.api.getDataManager().getconnection().prepareStatement("INSERT INTO PlayerInfo(uuid, playerName, permLevel, playerRank, playerKarma, rushcoins, shopcoins) VALUES(?, ?, ?, ?, ?, ?, ?)");
			pst.setString(1, player.getUniqueId().toString());
			pst.setString(2, player.getName());
			pst.setInt(3, 0);
			pst.setString(4, "player");
			pst.setString(5, "player");
			pst.setInt(6, 0);
			pst.setInt(7, 0);
			pst.executeUpdate();
			pst.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
