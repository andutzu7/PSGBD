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

    public static void unfilteredQuery() throws SQLException {
        String query = "SELECT * FROM STUDENTI WHERE id = 105 OR 1=1"; //Un query faulty care permite injectii
        Statement stmt = connection.createStatement();// nu este aruncata nici o exceptie
        System.out.println("Query-ul faulty a fost executat fara nici o problema.");
    }
    public static boolean isAlphaOnly(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
    public static boolean checkExpressionValidity(String expression) {
        //evitam cazurile de tipul 1=1 sau alte comenzi de tipul --\ (insert injection here ) verificand daca partea din
        //stanga a ecuatiei este copusa doar din literali
        String[] parts = expression.split("[.!?\\\\-]=");
        if(!isAlphaOnly(parts[0])){
            return false;
        }
        return true;
    }

    public static void filteredQuery(int id, String orExpression) throws SQLException {
        String query = "SELECT * FROM STUDENTI WHERE id = ? OR ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);//java si oracle au grija ca indexul sa fie valid
        if (checkExpressionValidity(orExpression)) {
            preparedStatement.setString(2, orExpression);
            preparedStatement.executeQuery();
        }
        else{
            System.out.println("Valoarea introdusa pentru expresia OR este invalida");
        }

    }


    public static void main(String[] args) throws SQLException {
        Database db = Database.getInstance();
        Main m = new Main();
        //unfilteredQuery();
        filteredQuery(1, "1=1");
    }

}