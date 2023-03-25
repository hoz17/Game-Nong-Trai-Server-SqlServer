package DAO;

import Model.Land;

import java.sql.*;

public class LandDAO extends DAO {
    public LandDAO() {
        super();
    }

    public int buyFarmland(int userID, int slot) {
        int sqlResult = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET state=1 WHERE Player_ID=? AND Slot=?");
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, slot);
            sqlResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sqlResult;
    }

    public Land getPlayerFarmland(int userID) {
        try {
            int[] slot = new int[32];
            int[] state = new int[32];
            int[] cropID = new int[32];
            Timestamp[] plantTime = new Timestamp[32];
            int[] waterLevel = new int[32];
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM land WHERE Player_ID=?");
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            for (int i = 0; i < 32 && rs.next(); i++) {
                slot[i] = rs.getInt(3);
                state[i] = rs.getInt(4);
                cropID[i] = rs.getInt(5);
                plantTime[i] = rs.getTimestamp(6);
                waterLevel[i] = rs.getInt(7);
            }
            rs.close();
            return new Land(userID, slot, state, cropID, plantTime, waterLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int harvest(int playerID, int slot){
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET Crop_ID = ? WHERE Player_ID = ? AND Slot = ?");
            preparedStatement.setInt(1, -1);
            preparedStatement.setInt(2,playerID);
            preparedStatement.setInt(3,slot);
            execute = preparedStatement.executeUpdate();
        }catch  (SQLException e){
            e.printStackTrace();
        }
        return execute;
    }
}
