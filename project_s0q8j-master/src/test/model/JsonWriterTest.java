package model;

import org.junit.jupiter.api.Test;
import persistence.JsonWriter;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    public void testInvalidWriter() {
        try {
            TimeTable tt = new TimeTable();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Should have failed");
        } catch (IOException e) {
            // passes test
        }
    }
}
