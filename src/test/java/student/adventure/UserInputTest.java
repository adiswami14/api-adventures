package student.adventure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
//import student.adventure.cli.Adventure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserInputTest {
    private String inputString;
    private JSONReader reader;
    private Adventure adventure;
    private ArrayList<Item> testInventoryList;
    private ArrayList<String> testRoomHistory;
    private ArrayList<Item> roomItemsList;
    private Room firstRoom;
    private Player player;

    public UserInputTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/Data.json"));
        firstRoom = reader.getGame().getRooms().get(0);
    }

    @Rule
    public ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void setUp() {
        inputString = "";
        testInventoryList = new ArrayList<>();
        testRoomHistory = new ArrayList<>();
        roomItemsList = firstRoom.getItems();
    }

    @Test (expected = NullPointerException.class)
    public void NullInputStreamTestRun() {
        inputString = null;
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    }

    @Test
    public void QuitTest() {
        exit.expectSystemExitWithStatus(0);
        inputString = "Adi"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        //Adventure instance is called in every new testing method because of the while loop
        //structure of my code in CLI
        //adventure.run();
    }

    @Test
    public void BadFormatQuitTest(){
        exit.expectSystemExitWithStatus(0);
        inputString = "Adi"+"\nqUIt\t";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
    }

    @Test
    public void EndingRoomTest() {
        inputString = "Adi"+"\ngo downstairs"+"\ngo to the living room"+"\ngo downstairs"; //basement reached
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        adventure.run();
        assertTrue(adventure.isGameOver());
    }

    @Test
    public void GoInValidDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo downstairs"+"\ngo to the living room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals("Living Room", player.getCurrentRoom().getName());
    }

    @Test
    public void BadFormatGoInValidDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo     DowNstaIrs"+"\ngo \tto \tthe liVIng room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals("Living Room", player.getCurrentRoom().getName());
    }

    @Test
    public void WrongRoomGoInDirectionTest(){
        exit.expectSystemExitWithStatus(0);
        inputString = "Adi"+"\ngo downstairs"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        adventure.run();
        player = adventure.getPlayer();
        assertFalse((player.getCurrentRoom().getName()).equals("Living Room"));
    }

    @Test
    public void GoInInvalidDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo yes"+"\nquit";  //direction entered is "yes"
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getCurrentRoom().getName(), "Bedroom");  //checks if direction entered is even valid
    }

    @Test
    public void GoInEmptyDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo    "+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getCurrentRoom().getName(), "Bedroom");
    }

    @Test
    public void TakeValidItemTest(){
        exit.expectSystemExit();
        testInventoryList.add(reader.getGame().getRooms().get(0).getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(testInventoryList.get(0).getName(), player.getInventory().get(0).getName());
        assertNotEquals(player.getCurrentRoom().getItems(), roomItemsList);  //two asserts for the Item tests
        //one for the inventory addition, another for the item lost from the room
    }

    @Test
    public void BadFormatTakeValidItemTest(){
        exit.expectSystemExit();
        testInventoryList.add(reader.getGame().getRooms().get(0).getItems().get(0));
        inputString = "Adi"+"\ntake \t\tlEBron \tJeRsey\t"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(testInventoryList.get(0).getName(), player.getInventory().get(0).getName());
        assertNotEquals(player.getCurrentRoom().getItems(), roomItemsList);
    }

    @Test
    public void TakeInvalidItemTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ntake something"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getInventory().size(), 0);
        assertEquals(roomItemsList, firstRoom.getItems());
    }

    @Test
    public void TakeEmptyItemTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ntake   "+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getInventory().size(), 0);
        assertEquals(roomItemsList, firstRoom.getItems());
    }

    @Test
    public void DropValidItemTest(){  //adds item to inventory and drops it
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\ndrop lebron jersey"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getInventory().size(), 0); //makes sure nothing is in inventory
        assertEquals(firstRoom.getItems(), roomItemsList);
    }

    @Test
    public void BadFormatDropValidItemTest(){  //adds item to inventory and drops it
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\ndrop LEbrOn \t   JerSEy"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getInventory().size(), 0); //makes sure nothing is in inventory
        assertEquals(firstRoom.getItems(), roomItemsList);
    }

    @Test
    public void DropInvalidItemTest(){  //adds item to inventory and drops invalid item
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\ndrop nothing"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertTrue(player.getInventory().get(0).getName().equals("LeBron jersey"));
        assertNotEquals(player.getCurrentRoom().getItems(), roomItemsList);
    }

    @Test
    public void DropEmptyItemTest(){
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ndrop  "+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(player.getInventory().size(), 0);
        assertEquals(firstRoom.getItems(), roomItemsList);
    }

    @Test
    public void UnidirectionalValidRoomHistoryTest() {
        exit.expectSystemExit();
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Kitchen");
        testRoomHistory.add("Living Room");
        inputString = "Adi"+"\ngo downstairs"+"\ngo TO the living room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(testRoomHistory, player.getRoomHistory());
    }

    @Test
    public void UnidirectionalIncorrectRoomHistoryTest() {
        exit.expectSystemExit();
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Bathroom");
        testRoomHistory.add("Kitchen");
        inputString = "Adi"+"\ngo downstairs"+"\ngo TO the living room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertNotEquals(testRoomHistory, player.getRoomHistory());
    }

    @Test
    public void MultidirectionalValidRoomHistoryTest() {
        exit.expectSystemExit();
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Bathroom");
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Attic");
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Kitchen");
        testRoomHistory.add("Living Room");
        testRoomHistory.add("Kitchen");
        testRoomHistory.add("Bedroom");
        inputString = "Adi"+"\ngo down the hallway"+"\ngo Back down the hallway"+"\ngo upstairs"
                +"\ngo downstairs"+"\ngo downstairs"+"\ngo to the living room"+"\ngo to the kitchen"
                +"\ngo upstairs"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertEquals(testRoomHistory, player.getRoomHistory());
    }

    @Test
    public void MultidirectionalIncorrectRoomHistoryTest() {
        exit.expectSystemExit();
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Bathroom");
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Attic");
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Kitchen");
        testRoomHistory.add("Basement");
        testRoomHistory.add("Kitchen");
        testRoomHistory.add("Bedroom");
        inputString = "Adi"+"\ngo downstairs"+"\ngo down the hallway"+"\ngo Back down the hallway"
                +"\ngo upstairs"+"\ngo downstairs"+"\ngo downstairs"+"\ngo to the living room"
                +"\ngo back to kitchen"+"\ngo upstairs"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        adventure = new Adventure(0, System.in, System.out);
        player = adventure.getPlayer();
        adventure.run();
        assertNotEquals(testRoomHistory, player.getRoomHistory());
    }
}
