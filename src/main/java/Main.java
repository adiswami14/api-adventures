import student.adventure.JSONReader;
import student.adventure.Adventure;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /* HttpServer server = AdventureServer.createServer(AdventureResource.class);
        server.start();*/
        JSONReader reader = new JSONReader(new File("src/main/resources/data/Data.json"));
        Adventure adventure = new Adventure(reader.getGame());
        adventure.run();
        adventure.terminate();
    }
}