package DAO;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DAO {
    public static Connection conn;

    public DAO() {

    }

    public static void connection() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setServerName("DARJEELING\\SQLEXPRESS");
        ds.setPortNumber(1433);
        ds.setDatabaseName("gamenongtrai");
        ds.setEncrypt(false);

        try {
            conn = ds.getConnection();
            System.out.println("Kết nối database thành công !");
            System.out.println(conn.getCatalog());
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Kết nối database thất bại !");

        }
    }
}
