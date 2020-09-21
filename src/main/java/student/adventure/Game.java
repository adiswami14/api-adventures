package student.adventure;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Wrapper class for the deserialization of JSON data
 */
public class Game {
    private ArrayList<Room> rooms; //list of rooms in the game

    /**
     * @return the arraylist of Room objects given by the JSON Data
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

}
