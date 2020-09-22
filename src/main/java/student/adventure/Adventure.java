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
 * Game engine class used for updating game status, basically stores all variables for this game
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
    private DatabaseConnection dbConnection; //instance of DatabaseConnection to talk to database
    private IOHandler ioHandler; //IOHandler instance for game if ran on command line
    private Scanner scanner; //scanner for it the game is ran on command line
    private Updater updater; //Updater instance for this game instance
    private boolean isCli = true; //true if cli is running, false if api is running (defaults to true)

    /**
     * Constructor for the API adventure class
     */
    public Adventure(int id, InputStream inputStream, OutputStream outputStream) {
        //initialization of multiple variables
        initializeGame(id, inputStream, outputStream);
        run();
    }

    /**
     * @return list of rooms in this game
     */
    public ArrayList<Room> getRooms() {
        return rooms;
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
     * Sets gameStatus variable to a variable passed in
     * @param status the gameStatus variable to change the current instance to
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * @return id of the game
     */
    public int getId(){
        return id;
    }

    /**
     * @return the AdventureState instance of this game engine
     */
    public AdventureState getState() {
        return state;
    }

    /**
     * @param state the Adventure State value that we are setting the game engine value to
     */
    public void setState(AdventureState state) {
        this.state = state;
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
     * @return current command issued by client
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return the map of commands in this game instance
     */
    public HashMap<String, List<String>> getCommandMap() {
        return commandMap;
    }

    /**
     * Checks if the player is in the final room
     */
    public boolean isGameOver() {
        return currentRoom.isEndingRoom();
    }

    /**
     * Checks if this game instance is being run on command line or through REST API
     */
    public boolean isCLI() {
        return isCli;
    }

    /**
     * Method that is called to loop through the game
     */
    public void run(){
        if(!isGameOver()) {
            updater = new Updater(this); //keep updating updater with current adventure instance
            //keeps updating game with up-to-date variables
            handleUserInput();
            updater.update();  //updates all of the commands and AdventureState instance
            if(isCli && scanner.hasNextLine()) {
                //if CLI is running and if scanner still has input left while game is still running
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
            ioHandler.println("Hey, " + player.getName() + "! ");
        }
        else player = new Player("Placeholder");
        player.setCurrentRoom(rooms.get(0));
        currentRoom = player.getCurrentRoom();
        player.getRoomHistory().add(currentRoom.getName()); //adds starting room to room history
    }

    /**
     * Runs certain functions according to the string passed in
     */
    public void handleUserInput() {
        Method method;
        if (command.getCommandName() == null) {
            return;
        }

        try {   //checks for methods w/ String params
            method = this.getClass().getDeclaredMethod(command.getCommandName(), String.class);
            method.setAccessible(true);
            method.invoke(this, Utils.formatString(command.getCommandValue()));
        } catch (Exception e) {
            try { //this is for methods that don't take in any params (i.e. examine(), quit())
                method = this.getClass().getDeclaredMethod(command.getCommandName(), null);
                method.setAccessible(true);
                method.invoke(this);
            } catch (Exception exception) {
                ioHandler.println("I don't understand the command \"" + command.getCommandName() + "\"!");
            }
        }
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
     * Function that gives distance to room that is typed in, solely for UI
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
     * Method for quitting the program
     * Seems unnecessary, but it improves readability
     */
    private void quit() {
        System.exit(0);
    }

    /**
     * Sets initial value to all member variables
     * @param id id of the current instance of Adventure game
     */
    private void initializeGame(int id, InputStream inputStream, OutputStream outputStream) {
        if(inputStream == null && outputStream == null) {
            //ensures that there are no IO streams — hence it is run on UI
            isCli = false;
            ioHandler = new student.adventure.IOHandler(System.in, System.out);
            scanner = new Scanner(System.in);
        } else {
            ioHandler = new student.adventure.IOHandler(inputStream, outputStream);
            scanner = new Scanner(inputStream);
        }
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
        status = new GameStatus(false, id, currentRoom.getDescription(), null, null,
                state, commandMap);
        command = new Command();
        updater = new Updater(this);

        try {
            dbConnection = new student.adventure.DatabaseConnection(); //establish a database connection
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
