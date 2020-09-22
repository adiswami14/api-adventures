package student.adventure;

import student.adventure.Utils;
import student.server.Command;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Class for handling input and output streams
 */
public class IOHandler {
    private InputStream inputStream; //variable which handles input
    private PrintStream printStream; //variable which handles output

    /**
     * @param inputStream input stream used to run game
     * @param outputStream output stream we can use to print
     */
    public IOHandler(InputStream inputStream, OutputStream outputStream) {
        if(inputStream == null || outputStream == null) {
            throw new IllegalArgumentException();
        }
        this.inputStream = inputStream;
        printStream = (PrintStream)outputStream;
    }

    /**
     * Turns command line input into a command
     * @param userInput user input from the command line
     * @return a Command from user input from the command line
     */
    public Command convertStringToCommand(String userInput) {
        if(userInput == null) {
            throw new IllegalArgumentException();
        }
        String[] strInput = Utils.formatString(userInput).split(" ");
        String command = strInput[0].toLowerCase();
        String subject = Utils.formatString(Utils.joinStringArray(strInput, 1, strInput.length));
        if(subject.length() ==0) { //for commands such as quit and examine
            return new Command(command, null);
        }
        return new Command(command, subject);
    }

    /**
     * @return game's input stream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @return game's print stream
     */
    public PrintStream getPrintStream() {
        return printStream;
    }

    /**
     * uses print() method for all objects
     * @param output the Object to be printed
     */
    public void print(Object output) {
        printStream.print(output);
    }

    /**
     * uses println() method for all objects
     * @param output the Object to be printed
     */
    public void println(Object output) {
        printStream.println(output);
    }

    /**
     * uses println() method for no params
     */
    public void println() {
        printStream.println();
    }
}
