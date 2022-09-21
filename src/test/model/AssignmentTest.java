package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest extends CalendarEventTest {

    private Assignment assignmentTest;

    @BeforeEach
    public void setUp() {
        testCalendarTime = new CalendarTime(2021,
                10,
                14,
                9,
                0,
                2021,
                10,
                14,
                10,
                0 );
        assignmentTest = new Assignment("Test", testCalendarTime, "Test Description");
        calendarEventTest = new Assignment("Test1", testCalendarTime, "Test Description");
    }

    @Test
    public void setProgressionTest() {
        assertEquals(0, assignmentTest.getProgression());
        assignmentTest.setProgression(50);
        assertEquals(50, assignmentTest.getProgression());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Test Description", assignmentTest.getDescription());
    }
}
