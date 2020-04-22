package Tema.SGBD;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


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

    public static void createTablePrieteni() {

        try {
            Statement stmt = connection.createStatement();
            String query = "create table Prieteni(" +
                    "    ID integer not null," +
                    "    id_pers integer not null," +
                    "    id_prieten integer not null," +
                    "    Nume varchar(100)," +
                    "    Prenume varchar(100)," +
                    "    primary key (ID)" +
                    ")";
            stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Idee pt populare: Pt un numar dat inainte de persoane selectam random cateva persoane din tabela studenti
    //si le adaugam ca prieten pt un id.Intr-un final fiecare persoana va avea acelasi numar de prieteni
    public static void populateFriendsTable(int howMany, int nrOfFriends) throws SQLException {
        Random random = new Random();
        String insertQuery = "INSERT INTO Prieteni values(?,?,?,?,?)";
        String getDataQuery = "SELECT nume,prenume FROM Studenti WHERE id = ?";
        int indexBound = 50;
        int id = 1;
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

        PreparedStatement getDataStatement = connection.prepareStatement(getDataQuery);

        for (int i = 1; i <= howMany; i++) {
            List<Integer> visitedIndexes = new ArrayList<>();
            visitedIndexes.add(i);
            for (int j = 0; j < nrOfFriends; j++) {
                int generatedIndex = random.nextInt(indexBound) + 1;//generam indexi de la 1 la bound
                while (visitedIndexes.contains(generatedIndex)) {
                    generatedIndex = random.nextInt(indexBound) + 1;
                }
                visitedIndexes.add(generatedIndex);
                getDataStatement.setInt(1, generatedIndex);
                ResultSet rs = getDataStatement.executeQuery();
                rs.next();
                String nume = rs.getString(1);
                String prenume = rs.getString(2);
                insertStatement.setInt(1, id++);
                insertStatement.setInt(2, i);
                insertStatement.setInt(3, generatedIndex);
                insertStatement.setString(4, nume);
                insertStatement.setString(5, prenume);
                insertStatement.executeQuery();
            }
        }
    }

    public static void printMap(Map<Integer, Integer> board) {
        Set<Map.Entry<Integer, Integer>> set = board.entrySet();//Converting to Set so that we can traverse
        Iterator<Map.Entry<Integer, Integer>> itr = set.iterator();
        while (itr.hasNext()) {
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry<Integer, Integer> entry = itr.next();
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static JSONObject getSuggestedList(Map<Integer, Integer> map) throws SQLException {
        JSONArray result = new JSONArray();
        String getDataQuery = "SELECT nume,prenume FROM Studenti WHERE id = ?";

        PreparedStatement insertStatement = connection.prepareStatement(getDataQuery);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            JSONObject friend = new JSONObject();
            insertStatement.setInt(1,entry.getKey());
            ResultSet rs=insertStatement.executeQuery();
            rs.next();
            friend.put("Nume ", rs.getString(1));
            friend.put("Prenume",rs.getString(2));
            result.add(friend);
        }
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("Prieteni sugerati",result);
        return jsonObject;
    }


    public static Map.Entry<Integer, Integer> getMaximumFromMap(Map<Integer, Integer> map) {
        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    /*
     * Ideea functiei: selectam toti prietenii pe care personID ii are. Apoi selectam prietenii fiecaruia dintre
     * acestia si ii adaugam intr un map pe care il adaugam intr-o lista, creeind un clasament
     *  La final jsonificam rezultatul si il printam in consola.
     * */
    public static void suggestFriends(int personId, int sFrListLen) throws SQLException {
        String getFriendsQuery = "SELECT id_prieten FROM Prieteni WHERE id_pers = ?";
        PreparedStatement getDataStatement = connection.prepareStatement(getFriendsQuery);
        //id prieten, nr aparitii
        Map<Integer, Integer> suggestedBoard = new TreeMap<Integer, Integer>();
        List<Integer> userFriendsIds = new ArrayList<>();
        getDataStatement.setInt(1, personId);
        ResultSet rs = getDataStatement.executeQuery();
        //Stocam indexurile fiecarui prieten al userului specificat
        while (rs.next()) {
            userFriendsIds.add(rs.getInt(1));
        }

        for (int i = 0; i < userFriendsIds.size(); i++) {
            getDataStatement.setInt(1, userFriendsIds.get(i));
            rs = getDataStatement.executeQuery();
            while (rs.next()) {
                int value;
                //daca nu se afla in lista
                if (!userFriendsIds.contains(rs.getInt(1))) {
                    if (suggestedBoard.containsKey(rs.getInt(1))) {
                        value = suggestedBoard.get(rs.getInt(1));

                        suggestedBoard.put(rs.getInt(1), value + 1);
                    } else {
                        suggestedBoard.put(rs.getInt(1), 1);
                    }

                }

            }
        }  
        Map<Integer, Integer> friendsList = new TreeMap<Integer, Integer>();
        for (int i = 0; i < sFrListLen; i++) {
            var currentMax = getMaximumFromMap(suggestedBoard);
            friendsList.put(currentMax.getKey(), currentMax.getValue());
            suggestedBoard.remove(currentMax.getKey());
        }
        JSONObject result = getSuggestedList(friendsList);
        System.out.println(result);
    }

    public static void main(String[] args) throws SQLException {
        Database db = Database.getInstance();
        Main m = new Main();
        //createTablePrieteni();
        //Pentru 10 studenti creem o lista de prieteni cu 20 de persoana
        //populateFriendsTable(10,20);
        suggestFriends(1, 5);
        connection.close();
    }
}