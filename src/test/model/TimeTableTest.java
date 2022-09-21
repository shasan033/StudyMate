package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimeTableTest {

    private TimeTable timeTableTest;
    private Class classTest1;
    private Class classTest2;
    private Class classTest3;
    private Assignment assignmentTest1;
    private Assignment assignmentTest2;
    private Exam examTest1;
    private Exam examTest2;
    private CalendarTime calendarTimeTest1;
    private CalendarTime calendarTimeTest2;
    private CalendarTime calendarTimeTest3;
    private CalendarTime calendarTimeTest4;
    private CalendarTime calendarTimeTest5;

    @BeforeEach
    public void setUp() {
        /// Entire second week of November
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
        // First day of December
        calendarTimeTest5 = new CalendarTime(2021, 11, 1, 8, 0,
                2021, 11, 1, 8, 0);

        examTest1 = new Exam("Test1", calendarTimeTest2, 45);
        examTest2 = new Exam("Test2", calendarTimeTest5, 50);
        assignmentTest1 = new Assignment("aTest1", calendarTimeTest2, "TestDescription");
        assignmentTest2 = new Assignment("aTest2", calendarTimeTest5, "TestDescription2");

        classTest1 = new Class("Class1", "Prof1");
        classTest2 = new Class("Class2", "Prof2");
        classTest3 = new Class("Class3", "Prof3");

        timeTableTest = new TimeTable();
    }

    @Test
    public void getClassesTest() {
        timeTableTest.addClass(classTest1);
        timeTableTest.addClass(classTest2);
        timeTableTest.addClass(classTest3);
        ArrayList<Class> expectedValue = new ArrayList<>();
        expectedValue.add(classTest1);
        expectedValue.add(classTest2);
        expectedValue.add(classTest3);
        assertEquals(expectedValue, timeTableTest.getClasses());
    }

    @Test
    public void getClassesForRangeTest() {
        Set<Integer> days = new HashSet<>();
        days.add(2);
        days.add(4);
        days.add(6);
        classTest1.addClassTimes(calendarTimeTest1, 1, 0, 9, 0, days);
        timeTableTest.addClass(classTest1);
        assertEquals(classTest1, timeTableTest.getClassesForRange(calendarTimeTest1).get(0));
        try {
            timeTableTest.getClassesForRange(calendarTimeTest4).get(0);
        } catch (IndexOutOfBoundsException e) {
            // pass
        }
    }

    @Test
    public void getAssignmentsForRangeTest() {
        classTest1.addAssignment(assignmentTest1);
        classTest1.addAssignment(assignmentTest2);
        ArrayList<Assignment> expectedValue = new ArrayList<>();
        expectedValue.add(assignmentTest1);
        timeTableTest.addClass(classTest1);
        assertEquals(expectedValue, timeTableTest.getAssignmentsForRange(calendarTimeTest4));
    }

    @Test
    public void getExamsForRangeTest() {
        classTest1.addExam(examTest1);
        classTest1.addExam(examTest2);
        ArrayList<Exam> expectedValue = new ArrayList<>();
        expectedValue.add(examTest1);
        timeTableTest.addClass(classTest1);
        assertEquals(expectedValue, timeTableTest.getExamsForRange(calendarTimeTest4));
    }

    @Test
    public void findClassTest() {
        timeTableTest.addClass(classTest1);
        assertEquals(classTest1, timeTableTest.findClass("Class1"));
        assertEquals(null, timeTableTest.findClass("Blah"));
    }
}
