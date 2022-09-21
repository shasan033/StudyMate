package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents an assignment for a class
public class Assignment extends CalendarEvent {

    private final String description;
    private int progression;

    // REQUIRES: assignmentName to be non-zero length
    // EFFECTS: initiates Assignment
    public Assignment(String assignmentName, CalendarTime dueDate, String description) {
        super(assignmentName, dueDate);
        this.description = description;
        progression = 0;
    }

    // EFFECTS: overload constructor
    public Assignment(String assignmentName, CalendarTime dueDate, String description, int progression) {
        super(assignmentName, dueDate);
        this.description = description;
        this.progression = progression;
    }

    // REQUIRES: newProgression to be [0, 100]
    // MODIFIES: this
    // EFFECTS: sets progression for assignment
    public void setProgression(int newProgression) {
        progression = newProgression;
    }

    // EFFECTS: returns assignment description
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns assignment progression
    public int getProgression() {
        return progression;
    }

    // EFFECTS: creates JSON object for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("assignmentName", eventName);
        json.put("assignmentDueDate", eventDueDate.toJson());
        json.put("description", description);
        json.put("progression", progression);
        return json;
    }
}
