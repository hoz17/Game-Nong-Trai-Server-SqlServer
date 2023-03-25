package DAO;

import Model.Inventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDAO extends DAO {
    public InventoryDAO() {
        super();
    }

    public Inventory getPlayerInventory(int userID) {
        int[] cropID = new int[20];
        int[] cropAmount = new int[20];
        int[] seedAmount = new int[20];
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM inventory WHERE Player_ID=?");
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            for (int i = 0; i < 20 && rs.next(); i++) {
                cropID[i] = rs.getInt(3);
                cropAmount[i] = rs.getInt(4);
                seedAmount[i] = rs.getInt(5);
            }
            rs.close();
            return new Inventory(userID, cropID, cropAmount, seedAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateSeed(int playerID, int cropID, int amount) {
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE inventory SET Seed_amount=? WHERE Player_ID=? AND Crop_ID=?");
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, playerID);
            preparedStatement.setInt(3, cropID);
            execute = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;
    }

    public int updateCrop(int playerID, int cropID, int amount) {
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE inventory SET Crop_amount = ? WHERE Player_ID = ? AND Crop_ID = ?");
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, playerID);
            preparedStatement.setInt(3, cropID);
            execute = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;
    }
}
