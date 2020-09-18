package student.adventure;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private ArrayList<Room> rooms;

    public Game(){
    }

    /**
     * @return the arraylist of Room objects given by the JSON Data
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

}
