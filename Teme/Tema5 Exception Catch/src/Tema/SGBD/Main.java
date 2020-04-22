package Tema.SGBD;

import java.sql.Date;

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

    public static void main(String[] args)  {
     try{
         Database db = Database.getInstance();
         Main m = new Main();
         String query= "INSERT INTO STUDENTI VALUES (?,?,?,?,?,?,?,?,?,?,?)";
         java.sql.Date sqlDate = new java.sql.Date(1);
         PreparedStatement preparedStatement= connection.prepareStatement(query);
         preparedStatement.setInt(1,1); //inseram la id ul 1 pt a crea o exceptie
         preparedStatement.setString(2,"Test");
         preparedStatement.setString(3,"Test");
         preparedStatement.setString(4,"Test");
         preparedStatement.setInt(5,1);
         preparedStatement.setString(6,"A4");
         preparedStatement.setInt(7,300);
         preparedStatement.setDate(8,sqlDate);
         preparedStatement.setString(9,"ceva@cv.com");
         preparedStatement.setDate(10,sqlDate);
         preparedStatement.setDate(11,sqlDate);
         Statement stmt = connection.createStatement();
         stmt.executeQuery(query);

     }catch(SQLException e){
         System.out.println("Query-ul este invalid, operatia de insert nu a avut loc.Eroarea este" +
                 "de tipul: ORA-01008: not all variables bound. Cautati pe google rezolvarea.");

     }
    }
}