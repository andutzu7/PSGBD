package Tema.SGBD;

import com.github.javafaker.Faker;

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

    public static void createTableChestionar() {

        try {
            Statement stmt = connection.createStatement();
            String query = "create table Chestionar(" +
                    "    id_student integer not null," +
                    "    Fave_book varchar(100)," +
                    "    Dream_job varchar(100)," +
                    "    Fave_music_genre varchar(100)," +
                    "    Fave_food varchar(100)," +
                    "    Least_favourite_color varchar(100)," +
                    "    primary key (id_student)" +
                    ")";
            stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxIndexOfStudents() throws SQLException {
        Statement stmt = connection.createStatement();
        int max = 0;
        ResultSet rs = stmt.executeQuery("select * from Studenti");
        while (rs.next()) {
            if (max < rs.getInt(1)) {
                max = rs.getInt(1);
            }
        }
        return max;
    }

    public static void insertDummyValues(int howMany) throws SQLException {
        Faker faker = new Faker();
        String query = "INSERT INTO Chestionar values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (int i = 1; i <= howMany; i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, faker.book().title());
            preparedStatement.setString(3, faker.job().title());
            preparedStatement.setString(4, faker.music().genre());
            preparedStatement.setString(5, faker.food().dish());
            preparedStatement.setString(6, faker.color().name());
            preparedStatement.executeQuery();
        }
    }

    public static void main(String[] args) {
        try {
            Database db = Database.getInstance();
            Main m = new Main();
            createTableChestionar();
            int howMany = getMaxIndexOfStudents();
            insertDummyValues(howMany);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}