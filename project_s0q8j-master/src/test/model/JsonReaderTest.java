package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    private Class classTest1;
    private Class classTest2;
    private Class classTest3;
    private Assignment assignmentTest1;
    private Assignment assignmentTest2;
    private Assignment assignmentTest3;
    private CalendarTime calendarTimeTest;
    private CalendarTime calendarTimeTest2;
    private CalendarTime calendarTimeTest3;
    private int year;
    private int month;
    private int day;


    @BeforeEach
    public void setUp() {
        calendarTimeTest = new CalendarTime(2021,
                10,
                11,
                9,
                0,
                2021,
                10,
                11,
                10,
                0 );
        Calendar startTimeTest = Calendar.getInstance();
        Calendar endTimeTest = Calendar.getInstance();
        endTimeTest.add(Calendar.HOUR, 1);
        calendarTimeTest2 = new CalendarTime(startTimeTest, endTimeTest);
        year = calendarTimeTest2.getEndTime().get(Calendar.YEAR);
        month = calendarTimeTest2.getEndTime().get(Calendar.MONTH);
        day = calendarTimeTest2.getEndTime().get(Calendar.DAY_OF_MONTH);
        calendarTimeTest3 = new CalendarTime(2021,
                10,
                12,
                9,
                0,
                2021,
                10,
                12,
                10,
                0);
        classTest1 = new Class("Class1", "Prof1");
        assignmentTest1 = new Assignment("assignment1", calendarTimeTest, "Test1");
        assignmentTest2 = new Assignment("assignment2", calendarTimeTest3, "Test2");
        assignmentTest3 = new Assignment("assignment3", calendarTimeTest2, "Test3");
    }

    @Test
    public void testNonExistentFile() {
        JsonReader reader = new JsonReader("./data/test.json");
        try {
            TimeTable tt = reader.read();
            fail("IOException excepted");
        } catch (IOException e) {
            // passes
        }
    }

    @Test
    public void testEmptyTimeTable() {
        JsonReader reader = new JsonReader("./data/testEmptyTimeTable.json");
        try {
            TimeTable tt = reader.read();
            assertEquals(0, tt.getClasses().size());
        } catch (IOException e) {
            fail("Didn't read from the file");
        }
    }

    @Test
    public void testSingleClassInTimeTable() {
        JsonReader reader = new JsonReader("./data/testSingleClassInTT.json");
        try {
            TimeTable tt = reader.read();
            tt.addClass(classTest1);
            assertEquals(2, tt.getClasses().size());
        } catch (IOException e) {
            fail("Couldn't read from the file");
        }
    }
}