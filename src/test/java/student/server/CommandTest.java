package student.server;

import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;

import static org.junit.Assert.assertEquals;

public class CommandTest {
    Adventure adventure;
    Command command;

    @Before
    public void SetUp() {
        adventure = new Adventure(0, null, null);
        command = new Command();
    }

    @Test
    public void GoCommandTest() {
        command = new Command("go", "Down the hallway");
        adventure.setCommand(command);
        assertEquals("Bathroom", adventure.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void BadFormatGoCommandTest() {
        command = new Command("go", "dOWn     \tThE hAllWAy");
        adventure.setCommand(command);
        assertEquals("Bathroom", adventure.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void TakeCommandTest() {
        command = new Command("take", "MacBook pro");
        adventure.setCommand(command);
        assertEquals("MacBook pro", adventure.getPlayer().getInventory().get(0).getName());
        assertEquals("LeBron jersey", adventure.getPlayer().getCurrentRoom().getItems().get(0).getName());
    }

    @Test
    public void BadFormatTakeCommandTest() {
        command = new Command("take", "   MaCBOOK   PrO");
        adventure.setCommand(command);
        assertEquals("MacBook pro", adventure.getPlayer().getInventory().get(0).getName());
        assertEquals("LeBron jersey", adventure.getPlayer().getCurrentRoom().getItems().get(0).getName());
    }

    @Test
    public void DropCommandTest() {
        adventure.getPlayer().getInventory().add(adventure.getPlayer().getCurrentRoom().getItems().remove(0));
        command = new Command("drop", "LeBron jersey");
        adventure.setCommand(command);
        assertEquals(0, adventure.getPlayer().getInventory().size());
        assertEquals(2, adventure.getPlayer().getCurrentRoom().getItems().size());
    }

    @Test
    public void BadFormatDropCommandTest() {
        adventure.getPlayer().getInventory().add(adventure.getPlayer().getCurrentRoom().getItems().remove(0));
        command = new Command("drop", "lebRon \t\tjERSeY\t\t");
        adventure.setCommand(command);
        assertEquals(0, adventure.getPlayer().getInventory().size());
        assertEquals(2, adventure.getPlayer().getCurrentRoom().getItems().size());
    }

    @Test
    public void DistanceToCommandTest() {
        command = new Command("distanceTo", "Attic");
        adventure.setCommand(command);
        assertEquals("The distance between the current room and the Attic is 5.0 meters",
                adventure.getStatus().getMessage());
    }

    @Test
    public void BadFormatDistanceToCommandTest() {
        command = new Command("distanceTo", "     \taTtIC");
        adventure.setCommand(command);
        assertEquals("The distance between the current room and the Attic is 5.0 meters",
                adventure.getStatus().getMessage());
    }

    @Test
    public void ExamineCommandTest() {
        command = new Command("examine", "");
        adventure.setCommand(command);
        assertEquals("You are in your bedroom. You can see the bathroom down the hallway.",
                adventure.getStatus().getMessage());
    }

    @Test
    public void BadFormatExamineCommandTest() {
        command = new Command("examine", "       \t\t\t");
        adventure.setCommand(command);
        assertEquals("You are in your bedroom. You can see the bathroom down the hallway.",
                adventure.getStatus().getMessage());
    }
}
