package DAO;

import Model.Land;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
            Integer[] cropID = new Integer[32];
            Timestamp[] plantTime = new Timestamp[32];
            int[] waterLevel = new int[32];
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM land WHERE Player_ID=?");
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            for (int i = 0; i < 32 && rs.next(); i++) {
                slot[i] = rs.getInt(3);
                state[i] = rs.getInt(4);
                if (rs.getString(5) == null) {
                    cropID[i] = null;
                } else
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

    public int harvest(int playerID, int slot) {
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET Crop_ID = null WHERE Player_ID = ? AND Slot = ?");
            preparedStatement.setInt(1, playerID);
            preparedStatement.setInt(2, slot);
            execute = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;
    }

    public int waterPlant(int userID, int waterLevel, int slot) {
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET Water_level = ? WHERE Player_ID = ? AND Slot = ?");
            preparedStatement.setInt(1, waterLevel);
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, slot);
            execute = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return execute;
    }

    public int plantCrop(int userID, Timestamp plantTime, int cropID, int slot) {
        int exe = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET Crop_ID = ?, Plant_time = ? , Water_level = 0 WHERE Player_ID = ? AND Slot = ?");
            preparedStatement.setInt(1, cropID);
            preparedStatement.setTimestamp(2, plantTime);
            preparedStatement.setInt(3, userID);
            preparedStatement.setInt(4, slot);
            exe = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exe;
    }

    public int trample(int userID, int slot) {
        int exe = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE land SET Crop_ID = null  WHERE Player_ID = ? AND Slot = ?");
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, slot);
            exe = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exe;
    }

    public Land getGuestIsland(int guestID) {
        int[] slot = new int[32];
        int[] state = new int[32];
        Integer[] cropID = new Integer[32];
        Timestamp[] plantTime = new Timestamp[32];
        int[] waterLevel = new int[32];
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM land WHERE Player_ID = ?");
            preparedStatement.setInt(1, guestID);
            ResultSet rs = preparedStatement.executeQuery();
            for (int i = 0; i < 32 && rs.next(); i++) {
                slot[i] = rs.getInt(3);
                state[i] = rs.getInt(4);
                cropID[i] = rs.getInt(5);
                plantTime[i] = rs.getTimestamp(6);
                waterLevel[i] = rs.getInt(7);
            }
            rs.close();
            return new Land(guestID, slot, state, cropID, plantTime, waterLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
