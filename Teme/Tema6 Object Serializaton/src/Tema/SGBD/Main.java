package Tema.SGBD;

import javax.swing.*;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;

class Main {
    private static Connection connection = null;

    Main() {
        try {
            connection = Database.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createSerializedTable() {

        try {
            Statement stmt = connection.createStatement();
            String query = "create table SerializedTable(" +
                    "    id_student integer not null," +
                    "    hashed_string varchar2(1000) not null ," +
                    "    primary key (id_student)" +
                    ")";
            stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Student initStudent() {
        Student s = new Student();
        s.nume = "IWILLBE";
        s.prenume = "SERIALIZED";
        s.numarMatricol = "T3STTT";
        s.an = 3;
        s.grupa = "A4";
        return s;
    }

    public static ByteArrayOutputStream serializeStudent(Student s) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(s);
        so.flush();
        return bo;
    }

    public static Student deserializeStudent(ByteArrayOutputStream serializedObject) throws IOException, ClassNotFoundException {
        byte[] b = serializedObject.toByteArray();
        InputStream in = new ByteArrayInputStream(b);
        ObjectInputStream obin = new ObjectInputStream(in);
        Object object = obin.readObject();
        //Student s = (Student) si.readObject();
        System.out.println((Student) object);
        return null;
    }

    public static void insertSerializedStudent(int id, ByteArrayOutputStream b) throws SQLException {
        String query = "INSERT INTO SERIALIZEDTABLE values(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, b.toString());
        preparedStatement.executeQuery();
    }

    public static Student readSerializedStudent(int id) throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT * FROM SERIALIZEDTABLE WHERE id_student =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        String sResult = new String(Base64.getEncoder().encode(rs.getString(1).getBytes()));
        sResult = rs.getString(2);
        byte[] b = Base64.getMimeDecoder().decode(sResult.getBytes());
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.writeBytes(b);
        Student s = deserializeStudent(result);
        return s;
    }


    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Database db = Database.getInstance();
        Main m = new Main();
        Student s = initStudent();
        //createSerializedTable();
        ByteArrayOutputStream b = serializeStudent(s);
        System.out.println("Datele studentului serializate: " + b.toString());
        //insertSerializedStudent(1,b);
        Student s2 = readSerializedStudent(1);
        System.out.println(s2);
    }


}