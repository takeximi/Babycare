package config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    //public static String serverName = "LAPTOP-7UOA152U\\SQLEXPRESS";
    public static String dbName = "PetShopDB";
    public static String portNumber = "1433";
    public static String userID = "sa";
    public static String password = "030303";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:sqlserver://localhost:1433;" + "databaseName=" + dbName + ";encrypt=false";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        System.out.println("Kết nối database");
        return DriverManager.getConnection(url, userID, password);
    }

    public static void getAll() {
        try {
            String query = "select * from tbltest"; //fix
            Connection con = DBConnect.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    Test connection
    public static void main(String[] args) {
        try {
            if (DBConnect.getConnection() != null) {
                System.out.println("Connect successfully!");
            } else {
                System.out.println("Connect failed!");
            }
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Error at model.DBContext.DBContext().getConnertion()");
        }
//System.out.println(checkUserNameExist("trumquang2002"));

    }
}
