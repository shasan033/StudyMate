package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTest {

    private Class classTest1;
    private Assignment assignmentTest1;
    private Exam examTest1;
    private CalendarTime calendarTimeTest1;
    private CalendarTime calendarTimeTest2;
    private CalendarTime calendarTimeTest3;
    private CalendarTime calendarTimeTest4;

    @BeforeEach
    public void setUp() {
        // Entire second week of November
        calendarTimeTest1 = new CalendarTime(2021, 10, 7, 0, 0,
                2021, 10, 13, 23, 59);
        // Example Due Date 1
        calendarTimeTest2 = new CalendarTime(2021, 10, 3, 12, 0,
                2021, 10, 3, 12, 0);
        // Example Due Date 2
        calendarTimeTest3 = new CalendarTime(2021, 10, 4, 12, 0,
                2021, 10, 4, 12, 0);
        // Entire first week of November
        calendarTimeTest4 = new CalendarTime(2021, 10, 1, 0, 0,
                2021, 10, 6, 23, 59);

        examTest1 = new Exam("Test1", calendarTimeTest2, 45);
        assignmentTest1 = new Assignment("aTest1", calendarTimeTest3, "TestDescription");

        classTest1 = new Class("Class1", "Prof1");
    }

    @Test
    public void getClassNameTest() {
        assertEquals("Class1", classTest1.getClassName());
    }

    @Test
    public void getProfNameTest() {
        assertEquals("Prof1", classTest1.getProfName());
    }

    @Test
    public void getAssignmentsTest() {
        classTest1.addAssignment(assignmentTest1);
        ArrayList<Assignment> test = new ArrayList<>();
        test.add(assignmentTest1);
        assertEquals(1, classTest1.getAssignments().size());
        assertEquals(test, classTest1.getAssignments());
    }

    @Test
    public void getExamsTest() {
        classTest1.addExam(examTest1);
        ArrayList<Exam> test = new ArrayList<>();
        test.add(examTest1);
        assertEquals(1, classTest1.getExams().size());
        assertEquals(test, classTest1.getExams());
    }

    @Test
    public void getClassTimesTest() {
        Set<Integer> days = new HashSet<>();
        days.add(4);
        classTest1.addClassTimes(calendarTimeTest1, 1, 0, 9, 0, days);
        assertEquals(1, classTest1.getClassTimes().size());
        Calendar givenValue = classTest1.getClassTimes().get(0).getStartTime();
        assertEquals(2021, givenValue.get(Calendar.YEAR));
        assertEquals(10, givenValue.get(Calendar.MONTH));
        assertEquals(4, givenValue.get(Calendar.DAY_OF_WEEK));
        assertEquals(9, givenValue.get(Calendar.HOUR));
    }

    @Test
    public void addClassTimesTestEntireWeek() {
        Set<Integer> days = new HashSet<>();
        for (int i = 1; i < 8; i++) {
            days.add(i);
        }
        classTest1.addClassTimes(calendarTimeTest1, 1, 0, 9, 0, days);
        assertEquals(7, classTest1.getClassTimes().size());
        for (int i = 7; i < 14; i++) {
            CalendarTime givenValue = classTest1.getClassTimes().get(i - 7);
            assertEquals(2021, givenValue.getStartTime().get(Calendar.YEAR));
            assertEquals(10, givenValue.getStartTime().get(Calendar.MONTH));
            assertEquals(i, givenValue.getStartTime().get(Calendar.DAY_OF_MONTH));
            assertEquals(9, givenValue.getStartTime().get(Calendar.HOUR));
        }
    }

    @Test
    public void addClassTimesTestMWF() {
        Set<Integer> days = new HashSet<>();
        days.add(2);
        days.add(4);
        days.add(6);
        classTest1.addClassTimes(calendarTimeTest1, 1, 0, 9, 0, days);
        assertEquals(3, classTest1.getClassTimes().size());
        for (int i = 2; i < 7; i += 2) {
            CalendarTime testTime = new CalendarTime(2021, 10, i, 9, 0,
                    2021, 10, i, 10, 0);
            CalendarTime givenValue = classTest1.getClassTimes().get((i / 2) - 1);
            assertEquals(2021, givenValue.getStartTime().get(Calendar.YEAR));
            assertEquals(10, givenValue.getStartTime().get(Calendar.MONTH));
            assertEquals(i, givenValue.getStartTime().get(Calendar.DAY_OF_WEEK));
            assertEquals(9, givenValue.getStartTime().get(Calendar.HOUR));
        }
    }

    @Test
    public void addClassTimesTestTT() {
        Set<Integer> days = new HashSet<>();
        days.add(3);
        days.add(5);
        classTest1.addClassTimes(calendarTimeTest1, 1, 0, 9, 0, days);
        assertEquals(2, classTest1.getClassTimes().size());
        for (int i = 3; i < 6; i += 2) {
            CalendarTime testTime = new CalendarTime(2021, 10, i, 9, 0,
                    2021, 10, i, 10, 0);
            int index = ((i + 1) / 2) - 2;
            CalendarTime givenValue = classTest1.getClassTimes().get(index);
            assertEquals(2021, givenValue.getStartTime().get(Calendar.YEAR));
            assertEquals(10, givenValue.getStartTime().get(Calendar.MONTH));
            assertEquals(i, givenValue.getStartTime().get(Calendar.DAY_OF_WEEK));
            assertEquals(9, givenValue.getStartTime().get(Calendar.HOUR));
        }
    }

    @Test
    public void getAssignmentsForRangeTest() {
        classTest1.addAssignment(assignmentTest1);
        List<Assignment> givenValue = classTest1.getAssignmentsForRange(calendarTimeTest4);
        List<Assignment> expectedValue = new ArrayList<>();
        expectedValue.add(assignmentTest1);
        assertEquals(1, givenValue.size());
        assertEquals(expectedValue, givenValue);
    }

    @Test
    public void getExamsForRangeTest() {
        classTest1.addExam(examTest1);
        List<Exam> givenValue = classTest1.getExamsForRange(calendarTimeTest4);
        List<Exam> expectedValue = new ArrayList<>();
        expectedValue.add(examTest1);
        assertEquals(1, givenValue.size());
        assertEquals(expectedValue, givenValue);
    }

    @Test
    public void findAssignmentTest() {
        assertEquals(null, classTest1.findAssignment("test"));
        classTest1.addAssignment(assignmentTest1);
        assertEquals(null, classTest1.findAssignment("test"));
        assertEquals("aTest1", classTest1.findAssignment("aTest1").getEventName());
    }
}
