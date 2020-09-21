package student.server;

import student.adventure.Adventure;
import student.adventure.DatabaseConnection;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdventureGameService implements AdventureService{
    int gameId = 0; //assigns a new id to each game
    Map<Integer, Adventure> gameMap = new HashMap<>(); //maps each Adventure instance to its own id

    /**
     * Resets the service to its initial state.
     */
    @Override
    public void reset() {
        gameMap.clear();
        gameId =0;
    }

    /**
     * Creates a new Adventure game and stores it.
     *
     * @return the id of the game.
     */
    @Override
    public int newGame() throws AdventureException {
        Adventure adventure = new Adventure(gameId);
        gameMap.put(gameId, adventure);
        gameId++;
        return adventure.getId();
    }

    /**
     * Returns the state of the game instance associated with the given ID.
     *
     * @param id the instance id
     * @return the current state of the game
     */
    @Override
    public GameStatus getGame(int id) {
        if(gameMap.containsKey(id)) {
            return gameMap.get(id).getStatus();
        }
        return null;
    }

    /**
     * Removes & destroys a game instance with the given ID.
     *
     * @param id the instance id
     * @return false if the instance could not be found and/or was not deleted
     */
    @Override
    public boolean destroyGame(int id) {
        if(gameMap.containsKey(id)) {
            gameMap.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Executes a command on the game instance with the given id, changing the game state if applicable.
     *
     * @param id      the instance id
     * @param command the issued command
     */
    @Override
    public void executeCommand(int id, Command command) {
        if(command == null) {
            throw new IllegalArgumentException();
        }
        if(gameMap.containsKey(id)) {
            gameMap.get(id).setCommand(command);
        }
    }

    /**
     * Returns a sorted leaderboard of player "high" scores.
     *
     * @return a sorted map of player names to scores
     */
    @Override
    public Map<String, Integer> fetchLeaderboard() {
        try {  //establishes a connection to the adventure.db
            DatabaseConnection databaseConnection = new DatabaseConnection();
            return databaseConnection.getLeaderBoard();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null; //return null otherwise
    }

    /**
     * @return gameId variable
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @return the map that maps id to Adventure game instance
     */
    public Map<Integer, Adventure> getGameMap() {
        return gameMap;
    }
}
