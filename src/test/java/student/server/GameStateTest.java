package student.server;

import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;
import student.adventure.Item;
import student.adventure.Player;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStateTest {
    private Adventure adventure;
    private GameStatus gameStatus;
    private HashMap<String, List<String>> commandOptions;

    @Before
    public void SetUp(){
        adventure = new Adventure(0);
        gameStatus = new GameStatus(false, 0, "Hey there!", null, null,
                new AdventureState(adventure), new HashMap<>());
        commandOptions = new HashMap<>();
    }

    @Test
    public void StarterGameStateCheck() {
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void GoGameStateCheck() {
        gameStatus.setMessage("Time to freshen up! Long day ahead!");
        adventure.setCommand(new Command("go", "down the hallway"));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void TakeGameStateCheck() {
        adventure.setCommand(new Command("take", "lebron jersey"));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void DropGameStateCheck() {
        Player player = adventure.getPlayer();
        player.getInventory().add(player.getCurrentRoom().getItems().get(0));
        adventure.setCommand(new Command("drop", "lebron jersey"));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void DistanceToGameStateCheck() {
        adventure.setCommand(new Command("distanceto", "Kitchen"));
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }

    @Test
    public void ExamineGameStateCheck() {
        adventure.setCommand(new Command("examine", ""));
        gameStatus.setMessage("You are in your bedroom. You can see the bathroom down the hallway.");
        assertTrue(gameStatus.equals(adventure.getStatus()));
    }
}