package student.adventure.cli;

import student.adventure.*;
import student.adventure.cli.IOHandler;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The CLI game engine
 */
public class CommandLineAdventure {
    private Scanner scanner; //scanner for the game
    private Player player;  //Player object of this game
    private ArrayList<Room> rooms;  //list of rooms in the game
    private Room currentRoom; //current Room that Player is in, used for brevity
    private IOHandler ioHandler; //IOHandler instance for external input/output streams

    /**
     * @param game instance of Game wrapper class, used to extract list of rooms
     */
    public CommandLineAdventure(Game game) {
        ioHandler = new IOHandler(System.in, System.out);
        scanner = new Scanner(ioHandler.getInputStream());
        this.rooms = game.getRooms();
    }

    /**
     * @return Player object of the game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Method that is called to start the game
     */
    public void run(){
        initializePlayer();
        examine();   //get description of the room you start of in
        while(!isGameOver()) {
            handleUserInput(scanner.nextLine());
        }
        ioHandler.println(currentRoom.getDescription());  //get description of the last room
    }


    /**
     * Method for quitting the program
     * Seems unnecessary, but it improves readability
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Checks if player is in basement, to check if game is over
     * @return whether the game is over
     */
    public boolean isGameOver() {
        return (currentRoom.isEndingRoom());
    }

    /**
     * Initializes basic player fields such as name and currentRoom
     */
    private void initializePlayer(){
        ioHandler.println("Hey player, what's your name: ");

        player = new Player(scanner.nextLine().trim());
        ioHandler.print("Hey, "+player.getName()+"! ");
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
     * @param userInput the input string the user passes in at certain points of the game
     */
    private void handleUserInput(String userInput) {
        String[] strInput = Utils.formatString(userInput).split(" ");
        String command = strInput[0];
        String subject = Utils.formatString(Utils.joinStringArray(strInput, 1, strInput.length));
        //since command is the first element, we only need to join the rest of the string array

        switch(command.toLowerCase()) { //checks first word of userInput
            case "examine":
                examine();
                break;
            case "take":
                take(subject);
                break;
            case "drop":
                drop(subject);
                break;
            case "go":
                go(subject);
                break;
            case "history":
                history();
                break;
            case "distanceto":
                distanceTo(subject);
                break;
            case "quit":  //no break here because quit and exit have exactly the same purpose
            case "exit":
                quit();
                break;
            default:
                System.out.println("I don't understand \""+userInput+"\"!");
                break;
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
                ioHandler.println("The distance between the current room and the "+roomName+" is "
                        +Utils.findDistance(currentLoc, room.getLocation())+" meters");
                return;
            }
        }

        ioHandler.println("The room name \""+roomName+"\" is invalid");
    }
}
