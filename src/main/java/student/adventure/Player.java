package student.adventure;

import java.util.ArrayList;

/**
 * Player object class
 */
public class Player {
    private String name; //name of the player
    private Room currentRoom; //currentRoom of the player
    private ArrayList<Item> inventory; //list of items the player possesses
    private ArrayList<String> roomHistory; //history of rooms player has traversed

    public Player(String name) {
        if(name == null || name.replaceAll(" ", "").isEmpty()) {
            //checks name isn't all whitespace
            throw new IllegalArgumentException();
        }
        this.name = Utils.formatString(name);
        inventory = new ArrayList<>();
        roomHistory = new ArrayList<>();
    }

    /**
     * @return the name of the Player
     */
    public String getName(){
        return name;
    }

    /**
     * @param name the name to set the name to
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return the current Room of the Player
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * @param currentRoom the new current room of the Player
     */
    public void setCurrentRoom(Room currentRoom){
        if(currentRoom == null) {
            throw new IllegalArgumentException();
        }
        this.currentRoom = currentRoom;
    }

    /**
     * @return the inventory of the Player
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    /**
     * @return history of rooms the Player has traversed
     */
    public ArrayList<String> getRoomHistory() {
        return roomHistory;
    }
}
