package student.adventure;

import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RoomTest {
    JSONReader reader;

    @Test (expected = IllegalArgumentException.class)
    public void NullNameRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/NullName.json"));
        reader.getGame().getRooms().get(0).getName();
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullDescriptionRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/NullDescription.json"));
        reader.getGame().getRooms().get(0).getDescription();
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullItemsRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/NullItems.json"));
        reader.getGame().getRooms().get(0).getItems();
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullPossibleDirectionsRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/NullPossibleDirections.json"));
        reader.getGame().getRooms().get(0).getPossibleDirections();
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullLocationRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/NullLocations.json"));
        reader.getGame().getRooms().get(0).getLocation();
    }

    @Test
    public void EmptyRoomTest() throws IOException {
        reader = new JSONReader(new File("src/main/resources/data/Empty.json"));
        reader.getGame().getRooms();
    }
}
