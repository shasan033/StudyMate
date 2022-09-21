package model;

import exceptions.NoAssignmentExistsException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents TimeTable with all classes being taken
public class TimeTable implements Writable {

    private final ArrayList<Class> classes;

    // EFFECTS: initiates TimeTable
    public TimeTable() {
        classes = new ArrayList<>();
    }

    // Getter
    public ArrayList<Class> getClasses() {
        return classes;
    }

    // MODIFIES: this
    // EFFECTS: adds class to TimeTable
    public void addClass(Class newClass) {
        classes.add(newClass);
        EventLog.getInstance().logEvent(new Event("Added " + newClass.getClassName()));
    }

    // EFFECTS: returns classes within CalendarTime range
    public ArrayList<Class> getClassesForRange(CalendarTime calendarTime) {
        Calendar startTime = calendarTime.getStartTime();
        Calendar endTime = calendarTime.getEndTime();
        ArrayList<Class> outputClasses = new ArrayList<>();

        for (Class c : classes) {
            for (CalendarTime ct : c.getClassTimes()) {
                if (ct.getStartTime().after(startTime) && ct.getEndTime().before(endTime)) {
                    outputClasses.add(c);
                }
            }
        }
        return outputClasses;
    }

    // EFFECTS: returns assignments within range
    public ArrayList<Assignment> getAssignmentsForRange(CalendarTime calendarTime) {
        ArrayList<Assignment> outputAssignments = new ArrayList<>();
        for (Class c : classes) {
            outputAssignments.addAll(c.getAssignmentsForRange(calendarTime));
        }
        return outputAssignments;
    }

    // EFFECTS: returns exams within range
    public ArrayList<Exam> getExamsForRange(CalendarTime calendarTime) {
        ArrayList<Exam> outputExams = new ArrayList<>();
        for (Class c : classes) {
            outputExams.addAll(c.getExamsForRange(calendarTime));
        }
        return outputExams;
    }

    // REQUIRES: existing Class w/ className
    // EFFECTS: returns Class with associated className
    public Class findClass(String className) {
        for (Class c : classes) {
            if (c.getClassName().equals(className)) {
                return c;
            }
        }
        return null;
    }

    // REQUIRES: existing assignment w/ assignment name
    // EFFECTS: returns assignment w/ associated name
    public Assignment findAssignment(String assignmentName) throws NoAssignmentExistsException {
        for (Class c : classes) {
            for (Assignment a : c.getAssignments()) {
                if (a.getEventName().equals(assignmentName)) {
                    return a;
                }
            }
        }
        throw new NoAssignmentExistsException();
    }

    // EFFECTS: creates JSON object for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("classes", classesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray classesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Class c : classes) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }
}
