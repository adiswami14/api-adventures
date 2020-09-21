package student.server;

import org.junit.Before;
import org.junit.Test;
import student.adventure.api.Adventure;
import student.adventure.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStateTest {
    private Adventure adventure;
    private GameStatus gameStatus;
    private AdventureState state;
    private HashMap<String, List<String>> commandOptions;

    @Before
    public void SetUp(){
        adventure = new Adventure(0);
        state = new AdventureState(adventure);
        commandOptions = new HashMap<>();
        gameStatus = new GameStatus(false, 0, "Hey there!", null, null,
                state, commandOptions);
    }

    @Test
    public void StarterGameStateCheck() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Down the hallway", "Downstairs", "Upstairs"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("LeBron jersey", "MacBook pro"));
        commandOptions.put("take", list);
        list = new ArrayList<>();
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void GoGameStateCheck() {
        gameStatus.setMessage("Time to freshen up! Long day ahead!");
        adventure.setCommand(new Command("go", "down the hallway"));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Downstairs", "Back down the hallway"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("Shaving cream", "Toothbrush", "Toothpaste"));
        commandOptions.put("take", list);
        list = new ArrayList<>();
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        gameStatus.setState(new AdventureState(adventure));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void TakeGameStateCheck() {
        adventure.setCommand(new Command("take", "lebron jersey"));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Down the hallway", "Downstairs", "Upstairs"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("MacBook pro"));
        commandOptions.put("take", list);
        list = new ArrayList<>(Arrays.asList("LeBron jersey"));
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        gameStatus.setState(new AdventureState(adventure));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void DropGameStateCheck() {
        Player player = adventure.getPlayer();
        player.getInventory().add(player.getCurrentRoom().getItems().remove(0));
        adventure.setCommand(new Command("drop", "lebron jersey"));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Down the hallway", "Downstairs", "Upstairs"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("MacBook pro", "LeBron jersey"));
        commandOptions.put("take", list);
        list = new ArrayList<>();
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        gameStatus.setState(new AdventureState(adventure));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void DistanceToGameStateCheck() {
        adventure.setCommand(new Command("distanceTo", "Attic"));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Down the hallway", "Downstairs", "Upstairs"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("MacBook pro"));
        commandOptions.put("take", list);
        list = new ArrayList<>(Arrays.asList("LeBron jersey"));
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        gameStatus.setState(new AdventureState(adventure));
        gameStatus.setMessage("The distance between the current room and the Attic is 5.0 meters");
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void ExamineGameStateCheck() {
        adventure.setCommand(new Command("examine", ""));
        gameStatus.setMessage("You are in your bedroom. You can see the bathroom down the hallway.");
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Down the hallway", "Downstairs", "Upstairs"));
        commandOptions.put("go", list);
        list = new ArrayList<>(Arrays.asList(""));
        commandOptions.put("examine", list);
        list = new ArrayList<>(Arrays.asList("MacBook pro"));
        commandOptions.put("take", list);
        list = new ArrayList<>(Arrays.asList("LeBron jersey"));
        commandOptions.put("drop", list);
        list = new ArrayList<>(Arrays.asList("Bedroom", "Bathroom", "Attic", "Kitchen", "Living Room", "Basement"));
        commandOptions.put("distanceTo", list);
        gameStatus.setState(new AdventureState(adventure));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }
}
