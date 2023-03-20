package Main;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static Connection conn;

    public static void main(String[] args) {
        Connection1();
    }

    public static void Connection1() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setServerName("DARJEELING\\SQLEXPRESS");
        ds.setPortNumber(1433);
        ds.setDatabaseName("Farm");
        ds.setEncrypt(false);

        try {
            conn = ds.getConnection();
            System.out.println("Success");
            System.out.println(conn.getCatalog());
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Failed");

        }
    }
}
