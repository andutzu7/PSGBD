package Tema.SGBD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;


class Main {
    private static Connection connection = null;

    Main() {
        try {
            connection = Database.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String executePLSQLFunction(String email, String rezolvare) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{?= call urmatoarea_intrebare(?,?)}");
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.setString(2, email);
        cstmt.setString(3, rezolvare);
        cstmt.execute();
        return cstmt.getString(1);
    }

    public static String generateProblemText(HashMap<String, List<String>> input) throws SQLException {
        List<String> result = new ArrayList<>();
        String query = "select TEXT_INTREBARE,TEXT_RASPUNS from INTREBARI i join RASPUNSURI r on i.ID = r.Q_ID WHERE R.ID=?";
        for (var entry : input.entrySet()) {
            String k = entry.getKey();
            result.add(k);
            break;
        }
        boolean okay = true;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (String i : input.get("Variante")) {
            preparedStatement.setString(1, i);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            if (okay) {
                result.add(rs.getString(1));
                okay = false;
            }
            result.add(i);
            result.add(rs.getString(2));

        }
        System.out.println(result);
        StringBuilder enunt = new StringBuilder("Intrebarea numarul " + result.get(0) + '\n');
        for (int i = 1; i < result.size(); i++) {

            enunt.append(result.get(i));
            if (i % 2 == 1) {
                enunt.append('\n');

            } else {

                enunt.append(' ');
            }

        }

        return enunt.toString();
    }

    //1:A75,A80,A77,A81,A74,A78, Raspunsuri: A75,A77,A78
    public static HashMap<String, List<String>> parseAnswer(String dbmsAnswer) {
        //unde facem joinuri n stuff ca sa generam enuntul
        //stringul e de forma question_id: answers (si pastrez intr un array valorile corecte
        HashMap<String, List<String>> result = new HashMap<>();
        String idIntrebare = dbmsAnswer.split(":")[0];
        result.put(idIntrebare, new ArrayList<>()); //e wasteful aici da n am csf
        String afterDots = dbmsAnswer.split(":")[1];
        String[] split = afterDots.split(",");
        List<String> variants = new ArrayList<>(Arrays.asList(split).subList(0, split.length - 1));
        result.put("Variante", variants);
        String answers = dbmsAnswer.split(":")[2];
        split = answers.split(",");
        List<String> rez = new ArrayList<>(Arrays.asList(split));
        result.put("Raspunsuri", rez);
        return result;
    }

    public static void main(String[] args) throws Exception {

        Database db = Database.getInstance();
        Main m = new Main();
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
        boolean over = false;
        while (!over) {

           /* String result = executePLSQLFunction("diana@uaic.ro", "");

            String str, str1;
            str = br.readLine();
            HashMap<String, List<String>> answer = parseAnswer(str);
            System.out.println(result);
            String problemText = generateProblemText(answer);
            System.out.println(problemText);
            str1 = kb.readLine();
            ps.println(problemText);
       */
        }

        ps.close();
        br.close();
        kb.close();
        ss.close();
        s.close();

        System.exit(0);
    }
/*
        Database db = Database.getInstance();
        Main m = new Main();
  */
}
