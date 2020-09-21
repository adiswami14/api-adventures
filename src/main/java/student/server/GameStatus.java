package student.server;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An object representing the current state of a game instance.
 */
public class GameStatus {
    /**
     * Whether or not this response is an error state.
     * Note: this should not be `true` for most cases. This should only be `true` if an exception was
     * thrown by the game engine.
     */
    private boolean error;
    /**
     * The instance ID associated with this GameStatus.
     * This field is required, and cannot be null.
     */
    private int id;
    /**
     * A text message to display to the user.
     * This field is required, and cannot be null.
     */
    private String message;
    /**
     * A URL of an image to display to the user.
     */
    private String imageUrl;
    /**
     * A YouTube video link to play for the user.
     */
    private String videoUrl;
    /**
     * An object (that you may modify) that contains values represented by the game's state.
     * E.g.: life total, # items in inventory, etc.
     * This field is required, and cannot be null. However, the AdventureState class can be empty if you don't need it.
     */
    private AdventureState state;
    /**
     * An mapping of commands to possible arguments for those commands. This will be used to create buttons on the
     * frontend, so it should be full of the possible options for the user.
     * E.g.: "go" -> ["up", "north", "down"]
     *       "examine" -> [] (need an empty list for no arguments)
     *       "answer" -> ["A", "B", "C", "D"] (for a trivia-like custom feature)
     * This field is required, and cannot be null.
     */
    private Map<String, List<String>> commandOptions;

    /**
     * Constructor for GameStatus class
     */
    public GameStatus(boolean error, int id, String message, String imageUrl, String videoUrl, AdventureState state,
                      Map<String, List<String>> commandOptions) {
        this.error = error;
        this.id = id;
        this.message = message;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.state = state;
        this.commandOptions = commandOptions;
    }

    public boolean isError() {
        return error;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Changes message to what one wants it to be
     * @param message the message to set the current message to
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public AdventureState getState() {
        return state;
    }

    /**
     * @param state AdventureState instance to set current state to
     */
    public void setState(AdventureState state) {
        this.state = state;
    }

    public Map<String, List<String>> getCommandOptions() {
        return commandOptions;
    }

    /**
     * @param o other Object to compare to
     * @return whether an object equals another
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameStatus that = (GameStatus) o;
        return toString().equals(that.toString());
    }

    /**
     * Automatically comes with the equals() method
     */
    @Override
    public int hashCode() {
        return Objects.hash(error, id, message, imageUrl, videoUrl, state, commandOptions);
    }

    /**
     * toString method to test all elements of this class
     * @return A string of all elements in the gameStatus class
     */
    public String toString() {
        String returnString = error + ", " + id + ", " + message + ", " + state + ", ";

        for (Map.Entry<String, List<String>> entry : commandOptions.entrySet()) {
            for(String string : entry.getValue()) {
                returnString+=string; //individually adds all values to the string to account for hashmap values
            }
        }

        return returnString.trim();
    }
}
