package student.adventure.api;

import student.adventure.Player;
import student.adventure.Utils;

import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Class that talks to database
 */
public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";  //URL of adventure.db
    private final Connection dbConnection; //connection used to connect to that db
    private Statement statement; //statement used to run SQL code

    /**
     * Constructor for DatabaseConnection class
     * @throws SQLException
     */
    public DatabaseConnection() throws SQLException {
       dbConnection = DriverManager.getConnection(DATABASE_URL);
    }

    /**
     * Adds player data to table
     * @param player Player object whose data is getting added
     * @throws SQLException
     */
    public void addPlayer(Player player) throws SQLException {
        statement = dbConnection.createStatement();
        statement.execute("insert into leaderboard_adithya9 (name, score) values " +
                "(\""+player.getName()+"\", "+player.getInventory().size()+")");
    }

    //code below from:
    //https://stackoverflow.com/questions/55325884/convert-resultset-into-map
    /**
     * Main method for getting the leaderboard back
     * @return the sorted Leaderboard
     * @throws SQLException
     */
    public Map<String, Integer> getLeaderBoard() throws SQLException {
        Map<String, Integer> leaderboard = new TreeMap<>();
        ResultSet resultSet = selectResultSet();
        while(resultSet.next()){
            String name = resultSet.getString(1);
            String score = resultSet.getString(2);
            leaderboard.put(name, Integer.parseInt(score));
        }
        return Utils.sort(leaderboard);
    }

    /**
     * @return ResultSet object from Select SQL statement
     * @throws SQLException
     */
    private ResultSet selectResultSet() throws SQLException {
        statement = dbConnection.createStatement();
        statement.execute("select name, score from leaderboard_adithya9");
        return statement.getResultSet();
    }
}
