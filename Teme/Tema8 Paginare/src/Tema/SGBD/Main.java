package Tema.SGBD;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection = null;

    Main() {
        try {
            connection = Database.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String readKeyboardInput() throws IOException {
        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        return reader.readLine();
    }

    public static void printTableNames(List<String> tables) {
        for (var i : tables) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static List<String> getDBTableNames() throws SQLException {
        List<String> result = new ArrayList<>();
        Statement stmt = connection.createStatement();
        String query = "SELECT Table_name FROM USER_TABLES";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            result.add(rs.getString(1));
        }
        rs.close();
        stmt.close();
        return result;
    }

    public static String readUserSelectedTable() throws SQLException, IOException {
        System.out.println("Introduceti numele tabelului pe care doriti sa il vizualizati.");
        System.out.print("Tabele disponibile:");
        ArrayList<String> tableNames = (ArrayList<String>) getDBTableNames();
        printTableNames(tableNames);
        System.out.print("Nume tabela: ");
        String name = readKeyboardInput().toUpperCase();
        while (!tableNames.contains(name)) {
            System.out.println("Numele introdus este invalid. Reintroduceti numele tabelei");
            name = readKeyboardInput();
        }
        return name;
    }

    public static Integer getNumberOfColumns(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableName + " WHERE ROWNUM <= 1");
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.close();
        return rsmd.getColumnCount();

    }

    public static void main(String[] args) throws SQLException, IOException {
        Database db = Database.getInstance();
        Main m = new Main();
        final int pagSize = 50;
        String numeTabel = readUserSelectedTable();

        String query = "select * from ( select a.*, rownum rnum from(SELECT * FROM " + numeTabel + ") a where " +
                "rownum <=? ) where rnum >=? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, 50);
        preparedStatement.setInt(2, 0);
        ResultSet rs = preparedStatement.executeQuery();
        int nrOfColumns = getNumberOfColumns(numeTabel);
        while (rs.next()) {
            for (int i = 1; i <= nrOfColumns; i++) {
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }
    }
}
