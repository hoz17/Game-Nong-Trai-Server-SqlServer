package DAO;

import Model.Crop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CropDAO extends DAO {

    public CropDAO() {
        super();
    }

    public Crop getCrop() {
        int cropID[] = new int[20];
        String cropName[] = new String[20];
        int cropGrowTime[] = new int[20];
        int cropBuyPrice[] = new int[20];
        int cropSellPrice[] = new int[20];
        int waterLevel[] = new int[20];
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM crop");
            ResultSet rs = preparedStatement.executeQuery();
            for (int i = 0; i < 20 && rs.next(); i++) {
                cropID[i] = rs.getInt(1);
                cropName[i] = rs.getString(2);
                cropGrowTime[i] = rs.getInt(3);
                cropBuyPrice[i] = rs.getInt(4);
                cropSellPrice[i] = rs.getInt(5);
                waterLevel[i] = rs.getInt(6);
            }
            rs.close();
            return new Crop(cropID, cropName, cropGrowTime, cropBuyPrice, cropSellPrice, waterLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
