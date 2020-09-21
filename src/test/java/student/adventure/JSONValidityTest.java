package student.adventure;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;
import student.adventure.JSONReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JSONValidityTest {
    //JSON validity testing
    @Test(expected = NullPointerException.class)
    public void NullJSONReaderTest() throws IOException {
        new JSONReader(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void EmptyJSONReaderTest() throws IOException {
        new JSONReader(new File(""));
    }

    @Test
    public void EmptyJSONObjectReaderTest() throws IOException {
        new JSONReader(new File("src/main/resources/data/Empty.json"));
        //Gson handles this in gson.fromJson() call in JSONReader.parseFile()
    }

    @Test (expected = JsonSyntaxException.class)
    public void IncorrectJSONDataReaderTest() throws IOException {
        new JSONReader(new File("src/main/resources/data/Incorrect.json"));
    }
}