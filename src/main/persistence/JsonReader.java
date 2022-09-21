package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

import model.Assignment;
import model.CalendarTime;
import model.Exam;
import model.TimeTable;
import model.Class;
import org.json.*;

// Adapted from CPSC 210 JsonSerializationDemo
// Represents a reader that reads TimeTable from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads TimeTable from file and returns it;
    // throws IOException if an error occurs reading data from file
    public TimeTable read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTimeTable(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses TimeTable from JSON object and returns it
    private TimeTable parseTimeTable(JSONObject jsonObject) {
        TimeTable tt = new TimeTable();
        addClasses(tt, jsonObject);
        return tt;
    }

    // MODIFIES: tt
    // EFFECTS: parses classes from JSON object and adds them to TimeTable
    private void addClasses(TimeTable tt, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("classes");
        for (Object json : jsonArray) {
            JSONObject nextClass = (JSONObject) json;
            addClass(tt, nextClass);
        }
    }

    // MODIFIES: tt
    // EFFECTS: parses class from JSON object and adds it to TimeTable
    private void addClass(TimeTable tt, JSONObject jsonObject) {
        String className = jsonObject.getString("className");
        String profName = jsonObject.getString("profName");
        ArrayList<Assignment> assignments = parseAssignments(jsonObject.getJSONArray("assignments"));
        ArrayList<Exam> exams = parseExams(jsonObject.getJSONArray("exams"));
        ArrayList<CalendarTime> classTimes = parseClassTimes(jsonObject.getJSONArray("classTimes"));

        Class newClass = new Class(className, profName);
        newClass.mergeClassTimes(classTimes);
        for (Assignment a : assignments) {
            newClass.addAssignment(a);
        }
        for (Exam e : exams) {
            newClass.addExam(e);
        }

        tt.addClass(newClass);
    }

    // EFFECTS: parses assignments from JSON array and returns it
    private ArrayList<Assignment> parseAssignments(JSONArray jsonArray) {
        ArrayList<Assignment> parsedAssignments = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextAssignment = (JSONObject) json;
            parsedAssignments.add(addAssignment(nextAssignment));
        }
        return parsedAssignments;
    }

    // EFFECTS: parses assignment and returns it
    private Assignment addAssignment(JSONObject jsonObject) {
        String name = jsonObject.getString("assignmentName");
        CalendarTime dueDate = parseCalendarTime(jsonObject.getJSONObject("assignmentDueDate"));
        String description = jsonObject.getString("description");
        int progression = jsonObject.getInt("progression");
        return new Assignment(name, dueDate, description, progression);
    }

    // EFFECTS: parses exams from JSON array and returns it
    private ArrayList<Exam> parseExams(JSONArray jsonArray) {
        ArrayList<Exam> parsedExams = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextExam = (JSONObject) json;
            parsedExams.add(addExam(nextExam));
        }
        return parsedExams;
    }

    // EFFECTS: parses exam and returns it
    private Exam addExam(JSONObject jsonObject) {
        String name = jsonObject.getString("examName");
        CalendarTime examDate = parseCalendarTime(jsonObject.getJSONObject("examDate"));
        int weight = jsonObject.getInt("weight");
        return new Exam(name, examDate, weight);
    }

    // EFFECTS: parses class times from JSON array and returns it
    private ArrayList<CalendarTime> parseClassTimes(JSONArray jsonArray) {
        ArrayList<CalendarTime> classTimes = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextValue = (JSONObject) json;
            classTimes.add(parseCalendarTime(nextValue));
        }
        return classTimes;
    }

    // EFFECTS: parses CalendarTime from JSON object and returns it
    private CalendarTime parseCalendarTime(JSONObject jsonObject) {
        Calendar startTime = null;
        Calendar endTime = null;

        JSONArray jsonArray = jsonObject.getJSONArray("dueDate");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject newValue = (JSONObject) jsonArray.get(i);
            if (i == 0) {
                startTime = addTime(newValue);
            } else {
                endTime = addTime(newValue);
            }
        }
        return new CalendarTime(startTime, endTime);
    }

    // EFFECTS: parses Calendar from JSON object and returns it
    private Calendar addTime(JSONObject jsonObject) {
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        int day = jsonObject.getInt("day");
        int hour = jsonObject.getInt("hour");
        int min = jsonObject.getInt("minute");
        return new GregorianCalendar(year, month, day, hour, min);
    }
}