package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents a start time and end time range
public class CalendarTime implements Writable {

    private final Calendar startTime;
    private final Calendar endTime;

    // REQUIRES: startMonth and endMonth to be [1, 12]
    //           startDate and endDate to be [1, 31], and to correspond to appropriate month (ie for Jan, 31)
    //           startHour and endHour to be [0, 23]
    //           startMin and endMin to be [0, 59]
    //           all start values to be before end values
    // MODIFIES: this
    // EFFECTS: initiates CalendarTime
    public CalendarTime(int startYear, int startMonth, int startDate, int startHour, int startMin,
                        int endYear, int endMonth, int endDate, int endHour, int endMin) {
        startTime = new GregorianCalendar(startYear, startMonth, startDate, startHour, startMin);
        endTime = new GregorianCalendar(endYear, endMonth, endDate, endHour, endMin);
    }

    // REQUIRES: startTime to be before endTime
    // EFFECTS: overload for CalendarTime constructor, initiates CalendarTime
    public CalendarTime(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // EFFECTS: returns start time
    public Calendar getStartTime() {
        return startTime;
    }

    // EFFECTS: returns end time
    public Calendar getEndTime() {
        return endTime;
    }

    // EFFECTS: creates JSON object for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dueDate", calendarToJson(startTime, endTime));
        return json;
    }

    // EFFECTS: adds start and end time to JSON
    public JSONArray calendarToJson(Calendar startTime, Calendar endTime) {
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(timeToJson(startTime));
        jsonArray.put(timeToJson(endTime));

        return jsonArray;
    }

    // EFFECTS: adds Calendar to JSON
    public JSONObject timeToJson(Calendar time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("year", time.get(Calendar.YEAR));
        jsonObject.put("month", time.get(Calendar.MONTH));
        jsonObject.put("day", time.get(Calendar.DAY_OF_MONTH));
        jsonObject.put("hour", time.get(Calendar.HOUR));
        jsonObject.put("minute", time.get(Calendar.MINUTE));
        return jsonObject;
    }
}
