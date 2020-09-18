package student.adventure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class JSONReader {
    private Game game;

    public JSONReader(File file) throws IOException {
        if(file == null) {
            throw new NullPointerException();
        }
        parseFile(file);
    }

    /**
     * @return the current instance of the Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Parses through JSON file given in JSONReader constructor
     * @throws IOException in the event that file is null
     */
    private void parseFile(File file) throws IOException {
        Gson gson = new Gson();
        game = gson.fromJson(new FileReader(file), Game.class);
    }
}
