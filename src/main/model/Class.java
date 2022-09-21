package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a single class that student takes
public class Class implements Writable {

    private final String className;
    private final String profName;
    private final ArrayList<Assignment> assignments;
    private final ArrayList<Exam> exams;
    private final ArrayList<CalendarTime> classTimes;

    // REQUIRES: className to be non-zero length
    //           profName to be non-zero length
    // EFFECTS: initiates Class
    public Class(String className, String profName) {
        this.className = className;
        this.profName = profName;
        assignments = new ArrayList<>();
        exams = new ArrayList<>();
        classTimes = new ArrayList<>();
    }

    // Getters
    public String getClassName() {
        return className;
    }

    public String getProfName() {
        return profName;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public ArrayList<CalendarTime> getClassTimes() {
        return classTimes;
    }

    // MODIFIES: this
    // EFFECTS: adds assignment to Class
    public void addAssignment(Assignment a) {
        assignments.add(a);
        EventLog.getInstance().logEvent(new Event("Added " + a.getEventName() + " to " + this.getClassName()));
    }

    // MODIFIES: this
    // EFFECTS: adds exam to Class
    public void addExam(Exam e) {
        exams.add(e);
        EventLog.getInstance().logEvent(new Event("Added " + e.getEventName() + " to " + this.getClassName()));
    }

    // REQUIRES: ct to start at 12am on the given day
    //           days: days of week [1, 7]
    //           classStart & classDuration: class must start and end on same day
    //           classStart: integer [0, 23]
    // MODIFIES: this
    // EFFECTS: adds all class days
    public void addClassTimes(CalendarTime ct, int classDurationHour, int classDurationMin,
                              int classStartHour, int classStartMin, Set<Integer> days) {
        Calendar startTime = ct.getStartTime();
        Calendar endTime = ct.getEndTime();
        Calendar classStartTime = new GregorianCalendar(startTime.get(Calendar.YEAR),
                startTime.get(Calendar.MONTH),
                startTime.get(Calendar.DAY_OF_MONTH),
                classStartHour,
                classStartMin);
        Calendar classEndTime = (Calendar) classStartTime.clone();
        classEndTime.add(Calendar.HOUR, classDurationHour);
        classEndTime.add(Calendar.MINUTE, classDurationMin);

        while (classEndTime.before(endTime)) {
            if (days.contains(classStartTime.get(Calendar.DAY_OF_WEEK))) {
                Calendar classStartTimeClone = (Calendar) classStartTime.clone();
                Calendar classEndTimeClone = (Calendar) classEndTime.clone();
                CalendarTime newCalendarTime = new CalendarTime(classStartTimeClone, classEndTimeClone);
                classTimes.add(newCalendarTime);
            }
            classStartTime.add(Calendar.DAY_OF_MONTH, 1);
            classEndTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        EventLog.getInstance().logEvent(new Event("Added class times to " + this.getClassName()));
    }

    // MODIFIES: this
    // EFFECTS: merges class times
    public void mergeClassTimes(ArrayList<CalendarTime> classTimes) {
        this.classTimes.addAll(classTimes);
        EventLog.getInstance().logEvent(new Event("Loaded class times into " + this.getClassName()));
    }

    // EFFECTS: returns assignments within CalendarTime range
    public List<Assignment> getAssignmentsForRange(CalendarTime calendarTime) {
        Calendar startTime = calendarTime.getStartTime();
        Calendar endTime = calendarTime.getEndTime();
        List<Assignment> outputAssignments = new ArrayList<>();

        for (Assignment a : assignments) {
            Calendar assignmentStartTime = a.getEventDueDate().getStartTime();
            Calendar assignmentEndTime = a.getEventDueDate().getEndTime();

            if (assignmentStartTime.after(startTime) && assignmentEndTime.before(endTime)) {
                outputAssignments.add(a);
            }
        }
        return outputAssignments;
    }

    // EFFECTS: returns exams within CalendarTime range
    public List<Exam> getExamsForRange(CalendarTime calendarTime) {
        Calendar startTime = calendarTime.getStartTime();
        Calendar endTime = calendarTime.getEndTime();
        List<Exam> outputExams = new ArrayList<>();

        for (Exam e : exams) {
            Calendar examStartTime = e.getEventDueDate().getStartTime();
            Calendar examEndTime = e.getEventDueDate().getEndTime();

            if (examStartTime.after(startTime) && examEndTime.before(endTime)) {
                outputExams.add(e);
            }
        }
        return outputExams;
    }

    // REQUIRES: Assignment w/ assignmentName has to exist
    // EFFECTS: returns requested assignment
    public Assignment findAssignment(String assignmentName) {
        for (Assignment a : assignments) {
            if (a.getEventName().equals(assignmentName)) {
                return a;
            }
        }
        return null;
    }

    // EFFECTS: creates JSON object for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("className", className);
        json.put("profName", profName);
        json.put("assignments", assignmentsToJson());
        json.put("exams", examsToJson());
        json.put("classTimes", classTimesToJson());
        return json;
    }

    // EFFECTS: creates JSON object for assignments
    private JSONArray assignmentsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Assignment a : assignments) {
            jsonArray.put(a.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: creates JSON object for exams
    private JSONArray examsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Exam e : exams) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: creates JSON object for class times
    private JSONArray classTimesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (CalendarTime ct : classTimes) {
            jsonArray.put(ct.toJson());
        }

        return jsonArray;
    }
}
