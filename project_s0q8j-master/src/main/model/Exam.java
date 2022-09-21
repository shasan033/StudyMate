package model;

import org.json.JSONObject;

// Represents an exam for a class
public class Exam extends CalendarEvent {

    private final int weight;

    // REQUIRES: examName to be non-zero length
    //           weight to be [0, 100]
    // EFFECTS: initiates Exam
    public Exam(String examName, CalendarTime examDate, int weight) {
        super(examName, examDate);
        this.weight = weight;
    }

    // EFFECTS: returns exam weight
    public int getWeight() {
        return weight;
    }

    // EFFECTS: creates JSON object for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("examName", eventName);
        json.put("examDate", eventDueDate.toJson());
        json.put("weight", weight);
        return json;
    }
}