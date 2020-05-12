package Tema.SGBD;

import java.io.*;
import java.net.*;
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
    public static String executePLSQLFunction(String email,String rezolvare) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{?= call urmatoarea_intrebare(?,?)}");
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.setString(2, email);
        cstmt.setString(3, rezolvare);
        cstmt.execute();
        return cstmt.getString(1);
    }

    public static String decryptStringAnswer(String dbmsAnswer){
        //unde facem joinuri n stuff ca sa generam enuntul

        return "";
    }
    public static void main(String []args )
            throws Exception
    {
        Database db = Database.getInstance();
        Main m = new Main();
        String result=executePLSQLFunction("diana@uaic.ro","");
        System.out.println(result);
        connection.close();

        System.out.println("Listening on port 888");

        ServerSocket ss = new ServerSocket(888);

        Socket s = ss.accept();
        System.out.println("Connection established");

        PrintStream ps
                = new PrintStream(s.getOutputStream());

        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        BufferedReader kb
                = new BufferedReader(
                new InputStreamReader(System.in));

        while (true) {

            String str, str1;

            while ((str = br.readLine()) != null) {
                System.out.println(str);
                str1 = kb.readLine();
                ps.println(str1);
            }

            ps.close();
            br.close();
            kb.close();
            ss.close();
            s.close();

            System.exit(0);
        }
    }
}