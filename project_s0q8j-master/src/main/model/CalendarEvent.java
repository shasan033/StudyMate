package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Abstract class for Assignment and Exam
public abstract class CalendarEvent implements Writable {
    protected String eventName;
    protected CalendarTime eventDueDate;

    // EFFECTS: initiates Event
    public CalendarEvent(String eventName, CalendarTime dueDate) {
        this.eventName = eventName;
        eventDueDate = dueDate;
    }

    // EFFECTS: returns event name
    public String getEventName() {
        return eventName;
    }

    // EFFECTS: returns due date
    public CalendarTime getEventDueDate() {
        return eventDueDate;
    }

    // EFFECTS: creates JSON object for this
    public abstract JSONObject toJson();
}
