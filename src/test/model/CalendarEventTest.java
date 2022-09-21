package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CalendarEventTest {

    protected CalendarEvent calendarEventTest;
    protected CalendarTime testCalendarTime;

    @Test
    public void getEventNameTest() {
        assertEquals("Test1", calendarEventTest.getEventName());
    }

    @Test
    public void getEventDueDateTest() {
        assertEquals(0,
                calendarEventTest.getEventDueDate().getStartTime().compareTo(testCalendarTime.getStartTime()));
        assertEquals(0,
                calendarEventTest.getEventDueDate().getEndTime().compareTo(testCalendarTime.getEndTime()));
    }
}
