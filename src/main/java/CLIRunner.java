import student.adventure.cli.CommandLineAdventure;
import student.adventure.JSONReader;

import java.io.File;
import java.io.IOException;

public class CLIRunner {
    public static void main(String[] args) throws IOException {
        JSONReader reader = new JSONReader(new File("src/main/resources/data/Data.json"));
        CommandLineAdventure adventure = new CommandLineAdventure(reader.getGame());
        adventure.run();
        adventure.quit();
    }
}
