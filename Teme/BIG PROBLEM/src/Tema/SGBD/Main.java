package Tema.SGBD;

import java.sql.*;

class Main {
    private static Connection connection = null;

    Main() {
        try {
            connection = Database.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void executePLSQLFunction() throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{?= call urmatoarea_intrebare(?,?)}");
        String email="ion@yahoo.com";
        String rezolvare="1:A19,A11,A14";
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.setString(2, email);
        cstmt.setString(3, rezolvare);
        cstmt.execute();
        String result= cstmt.getString(1);
        System.out.print(result);

    }
    public static void main(String[] args) {
        try {
            Database db = Database.getInstance();
            Main m = new Main();
            executePLSQLFunction();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}