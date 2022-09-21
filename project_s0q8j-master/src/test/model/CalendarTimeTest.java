package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTimeTest {

    private CalendarTime calendarTime;

    @BeforeEach
    public void setUp() {
        calendarTime = new CalendarTime(2021,
                10,
                14,
                9,
                0,
                2021,
                10,
                14,
                10,
                0 );
    }
}