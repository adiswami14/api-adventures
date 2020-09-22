package student.adventure;

import student.server.AdventureState;
import student.server.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class made for the sole purpose of updating variables in game engine
 */
public class Updater {
    private Adventure adventure; //instance of game engine
    private Player player; //player instance of the game engine
    private HashMap<String, List<String>> commandMap; //list of possible commands from the game engine

    /**
     * Constructor of the Updater class
     */
    public Updater(Adventure adventure) {
        this.adventure = adventure;
        player = adventure.getPlayer();
        commandMap = adventure.getCommandMap();
    }

    /**
     * Updates commands, adventure state, and game status of the game
     */
    public void update() {
        updateCommands();
        updateAdventureState();
        adventure.setStatus(new GameStatus(false, adventure.getId(), adventure.getStatus().getMessage(),
                null, null, adventure.getState(), adventure.getCommandMap()));

        if(!adventure.isCLI()) {
            adventure.getPlayer().setName(adventure.getCommand().getPlayerName());
            //reset name to the name of the player given by UI command
        }
    }

    /**
     * Updates command buttons of current game
     */
    private void updateCommands() {
        updateCommandsGo();
        updateCommandsTake();
        updateCommandsDrop();
        updateCommandsExamine();
        updateCommandsDistanceTo();
    }

    /**
     * Keeps track of buttons for possible directions to go in
     */
    private void updateCommandsGo() {
        ArrayList<String> directions = new ArrayList<>();
        for(Direction d: player.getCurrentRoom().getPossibleDirections()) {
            directions.add(d.getName());
        }
        commandMap.put("go", directions);
    }

    /**
     * Keeps track of buttons for possible items to take
     */
    private void updateCommandsTake() {
        ArrayList<String> items = new ArrayList<>();
        for(Item item: player.getCurrentRoom().getItems()) {
            items.add(item.getName());
        }
        commandMap.put("take", items);
    }

    /**
     * Keeps track of buttons for possible items to drop
     */
    private void updateCommandsDrop() {
        ArrayList<String> items = new ArrayList<>();
        for(Item item: player.getInventory()) {
            items.add(item.getName());
        }
        commandMap.put("drop", items);
    }

    /**
     * Button for examining current room
     */
    private void updateCommandsExamine() {
        ArrayList<String> empty = new ArrayList<>();
        empty.add("");
        commandMap.put("examine", empty);
    }

    /**
     * Keeps track of buttons for possible rooms to measure distance to
     */
    private void updateCommandsDistanceTo() {
        ArrayList<String> roomNames = new ArrayList<>();
        for(Room room : adventure.getRooms()) {
            roomNames.add(room.getName());
        }
        commandMap.put("distanceTo", roomNames);
    }

    /**
     * Update AdventureState instance by updating inventory and room history changes
     */
    private void updateAdventureState() {
        adventure.setState(new AdventureState(adventure));
    }
}
