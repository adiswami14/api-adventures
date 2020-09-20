package student.adventure;

import student.server.Command;
import student.server.GameStatus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

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
        status = new GameStatus(false, id, "Hey there!", null, null, null,
                null);
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
    }

    /**
     * Method that is called to start the game
     */
    public void run(){
        initializePlayer();
        examine();   //get description of the room you start of in
        handleUserInput();
        //ioHandler.println(currentRoom.getDescription());  //get description of the last room
    }

    /**
     * Method for quitting the program
     * Seems unnecessary, but it improves readability
     */
    public void quit() {

    }

    /**
     * Initializes basic player fields such as name and currentRoom
     */
    private void initializePlayer(){
        player = new Player("");
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
        examineDirections(currentRoom); //split examine function into examineDirections and examineItems
        examineItems(currentRoom);
    }

    /**
     * Examines possible directions for a given room
     * @param room room we are examining
     */
    private void examineDirections(Room room) {
        //ioHandler.println(room.getDescription());  //String we will return for this method
        //ioHandler.print("From here, you can go: ");

        ArrayList<Direction> directionsInRoom = room.getPossibleDirections();

        for(int directionIndex = 0; directionIndex<directionsInRoom.size();directionIndex++) {
           // ioHandler.print(directionsInRoom.get(directionIndex).getName());
            if(directionIndex != directionsInRoom.size()-1) {
               // ioHandler.print(", ");
            }
        }
        //ioHandler.println();
    }

    /**
     * Examines possible Items for a given room
     * @param room room we are examining
     */
    private void examineItems(Room room) {
       // ioHandler.print("Items: ");
        ArrayList<Item> itemsInRoom = room.getItems();

        for(int itemIndex = 0; itemIndex<itemsInRoom.size();itemIndex++) {
            //ioHandler.print(itemsInRoom.get(itemIndex).getName());
            if(itemIndex != itemsInRoom.size()-1) {
                //ioHandler.print(", ");
            }
        }
        //ioHandler.println();
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
                //ioHandler.println("I don't understand \""+commandName+"\"!");
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
                        return;
                    }
                }
            }
        }
        //ioHandler.println("I can't go \""+directionName+"\"!");
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
        //ioHandler.println("You don't have \""+itemName+"\" in your inventory!");
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
     * Function that formats and prints out room history of player
     */
    private void history() {
        //ioHandler.print("Your room history: ");
        ArrayList<String> playerRoomHistory = player.getRoomHistory();

        for(int roomIndex = 0; roomIndex<playerRoomHistory.size();roomIndex++) {
            //ioHandler.print(playerRoomHistory.get(roomIndex));
            if(roomIndex != playerRoomHistory.size()-1) {
                //ioHandler.print(", ");
            }
        }
        //ioHandler.println();
    }

    /**
     * Function that gives distance to room that is typed in
     */
    private void distanceTo(String roomName) {
        Location currentLoc = currentRoom.getLocation();

        for(Room room : rooms) {
            if(room.getName().toLowerCase().equals(roomName.toLowerCase())) {
                //ioHandler.println("The distance between the current room and the "+roomName+" is "
                        //+Utils.findDistance(currentLoc, room.getLocation())+" meters");
                return;
            }
        }

        //ioHandler.println("The room name \""+roomName+"\" is invalid");
    }

    /**
     * Checks if player is in basement, to check if game is over
     * @return whether the game is over
     */
    private boolean isGameOver() {
        return (currentRoom.isEndingRoom());
    }

    /**
     * Updates gameState of current game
     */
    private void updateGameState() {

    }
}
