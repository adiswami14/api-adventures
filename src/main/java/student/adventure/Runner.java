package student.adventure;

import java.util.ArrayList;
import java.util.Scanner;

public class Runner {
    private Scanner scanner;
    private Player player;
    private Game game;
    private Room currentRoom;

    public Runner(Game game) {
        scanner = new Scanner(System.in);
        this.game = game;
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
        while(!isGameOver()) {
            checkUserInput(scanner.nextLine());
        }
        System.out.println(currentRoom.getDescription());  //just to get description of the last room
        terminate();
    }

    /**
     * Initializes basic player fields such as name and currentRoom
     */
    private void initializePlayer(){
        System.out.println("Hey player, what's your name: ");

        player = new Player(scanner.nextLine().trim());
        System.out.print("Hey, "+player.getName()+"! ");
        player.setCurrentRoom(game.getRooms().get(0));
        currentRoom = player.getCurrentRoom();
        player.getRoomHistory().add(currentRoom.getName()); //adds starting room to room history

        examine(currentRoom);   //just to get description of the room you start of in
    }

    /**
     * Aligns with examine() method in assignment description
     * @param room room we are examining
     */
    private void examine(Room room) {
        if(room == null) {
            throw new IllegalArgumentException();
        }
        examineDirections(room); //split examine function into examineDirections and examineItems
        examineItems(room);
    }

    /**
     * Examines possible directions for a given room
     * @param room room we are examining
     */
    private void examineDirections(Room room) {
        System.out.println(room.getDescription());  //String we will return for this method
        System.out.print("From here, you can go: ");
        ArrayList<Direction> directionsInRoom = room.getPossibleDirections();

        for(int directionIndex = 0; directionIndex<directionsInRoom.size();directionIndex++) {
            System.out.print(directionsInRoom.get(directionIndex).getName());
            if(directionIndex != directionsInRoom.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Examines possible Items for a given room
     * @param room room we are examining
     */
    private void examineItems(Room room) {
        System.out.print("Items: ");
        ArrayList<Item> itemsInRoom = room.getItems();

        for(int itemIndex = 0; itemIndex<itemsInRoom.size();itemIndex++) {
            System.out.print(itemsInRoom.get(itemIndex).getName());
            if(itemIndex != itemsInRoom.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Runs certain functions according to the string passed in
     * @param userInput the input string the user passes in at certain points of the game
     */
    private void checkUserInput(String userInput) {
        String[] strInput = Utils.formatString(userInput).split(" ");
        String command = strInput[0];
        String subject = Utils.formatString(Utils.joinStringArray(strInput, 1, strInput.length));
        //since command is the first element, we only need to join the rest of the string array

        switch(command.toLowerCase()) { //checks first word of userInput
            case "examine":
                examine(currentRoom);
                break;
            case "take":
                takeItem(subject);
                break;
            case "drop":
                dropItem(subject);
                break;
            case "go":
                goInDirection(subject);
                break;
            case "history":
                history();
                break;
            case "distanceto":
                distanceTo(subject);
                break;
            case "quit":  //no break here because quit and exit have exactly the same purpose
            case "exit":
                terminate();
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
    private void goInDirection(String directionName) {
        for(Direction direction : currentRoom.getPossibleDirections()) {

            if(direction.getName().toLowerCase().equals(directionName.toLowerCase())) {
                //checks lowercase in all command methods because we want to be able to print out exactly
                //what the user said, instead of altering the command and/or subject string
                for(Room room : game.getRooms()) {
                    if(direction.getConnectingRoom().equals(room.getName())) {
                        player.setCurrentRoom(room);
                        currentRoom = player.getCurrentRoom();
                        player.getRoomHistory().add(currentRoom.getName()); //adds room to room history list
                        return;
                    }
                }
            }
        }
        System.out.println("I can't go \""+directionName+"\"!");
    }

    /**
     * Player attempts to drop a certain item
     * @param itemName name of the item the user enters
     */
    private void dropItem(String itemName) {
        for(Item item : player.getInventory()) {
            if(item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                currentRoom.getItems().add(item);
                player.getInventory().remove(item);
                return;
            }
        }
        System.out.println("You don't have \""+itemName+"\" in your inventory!");
    }

    /**
     * Player attempts to take a certain item
     * @param itemName name of the item the user enters
     */
    private void takeItem(String itemName) {
        for(Item item : currentRoom.getItems()) {
            if(item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                player.getInventory().add(item);
                currentRoom.getItems().remove(item);
                return;
            }
        }
        System.out.println("There is no item \""+itemName+"\" in this room!");
    }

    /**
     * Function that formats and prints out room history of player
     */
    private void history() {
        System.out.print("Your room history: ");
        ArrayList<String> playerRoomHistory = player.getRoomHistory();

        for(int roomIndex = 0; roomIndex<playerRoomHistory.size();roomIndex++) {
            System.out.print(playerRoomHistory.get(roomIndex));
            if(roomIndex != playerRoomHistory.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Function that gives distance to room that is typed in
     */
    private void distanceTo(String roomName) {
        Location currentLoc = currentRoom.getLocation();

        for(Room room : game.getRooms()) {
            if(room.getName().toLowerCase().equals(roomName.toLowerCase())) {
                System.out.println("The distance between the current room and the "+roomName+" is "
                        +Utils.findDistance(currentLoc, room.getLocation())+" meters");
                return;
            }
        }

        System.out.println("The room name \""+roomName+"\" is invalid");
    }

    /**
     * Checks if player is in basement, to check if game is over
     * @return whether the game is over
     */
    private boolean isGameOver() {
        return (currentRoom.getName().equals("Basement"));
    }

    /**
     * Method for quitting the program
     * Seems unnecessary, but it improves readability
     */
    private void terminate() {
        System.exit(0);
    }
}
