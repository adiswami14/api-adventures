package student.adventure;

import org.junit.Test;
import student.server.Command;

import static org.junit.Assert.assertEquals;

public class IOHandlerTest {
    private IOHandler ioHandler;

    @Test (expected = IllegalArgumentException.class)
    public void NullInputParameterIOHandlerTest() {
        ioHandler = new IOHandler(null, System.out);
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullOutputParameterIOHandlerTest() {
        ioHandler = new IOHandler(System.in, null);
    }

    @Test
    public void ValidInputParameterIOHandlerTest() {
        ioHandler = new IOHandler(System.in, System.out);
        assertEquals(System.in, ioHandler.getInputStream());
    }

    @Test
    public void ValidOutputParameterIOHandlerTest() {
        ioHandler = new IOHandler(System.in, System.out);
        assertEquals(System.out, ioHandler.getPrintStream());
    }

    @Test
    public void ValidConvertStringToCommandTest() {
        ioHandler = new IOHandler(System.in, System.out);
        Command command = new Command("go", "downstairs");
        Command testCommand = ioHandler.convertStringToCommand("go downstairs");
        assertEquals(command.getCommandName(),
                testCommand.getCommandName());
        assertEquals(command.getCommandValue(),
                testCommand.getCommandValue());
    }

    @Test (expected = IllegalArgumentException.class)
    public void NullConvertStringToCommandTest() {
        ioHandler = new IOHandler(System.in, System.out);
        Command command = new Command("go", "downstairs");
        Command testCommand = ioHandler.convertStringToCommand(null);
    }
}

