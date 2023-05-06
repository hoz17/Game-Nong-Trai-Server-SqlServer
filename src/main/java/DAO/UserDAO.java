package DAO;

import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO {
    public UserDAO() {
        super();
    }

    public User verifyUser(User user) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT *\n"
                    + "FROM player\n"
                    + "WHERE Username = ?\n"
                    + "AND Password = ?");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO player VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPlayerName());
            preparedStatement.setInt(4, 1000);
            preparedStatement.setInt(5, user.getGenderSkin());
            preparedStatement.setInt(6, user.getPetID());
            preparedStatement.executeUpdate();
            preparedStatement = conn.prepareStatement("SELECT Player_ID FROM player WHERE Username=?");
            preparedStatement.setString(1, user.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user.setUserID(rs.getInt(1));
            }
            addLand(user);
            addInventory(user);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addLand(User user) {
        try {
            for (int i = 0; i < 32; i++) {
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO land VALUES(?,?,?,null,?,?)");
                preparedStatement.setInt(1, user.getUserID());
                preparedStatement.setInt(2, i);
                if (i < 4) {
                    preparedStatement.setInt(3, 1);
                } else {
                    preparedStatement.setInt(3, 0);
                }
//                preparedStatement.setInt(4, null);
                preparedStatement.setTimestamp(4, null);
                preparedStatement.setInt(5, 0);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addInventory(User user) {
        try {
            for (int i = 0; i < 20; i++) {
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO inventory VALUES (?,?,?,?)");
                preparedStatement.setInt(1, user.getUserID());
                preparedStatement.setInt(2, i);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateMoney(int userID, int amount) {
        int execute = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE player SET Money=? WHERE Player_ID=?");
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, userID);
            execute = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;
    }

    public boolean checkDuplicated(String username) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM player WHERE Username = ?");
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkDuplicatedName(String playerName) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM player WHERE Player_name = ?");
            preparedStatement.setString(1, playerName);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLeaderBoard() {
        String output = "";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT TOP 10 Player_name, Money from player ORDER BY Money DESC ");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output += rs.getString(1) + "=" + rs.getInt(2) + "=";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String getVisitList(String input) {
        String output = "";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT Player_ID, Player_name FROM player WHERE Player_name like '%?%'");
            preparedStatement.setString(1, input);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output += rs.getInt(1) + rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }
}
