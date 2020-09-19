package student.adventure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class UserInputTest {
    private Scanner scanner;
    private String inputString;
    private JSONReader reader;
    private Adventure adventure;
    private ArrayList<Item> testInventoryList;
    private ArrayList<String> testRoomHistory;
    private ArrayList<Item> roomItemsList;
    private Room firstRoom;

    public UserInputTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/Data.json"));
        firstRoom = reader.getGame().getRooms().get(0);
    }

    @Rule
    public ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void setUp() {
        scanner = new Scanner(System.in);
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
    public void GoInValidDirectionTest(){
        exit.expectSystemExit();
        Room livingRoom = reader.getGame().getRooms().get(4);
        inputString = "Adi"+"\ngo downstairs"+"\ngo to the living room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertEquals(livingRoom, adventure.getPlayer().getCurrentRoom());
    }

    @Test
    public void WrongRoomGoInDirectionTest(){
        Room livingRoom = reader.getGame().getRooms().get(4);
        inputString = "Adi"+"\ngo downstairs"+"\ngo to the living room"+"\ngo downstairs";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertFalse(livingRoom.equals(adventure.getPlayer().getCurrentRoom()));
    }

    @Test
    public void GoInInvalidDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo yes"+"\nquit";  //direction entered is "yes"
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getCurrentRoom());  //checks if direction entered is even valid
    }

    @Test
    public void GoInEmptyDirectionTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ngo    "+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getCurrentRoom());
    }

    @Test
    public void TakeValidItemTest(){
        exit.expectSystemExit();
        testInventoryList.add(reader.getGame().getRooms().get(0).getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertEquals(testInventoryList, adventure.getPlayer().getInventory());
        assertNotEquals(firstRoom.getItems(), roomItemsList);  //two asserts for the Item tests
        //one for the inventory addition, another for the item lost from the room
    }

    @Test
    public void TakeInvalidItemTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ntake something"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getInventory());
        assertEquals(roomItemsList, firstRoom.getItems());
    }

    @Test
    public void TakeEmptyItemTest(){
        exit.expectSystemExit();
        inputString = "Adi"+"\ntake   "+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getInventory());
        assertEquals(roomItemsList, firstRoom.getItems());
    }

    @Test
    public void DropValidItemTest(){  //adds item to inventory and drops it
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\ndrop lebron jersey"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getInventory()); //makes sure nothing is in inventory
        assertEquals(firstRoom.getItems(), roomItemsList);
    }

    @Test
    public void DropInvalidItemTest(){  //adds item to inventory and drops invalid item
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ntake lebron jersey"+"\ndrop nothing"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertTrue(adventure.getPlayer().getInventory().get(0).getName().equals("LeBron jersey"));
        assertNotEquals(firstRoom.getItems(), roomItemsList);
    }

    @Test
    public void DropEmptyItemTest(){
        exit.expectSystemExit();
        testInventoryList.add(firstRoom.getItems().get(0));
        inputString = "Adi"+"\ndrop"+"\nexit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNull(adventure.getPlayer().getInventory());
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
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertEquals(testRoomHistory, adventure.getPlayer().getRoomHistory());
    }

    @Test
    public void UnidirectionalIncorrectRoomHistoryTest() {
        exit.expectSystemExit();
        testRoomHistory.add("Bedroom");
        testRoomHistory.add("Bathroom");
        testRoomHistory.add("Kitchen");
        inputString = "Adi"+"\ngo downstairs"+"\ngo TO the living room"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertNotEquals(testRoomHistory, adventure.getPlayer().getRoomHistory());
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
        inputString = "Adi"+"\ngo downstairs"+"\ngo down the hallway"+"\ngo Back down the hallway"
                +"\ngo upstairs"+"\ngo downstairs"+"\ngo downstairs"+"\ngo to the living room"
                +"\ngo back to kitchen"+"\ngo upstairs"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertEquals(testRoomHistory, adventure.getPlayer().getRoomHistory());
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
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
        assertEquals(testRoomHistory, adventure.getPlayer().getRoomHistory());
    }

    @Test
    public void QuitTest() {
        exit.expectSystemExitWithStatus(0);
        inputString = "Adi"+"\nquit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
    }

    @Test
    public void ExitTest() {
        exit.expectSystemExitWithStatus(0);
        inputString = "Adi"+"\nexit";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        scanner = new Scanner(System.in);
        adventure = new Adventure(reader.getGame());
        adventure.run();
    }
}
