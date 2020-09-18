package student.adventure;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;
import student.adventure.JSONReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JSONValidityTest {
    private Gson gson;
    private String filePaths;
    @Before
    public void setUp() {
        gson = new Gson();
        filePaths = "";
    }

    //JSON validity testing
    @Test
    public void EmptyJSONStringTest() {
        filePaths = "";
        assertNull(gson.fromJson(filePaths, Game.class));
    }

    @Test
    public void EmptyJSONObjectTest() {
        filePaths ="{}";
        Game game = gson.fromJson(filePaths, Game.class);
        assertNull(game.getRooms());
    }

    @Test(expected = JsonSyntaxException.class)
    public void InvalidJSONStringTest() {
        filePaths = "{Iasdsaffdfa}";
        gson.fromJson(filePaths, Game.class);
    }

    @Test(expected = NullPointerException.class)
    public void nullJSONReaderTest() throws IOException {
        new JSONReader(null);
    }
}