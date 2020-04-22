package Tema.SGBD;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance=null;
    private Connection connection;

    private Database() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection =DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe","STUDENT","STUDENT");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}