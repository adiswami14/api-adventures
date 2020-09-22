package student.server;

import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdventureGameServiceTest {
    private AdventureGameService ags;

    @Before
    public void SetUp() {
        ags = new AdventureGameService();
    }

    @Test
    public void ResetTest() {
        ags.reset();
        assertEquals(0, ags.getGameId());
    }

    @Test
    public void NewGameTest() throws AdventureException {
        ags.newGame();
        ags.newGame();
        ags.newGame();
        assertEquals(2, ags.getGameMap().get(2).getId()); //check id passed into Adventure object
    }

    @Test
    public void GetGameTest() throws AdventureException {
        ags.newGame();
        ags.newGame();
        ags.newGame();
        Adventure adventure = new Adventure(1, null, null);
        adventure.run();
        HashMap<String, List<String>> commandOptions = new HashMap<String, List<String>>();
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
        GameStatus status = new GameStatus(false, 1, "", null, null,
                new AdventureState(adventure), commandOptions);
        status.setMessage(adventure.getRooms().get(0).getDescription());
        assertTrue(status.equals(ags.getGame(1)));
    }

    @Test
    public void DestroyGameTest() throws AdventureException {
        ags.newGame();
        ags.destroyGame(0);
        assertEquals(0, ags.getGameMap().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullExecuteCommandTest() throws AdventureException {
        ags.newGame();
        ags.executeCommand(0, null);
    }

    @Test
    public void ValidExecuteCommandTest() throws AdventureException {
        ags.newGame();
        Command command = new Command("go", "East");
        ags.executeCommand(0, command);
        assertEquals(command, ags.getGameMap().get(0).getCommand());
    }


}
