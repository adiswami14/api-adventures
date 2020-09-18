package student.adventure;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerTest {
    Player player;

    @Test (expected = IllegalArgumentException.class)
    public void NullNamePlayerTest() {
        player = new Player(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EmptyNamePlayerTest() {
        player = new Player("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void EmptyWhitespaceNamePlayerTest() {
        player = new Player(" ");
    }

    @Test
    public void ValidFirstNamePlayerTest() {
        player = new Player("Gerald");
        assertEquals("Gerald", player.getName());
    }

    @Test
    public void ExtraWhitespaceFirstNamePlayerTest() {
        player = new Player("  Gerald    ");
        assertEquals("Gerald", player.getName());
    }

    @Test
    public void ValidFullNamePlayerTest() {
        player = new Player("Joe Mama");
        assertEquals("Joe Mama", player.getName());
    }

    @Test
    public void ExtraWhitespaceFullNamePlayerTest() {
        player = new Player("    Joe      Mama   ");
        assertEquals("Joe Mama", player.getName());
    }

    @Test
    public void PunctuationFullNamePlayerTest() {
        player = new Player("Joe.? Mama!");
        assertEquals("Joe Mama", player.getName());
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullPlayerSetCurrentRoomTest() {
        player = new Player("Joe Mama");
        player.setCurrentRoom(null);
    }
}
