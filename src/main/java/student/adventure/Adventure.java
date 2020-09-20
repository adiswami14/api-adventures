package student.adventure;

import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Main adventure class that is used to interact with the API
 */
public class Adventure {
    private Player player;  //Player object of this game
    private ArrayList<Room> rooms;  //list of rooms in the game
    private Room currentRoom; //current Room that Player is in, used for brevity
    private GameStatus status; //current status of the Adventure game
    private Command command; //command currently issued by the client
    private int id; //id for this Adventure instance
    private AdventureState state; //AdventureState instance for displaying inventory
    private HashMap<String, List<String>> commandMap; //map for commands

    /**
     * Constructor for the API adventure class
     */
    public Adventure(int id) {
        JSONReader reader = null;

        try { //made a try-catch block to not interfere with AdventureService method's signature
            reader = new JSONReader(new File("src/main/resources/data/Data.json"));
            this.rooms = reader.getGame().getRooms();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.id = id;
        initializePlayer();
        commandMap = new HashMap<>();
        state = new AdventureState(this);
        status = new GameStatus(false, id, "", null, null,
               state, commandMap);
        status.setMessage("Hey there!");  //basic starting message
        command = new Command();

        run();
    }

    /**
     * @return Player object of the game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets current status of the game
     * @return GameStatus object of this game
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * @return id of the game
     */
    public int getId(){
        return id;
    }

    /**
     * Sets command given by client to current command issued to the game
     * @param command the command passed in by the client
     */
    public void setCommand(Command command) {
        this.command = command;
        run(); //acts as a loop — ran every time a new command is called
    }

    /**
     * @return current command issued by client
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Method that is called to start the game
     */
    public void run(){
        handleUserInput();
        updateCommands();
        updateAdventureState();
        player.setName(command.getPlayerName()); //reset name to the name of the player given by command
        status = new GameStatus(false, id, status.getMessage(), null,
                null, state, commandMap);
    }

    /**
     * Initializes basic player fields such as name and currentRoom
     */
    private void initializePlayer(){
        player = new Player("Placeholder");
        player.setCurrentRoom(rooms.get(0));
        currentRoom = player.getCurrentRoom();
        player.getRoomHistory().add(currentRoom.getName()); //adds starting room to room history
    }

    /**
     * Aligns with examine() method in assignment description
     */
    private void examine() {
        if(currentRoom == null) {
            throw new IllegalArgumentException();
        }
        status.setMessage(currentRoom.getDescription());
    }

    /**
     * Runs certain functions according to the string passed in
     */
    private void handleUserInput() {
        Method method;

        try {   //checks for methods w/ String params
            method = this.getClass().getDeclaredMethod(command.getCommandName(), String.class);
            method.setAccessible(true);
            method.invoke(this, command.getCommandValue());
        } catch(Exception e) {
            try { //this is for methods that don't take in any params (i.e. examine(), quit())
                method = this.getClass().getDeclaredMethod(command.getCommandName(), null);
                method.setAccessible(true);
                method.invoke(this);
            } catch (Exception exception) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Player attempts to go in a certain direction
     * @param directionName the name of the direction the user enters
     */
    private void go(String directionName) {
        for(Direction direction : currentRoom.getPossibleDirections()) {

            if(direction.getName().toLowerCase().equals(directionName.toLowerCase())) {
                //checks lowercase in all command methods because we want to be able to print out exactly
                //what the user said, instead of altering the command and/or subject string
                for(Room room : rooms) {
                    if(direction.getConnectingRoom().equals(room.getName())) {
                        player.setCurrentRoom(room);
                        currentRoom = player.getCurrentRoom();
                        player.getRoomHistory().add(currentRoom.getName()); //adds room to room history list
                        examine();
                        return;
                    }
                }
            }
        }
    }

    /**
     * Player attempts to drop a certain item
     * @param itemName name of the item the user enters
     */
    private void drop(String itemName) {
        for(Item item : player.getInventory()) {
            if(item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                currentRoom.getItems().add(item);
                player.getInventory().remove(item);
                return;
            }
        }
    }

    /**
     * Player attempts to take a certain item
     * @param itemName name of the item the user enters
     */
    private void take(String itemName) {
        for(Item item : currentRoom.getItems()) {
            if(item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                player.getInventory().add(item);
                currentRoom.getItems().remove(item);
                return;
            }
        }
        //ioHandler.println("There is no item \""+itemName+"\" in this room!");
    }

    /**
     * Function that gives distance to room that is typed in
     */
    private void distanceTo(String roomName) {
        Location currentLoc = currentRoom.getLocation();

        for(Room room : rooms) {
            if(room.getName().toLowerCase().equals(roomName.toLowerCase())) {
                status.setMessage("The distance between the current room and the "+roomName+" is "
                        +Utils.findDistance(currentLoc, room.getLocation())+" meters");
                return;
            }
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
        for(Direction d: currentRoom.getPossibleDirections()) {
            directions.add(d.getName());
        }
        commandMap.put("go", directions);
    }

    /**
     * Keeps track of buttons for possible items to take
     */
    private void updateCommandsTake() {
        ArrayList<String> items = new ArrayList<>();
        for(Item item: currentRoom.getItems()) {
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
        for(Room room : rooms) {
            roomNames.add(room.getName());
        }
        commandMap.put("distanceTo", roomNames);
    }

    /**
     * update AdventureState instance by updating inventory and room history changes
     */
    private void updateAdventureState() {
        state = new AdventureState(this);
    }
}
