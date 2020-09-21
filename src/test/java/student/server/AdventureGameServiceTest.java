package student.server;

import org.junit.Before;
import org.junit.Test;
import student.adventure.api.Adventure;

import java.util.HashMap;

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
        GameStatus status = new GameStatus(false, 1, "Hey there!", null, null,
                new AdventureState(new Adventure(1)), new HashMap<>());
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
