package student.server;

/**
 * Holds information about a command issued by a client. Includes the command's name
 * and arguments.
 */
public class Command {
    /**
     * The string representing the command's action (e.g.: "go", "take", "attend").
     */
    private String commandName;
    /**
     * The argument to the command (e.g.: "North", "sweatshirt", "CS 126 Lecture").
     */
    private String commandValue;
    /**
     * The name of the player who issued this command. Included so you can track them in the leaderboard
     * if this command would win them the game.
     */
    private String playerName;

    /**
     * Default constructor for Command class
     */
    public Command() {

    }

    /**
     * Main constructor for Command class
     * @param commandName the directive of the command (i.e. "go", "drop")
     * @param commandValue the subject of the command
     */
    public Command(String commandName, String commandValue) {
        this.commandName = commandName;
        this.commandValue = commandValue;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandValue() {
        return commandValue;
    }

    public String getPlayerName() {
        return playerName;
    }
}
