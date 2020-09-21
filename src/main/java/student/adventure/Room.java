package student.adventure;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Class that stores Room objects, represents a single room in the game
 */
public class Room {
    private String name; //name of the Room
    private String description; //sentence describing this Room object
    private boolean isEndingRoom; //boolean on whether this Room object ends the game or not
    private ArrayList<Item> items; //list of items in the room
    @SerializedName("directions")
    private ArrayList<Direction> possibleDirections; //possible directions available to Player from this Room
    private Location location; //Location instance of the room

    /**
     * @return the name of the Room
     */
    public String getName() {
        if(name == null) {
            throw new IllegalArgumentException();
        }
        return name;
    }

    /**
     * @return the description of the Room
     */
    public String getDescription() {
        if(description == null) {
            throw new IllegalArgumentException();
        }
        return description;
    }

    /**
     * @return the list of items in the Room
     */
    public ArrayList<Item> getItems() {
        if(items == null) {
            throw new IllegalArgumentException();
        }
        return items;
    }

    /**
     * @return the possible Directions the Player can go from this specific Room
     */
    public ArrayList<Direction> getPossibleDirections() {
        if(possibleDirections == null) {
            throw new IllegalArgumentException();
        }
        return possibleDirections;
    }

    /**
     * @return the location of the specific room
     */
    public Location getLocation() {
        if(location == null) {
            throw new IllegalArgumentException();
        }
        return location;
    }

    /**
     * @return whether this specific Room object is the ending room of the game
     */
    public boolean isEndingRoom() {
        return isEndingRoom;
    }
}
