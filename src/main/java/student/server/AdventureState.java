package student.server;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import student.adventure.Adventure;
import student.adventure.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent values in a game state.
 *
 * Note: these fields should be JSON-serializable values, like Strings, ints, floats, doubles, etc.
 * Please don't nest objects, as the frontend won't know how to display them.
 *
 * Good example:
 * private String shoppingList;
 *
 * Bad example:
 * private ShoppingList shoppingList;
 */
@JsonSerialize
public class AdventureState {
    private List<String> inventory; //list of items in player's possession
    private List<String> history; //room history of player

    /**
     * Constructor of AdventureState class
     * @param adventure instance of game engine
     */
    public AdventureState(Adventure adventure) {
        inventory = new ArrayList<>();
        history = new ArrayList<>();
        inventoryToString(adventure.getPlayer().getInventory());
        historyToString(adventure.getPlayer().getRoomHistory());
    }

    /**
     * @return list of item names in Player inventory
     */
    public List<String> getInventory() {
        return inventory;
    }

    /**
     * @return list of room names in player roomHistory list
     */
    public List<String> getHistory() {
        return history;
    }

    /**
     * Converts values of lists to strings for testing
     * @return string containing all the values of inventory and history lists
     */
    public String toString() {
        return inventory.toString()+history.toString();
    }

    /**
     * Converts item elements in inventory to String
     * @param itemList inventory of Player
     */
    private void inventoryToString(List<Item> itemList) {
        for(int itemIndex =0;itemIndex<itemList.size();itemIndex++) {
            if(itemIndex < itemList.size()-1) {
                inventory.add(itemList.get(itemIndex).getName()+", "); //formats with commas on the actual UI
            } else inventory.add(itemList.get(itemIndex).getName());
        }
    }

    /**
     * Converts of room elements in RoomHistory to String
     * @param roomList roomHistory of Player
     */
    private void historyToString(List<String> roomList) {
        for(int roomIndex = 0; roomIndex<roomList.size();roomIndex++) {
            if(roomIndex < roomList.size()-1) {
                history.add(roomList.get(roomIndex)+", ");
            } else history.add(roomList.get(roomIndex));
        }
    }

}
