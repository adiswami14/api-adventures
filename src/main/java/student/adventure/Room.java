package student.adventure;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Room {
    private String name;
    private String description;
    private ArrayList<Item> items;
    @SerializedName("directions")
    private ArrayList<Direction> possibleDirections;
    private Location location;

    public Room() {
    }

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
}
