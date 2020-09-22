package student.adventure;

import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * Main adventure class that is used to interact with the API (also works with CLI)
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
    private student.adventure.DatabaseConnection dbConnection; //instance of DatabaseConnection to talk to database
    private student.adventure.IOHandler ioHandler; //IOHandler instance for game if ran on command line
    private Scanner scanner; //scanner for it the game is ran on command line
    private boolean isCli = true; //true if cli is running, false if api is running

    /**
     * Constructor for the API adventure class
     */
    public Adventure(int id, InputStream inputStream, OutputStream outputStream) {
        if(inputStream == null && outputStream == null) {
            //ensures that there are no IO streams — hence it is run on UI
            isCli = false;
        }
        JSONReader reader = null;

        try { //made a try-catch block to not interfere with AdventureService method's signature
            reader = new JSONReader(new File("src/main/resources/data/Data.json"));
            this.rooms = reader.getGame().getRooms();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialization of multiple variables
        initializeGame(id, inputStream, outputStream);
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
    public void setCommand(Command command){
        this.command = command;
        run(); //acts as a loop — ran every time a new command is called
    }

    /**
     * Method for quitting the program
     * Seems unnecessary, but it improves readability
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * @return current command issued by client
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Checks if the player is in the final room
     */
    public boolean isGameOver() {
        return currentRoom.isEndingRoom();
    }

    /**
     * Method that is called to loop through the game
     */
    public void run(){
        //examine();
        if(!isGameOver()) {
            handleUserInput();
            update();  //updates all of the commands and AdventureState instance
            if(isCli && scanner.hasNextLine()) {  //if CLI is running and if scanner still has input left
                setCommand(ioHandler.convertStringToCommand(scanner.nextLine()));
            }
        } else {
            try {
                dbConnection.addPlayer(getPlayer());  //adds player to the database
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Initializes basic player fields such as name and currentRoom
     */
    private void initializePlayer(){
        if(isCli) {
            ioHandler.println("Hey player, what's your name: ");
            player = new Player(scanner.nextLine().trim());
            ioHandler.print("Hey, " + player.getName() + "! ");
        }
        else player = new Player("Placeholder");
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
        examineDirections(currentRoom);
        examineItems(currentRoom);
    }

    /**
     * Examines possible directions for a given room
     * @param room room we are examining
     */
    private void examineDirections(Room room) {
        ioHandler.println(room.getDescription());  //String we will return for this method
        ioHandler.print("From here, you can go: ");

        ArrayList<Direction> directionsInRoom = room.getPossibleDirections();

        for(int directionIndex = 0; directionIndex<directionsInRoom.size();directionIndex++) {
            ioHandler.print(directionsInRoom.get(directionIndex).getName());
            if(directionIndex != directionsInRoom.size()-1) {
                ioHandler.print(", ");
            }
        }
        ioHandler.println();
    }

    /**
     * Examines possible Items for a given room
     * @param room room we are examining
     */
    private void examineItems(Room room) {
        ioHandler.print("Items: ");
        ArrayList<Item> itemsInRoom = room.getItems();

        for(int itemIndex = 0; itemIndex<itemsInRoom.size();itemIndex++) {
            ioHandler.print(itemsInRoom.get(itemIndex).getName());
            if(itemIndex != itemsInRoom.size()-1) {
                ioHandler.print(", ");
            }
        }
        ioHandler.println();
    }

    /**
     * Runs certain functions according to the string passed in
     */
    private void handleUserInput() {
        Method method;
        if(command.getCommandName() == null) return;
        try {   //checks for methods w/ String params
            method = this.getClass().getDeclaredMethod(command.getCommandName(), String.class);
            method.setAccessible(true);
            method.invoke(this, Utils.formatString(command.getCommandValue()));
        } catch(Exception e) {
            try { //this is for methods that don't take in any params (i.e. examine(), quit())
                method = this.getClass().getDeclaredMethod(command.getCommandName(), null);
                method.setAccessible(true);
                method.invoke(this);
            } catch (Exception exception) {
                ioHandler.println("I don't understand the command \""+command.getCommandName()+"\"!");
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
        ioHandler.println("I can't go \""+directionName+"\"!");
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
        ioHandler.println("You don't have \""+itemName+"\" in your inventory!");
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
        ioHandler.println("There is no item \""+itemName+"\" in this room!");
    }

    /**
     * Function that formats and prints out room history of player
     */
    private void history() {
        ioHandler.print("Your room history: ");
        ArrayList<String> playerRoomHistory = player.getRoomHistory();

        for(int roomIndex = 0; roomIndex<playerRoomHistory.size();roomIndex++) {
            ioHandler.print(playerRoomHistory.get(roomIndex));
            if(roomIndex != playerRoomHistory.size()-1) {
                ioHandler.print(", ");
            }
        }
        ioHandler.println();
    }

    /**
     * Function that gives distance to room that is typed in
     */
    private void distanceTo(String roomName) {
        Location currentLoc = currentRoom.getLocation();

        for(Room room : rooms) {
            if(room.getName().toLowerCase().equals(roomName.toLowerCase())) {
                status.setMessage("The distance between the current room and the "+room.getName()+" is "
                        +Utils.findDistance(currentLoc, room.getLocation())+" meters");
                ioHandler.println("The distance between the current room and the "+roomName+" is "
                        +Utils.findDistance(currentLoc, room.getLocation())+" meters");
                return;
            }
        }
        ioHandler.println("The room name \""+roomName+"\" is invalid");
    }

    /**
     * Updates commands, adventure state, and game status of the game
     */
    private void update() {
        updateCommands();
        updateAdventureState();
        status = new GameStatus(false, id, status.getMessage(), null,
                null, state, commandMap);

        if(!isCli) {
            player.setName(command.getPlayerName()); //reset name to the name of the player given by UI command
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

    /**
     * Sets initial value to all member variables
     * @param id id of the current instance of Adventure game
     */
    private void initializeGame(int id, InputStream inputStream, OutputStream outputStream) {
        if(inputStream == null && outputStream == null) { //if game is being run on UI instead of CLI
            ioHandler = new student.adventure.IOHandler(System.in, System.out);
            scanner = new Scanner(System.in);
        } else {
            ioHandler = new student.adventure.IOHandler(inputStream, outputStream);
            scanner = new Scanner(inputStream);
        }

        this.id = id;
        initializePlayer();
        commandMap = new HashMap<>();
        state = new AdventureState(this);
        status = new GameStatus(false, id, "", null, null,
                state, commandMap);
        status.setMessage("Hey there!");  //basic starting message
        command = new Command();

        try {
            dbConnection = new student.adventure.DatabaseConnection(); //establish a database connection
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
