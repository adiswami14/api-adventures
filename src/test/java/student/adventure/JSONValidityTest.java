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
    public void nullJSONReaderTest() throws IOException {
        new JSONReader(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void emptyJSONReaderTest() throws IOException {
        new JSONReader(new File(""));
    }
}