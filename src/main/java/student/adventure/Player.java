package student.adventure;

import java.util.ArrayList;

public class Player {
    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private ArrayList<String> roomHistory;

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

    public ArrayList<String> getRoomHistory() {
        return roomHistory;
    }
}
