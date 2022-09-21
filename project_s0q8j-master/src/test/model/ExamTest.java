package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExamTest extends CalendarEventTest {

    private Exam examTest;

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
        examTest = new Exam("Test", testCalendarTime, 40);
        calendarEventTest = new Exam("Test1", testCalendarTime, 40);
    }

    @Test
    public void getWeightTest() {
        assertEquals(40, examTest.getWeight());
    }
}
