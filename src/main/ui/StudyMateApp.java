package ui;

import model.*;
import model.Class;
import exceptions.NoAssignmentExistsException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Represents the console interface of StudyMateApp
public class StudyMateApp {

    private static final String JSON_STORE = "./data/SavedData.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Scanner input;
    private TimeTable tt;
    private CalendarTime today;
    private String currentDayOfWeekName;
    private String currentMonthName;

    // EFFECTS: Constructs TimeTable and runs StudyMateApp
    public StudyMateApp() {
        runStudyMateApp();
    }

    // MODIFIES: this
    // EFFECTS: processes commands and displays outputs
    public void runStudyMateApp() {
        boolean running = true;
        String command;

        init();
        displayCurrentDayInfo();

        while (running) {

            displayMenuOptions();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                running = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Goodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes current day and JSON features
    private void init() {
        tt = new TimeTable();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        currentDayOfWeekName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);
        currentMonthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);

        today = new CalendarTime(year, month, dayOfMonth, 0, 0,
                year, month, dayOfMonth, 23, 59);
    }

    // EFFECTS: displays current date, classes, assignments and exams
    private void displayCurrentDayInfo() {
        ArrayList<Class> classesToday = tt.getClassesForRange(today);
        ArrayList<Assignment> assignmentsToday = tt.getAssignmentsForRange(today);
        ArrayList<Exam> examsToday = tt.getExamsForRange(today);
        System.out.println("Today is: " + currentDayOfWeekName + ", " + currentMonthName + " "
                + today.getStartTime().get(Calendar.DAY_OF_MONTH)
                + ", " + today.getStartTime().get(Calendar.YEAR));
        displayCurrentClasses(classesToday);
        displayCurrentAssignments(assignmentsToday);
        displayCurrentExams(examsToday);
    }

    // EFFECTS: displays classes occurring today
    private void displayCurrentClasses(ArrayList<Class> classesToday) {
        System.out.println("\nYour classes for today are: ");
        if (classesToday.size() == 0) {
            System.out.println("none");
        } else {
            for (Class c : classesToday) {
                System.out.println(c.getClassName() + "\n");
            }
        }
    }

    // EFFECTS: displays assignments occurring today
    private void displayCurrentAssignments(ArrayList<Assignment> assignmentsToday) {
        System.out.println("Your assignments due today are: ");
        if (assignmentsToday.size() == 0) {
            System.out.println("none");
        } else {
            for (Assignment a : assignmentsToday) {
                System.out.println("\n" + a.getEventName());
            }
        }
    }

    // EFFECTS: displays exams occurring today
    private void displayCurrentExams(ArrayList<Exam> examsToday) {
        System.out.println("Your exams today are: ");
        if (examsToday.size() == 0) {
            System.out.println("none");
        } else {
            for (Exam e : examsToday) {
                System.out.println("\n" + e.getEventName());
            }
        }
    }

    // EFFECTS: displays main menu options to user
    private void displayMenuOptions() {
        System.out.println("What would you like to do?: ");
        System.out.println("1. Add a new class");
        System.out.println("2. Add a new assignment");
        System.out.println("3. Add a new exam");
        System.out.println("4. See upcoming exams for next week");
        System.out.println("5. See next 3 days of assignments");
        System.out.println("6. Set progression for an assignment");
        System.out.println("7. Save Classes");
        System.out.println("8. Load Classes");
    }

    // EFFECTS: processes input commands
    private void processCommand(String command) {
        switch (command) {
            case "1":
            case "2":
            case "3":
            case "4":
                processCommand2(command);
                break;
            case "5":
                displayNextAssignments(3);
                break;
            case "6":
                setAssignmentProgression();
                break;
            case "7":
                saveTimeTable();
                break;
            case "8":
                loadTimeTable();
                break;
            default:
                System.out.println("Selection not valid. Please try again!");
                break;
        }
    }

    // EFFECTS: displays assignments for next 3 days
    private void displayNextAssignments(int numDays) {
        ArrayList<Assignment> nextAssignments = getNextAssignments(numDays);
        if (nextAssignments.size() == 0) {
            System.out.println("No assignments for next 3 days!");
        } else {
            for (Assignment a : nextAssignments) {
                displaySingleAssignment(a);
            }
        }
    }

    // EFFECTS: displays a single assignment
    private void displaySingleAssignment(Assignment a) {
        System.out.println(a.getEventName());
        System.out.println(a.getDescription());
        System.out.println(a.getProgression() + "% completed");
        System.out.println("-----");
    }

    // EFFECTS: gets the next numDays assignments
    private ArrayList<Assignment> getNextAssignments(int numDays) {
        Calendar startTime = today.getStartTime();
        Calendar endTime = today.getEndTime();
        endTime.add(Calendar.DAY_OF_MONTH, numDays);
        CalendarTime nextThreeDays = new CalendarTime(startTime, endTime);
        return tt.getAssignmentsForRange(nextThreeDays);
    }

    // MODIFIES: model.Assignment
    // EFFECTS: processes user input for setting assignment progression
    private void setAssignmentProgression() {
        Assignment requestedAssignment;
        String command;
        while (true) {
            System.out.println("Choose one of the following assignments: ");
            displayAllAssignments();
            command = input.next();
            try {
                requestedAssignment = tt.findAssignment(command);
                break;
            } catch (NoAssignmentExistsException e) {
                System.out.println("Assignment selection invalid. Try again.");
            }
        }
        displaySingleAssignment(requestedAssignment);
        System.out.println("How far have you progressed so far (0 - 100)? ");
        command = input.next();
        int progression = Integer.parseInt(command);
        requestedAssignment.setProgression(progression);
        System.out.println("Progression set!");
    }

    // EFFECTS: displays all assignments
    private void displayAllAssignments() {
        ArrayList<Assignment> nextAssignments = getNextAssignments(365);
        if (nextAssignments.size() == 0) {
            System.out.println("No assignments added yet");
        } else {
            for (Assignment a : nextAssignments) {
                System.out.println(a.getEventName());
            }
        }
    }

    // EFFECTS: process addition of items commands
    private void processCommand2(String command) {
        switch (command) {
            case "1":
                addNewClass();
                break;
            case "2":
                addNewAssignment();
                break;
            case "3":
                addNewExam();
                displayCurrentExams(tt.getExamsForRange(today));
                break;
            case "4":
                displayNextExams();
                break;
            default:
                System.out.println("Selection not valid. Please try again!");
                break;
        }
    }

    // EFFECTS: processes user inputs to add new class
    private void addNewClass() {
        String command;
        String className;
        String profName;

        System.out.println("Please enter the class name: ");
        command = input.next();
        className = command;

        System.out.println("Please enter the professor's name: ");
        command = input.next();
        profName = command;

        Class newClass = new Class(className, profName);
        addNewClassTime(newClass);
        tt.addClass(newClass);
        System.out.println("Added new class");
        displayCurrentClasses(tt.getClassesForRange(today));
    }

    // EFFECTS: processes user input for adding class times
    private void addNewClassTime(Class inputClass) {
        String command;

        System.out.println("Does this class repeat on MWF (1), TuesThurs (2), or none of these (0)?: ");
        command = input.next();
        switch (command) {
            case "0":
                processMultipleClassTimeInputsMenu(inputClass);
                break;
            case "1":
                processClassTimeInputMenu(inputClass, 1);
                break;
            case "2":
                processClassTimeInputMenu(inputClass, 2);
                break;
            default:
                System.out.println("Input not valid");
                break;
        }
    }

    // REQUIRES: command to be in appropriate format
    // MODIFIES: inputClass
    // EFFECTS: processes user input to add class times
    private void processClassTimeInputMenu(Class inputClass, int repetition) {
        System.out.println("What is the start date of this class? (YYYY/MM/DD)");
        int[] startDate = parseStringIntoInteger(input.next(), "/");
        System.out.println("What is the end date of this class? (YYYY/MM/DD)");
        int[] endDate = parseStringIntoInteger(input.next(), "/");
        System.out.println("How long is this class? (HH/mm)");
        int[] duration = parseStringIntoInteger(input.next(), "/");
        System.out.println("What time does this class start? (HH/mm)");
        int[] startTime = parseStringIntoInteger(input.next(), "/");
        CalendarTime ct = new CalendarTime(startDate[0], startDate[1], startDate[2], 0, 0,
                endDate[0], endDate[1], endDate[2], 23, 59);
        Set<Integer> days = new HashSet<>();
        if (repetition == 1) {
            days.add(2);
            days.add(4);
            days.add(6);
        } else {
            days.add(3);
            days.add(5);
        }
        inputClass.addClassTimes(ct, duration[0], duration[1], startTime[0], startTime[1], days);
    }

    // EFFECTS: processes addition of multiple class times to a class
    private void processMultipleClassTimeInputsMenu(Class inputClass) {
        System.out.println("What is the start date of this class? (YYYY/MM/DD)");
        int[] startDate = parseStringIntoInteger(input.next(), "/");
        System.out.println("What is the end date of this class? (YYYY/MM/DD)");
        int[] endDate = parseStringIntoInteger(input.next(), "/");
        System.out.println("How long is this class? (HH/mm)");
        int[] duration = parseStringIntoInteger(input.next(), "/");
        System.out.println("What time does this class start? (HH/mm)");
        int[] startTime = parseStringIntoInteger(input.next(), "/");
        CalendarTime ct = new CalendarTime(startDate[0], startDate[1], startDate[2], 0, 0,
                endDate[0], endDate[1], endDate[2], 23, 59);
        Set<Integer> days = new HashSet<>();
        System.out.println("Which days of the week does this class repeat on? (0: Sat, 7: Sun)");
        int[] repeatDays = parseStringIntoInteger(input.next(), ",");
        for (Integer i : repeatDays) {
            days.add(i);
        }
        inputClass.addClassTimes(ct, duration[0], duration[1], startTime[0], startTime[1], days);
    }

    // REQUIRES: input to be separated by "/", other values to be parsable to int
    // EFFECTS: converts input string into int values
    private int[] parseStringIntoInteger(String input, String deliminator) {
        String[] stringArray = input.split(deliminator);
        int[] integerArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            integerArray[i] = Integer.parseInt(stringArray[i]);
        }
        return integerArray;
    }

    // EFFECTS: outputs exams occurring throughout next week
    private void displayNextExams() {
        ArrayList<Exam> nextExams = getExamsForNextWeek();
        try {
            for (Exam e : nextExams) {
                System.out.println(e.getEventName());
            }
        } catch (Exception e) {
            System.out.println("No exams for next week");
        }
    }

    // EFFECTS: gets exams occurring throughout next week
    private ArrayList<Exam> getExamsForNextWeek() {
        ArrayList<Exam> nextExams = new ArrayList<>();
        Calendar sevenDaysAhead = Calendar.getInstance();
        sevenDaysAhead.add(Calendar.DAY_OF_MONTH, 7);
        for (Class c : tt.getClasses()) {
            for (Exam e : c.getExams()) {
                Calendar endDate = e.getEventDueDate().getEndTime();
                if (sevenDaysAhead.before(endDate)) {
                    nextExams.add(e);
                }
            }
        }
        return nextExams;
    }

    // MODIFIES: model.Class
    // EFFECTS: adds assignment to user-requested class
    private void addNewAssignment() {
        String command;
        String assignmentName;
        String description;

        System.out.println("Which class is this assignment for?: ");
        command = input.next();
        Class requestedClass = tt.findClass(command);
        if (requestedClass == null) {
            System.out.println("Class not found");
            return;
        }

        System.out.println("Please enter the assignment name: ");
        command = input.next();
        assignmentName = command;

        System.out.println("Please enter a description: ");
        command = input.next();
        description = command;

        CalendarTime date = processEventTimeInputMenu();

        Assignment newAssignment = new Assignment(assignmentName, date, description);
        requestedClass.addAssignment(newAssignment);
        System.out.println("Added new assignment");
        displayCurrentAssignments(tt.getAssignmentsForRange(today));
    }

    // REQUIRES: command to be in proper format
    // EFFECTS: processes user input into CalendarTime
    private CalendarTime processEventTimeInputMenu() {
        String command;

        System.out.println("Please enter the event end date: -- YYYY/MM/DD/HH/MinMin");
        command = input.next();
        int[] arr1 = parseStringIntoInteger(command, "/");
        return new CalendarTime(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4],
                arr1[0], arr1[1], arr1[2], arr1[3], arr1[4]);
    }

    // MODIFIES: model.Class
    // EFFECTS: processes user input to create exam
    private void addNewExam() {
        String command;
        String examName;
        int examWeight;

        System.out.println("Which class is this exam for?:  ");
        Class requestedClass = tt.findClass(input.next());
        if (requestedClass == null) {
            System.out.println("Class not found");
            return;
        }

        System.out.println("Please enter the exam name: ");
        examName = input.next();

        System.out.println("Please enter the exam weight: ");
        command = input.next();
        try {
            examWeight = Integer.parseInt(command);
        } catch (Exception e) {
            System.out.println("Input not valid");
            return;
        }
        CalendarTime date = processEventTimeInputMenu();
        Exam newExam = new Exam(examName, date, examWeight);
        requestedClass.addExam(newExam);
        System.out.println("Added new exam");
    }

    // MODIFIES: ./data/SavedData.json
    // EFFECTS: saves timetable
    private void saveTimeTable() {
        try {
            jsonWriter.open();
            jsonWriter.write(tt);
            jsonWriter.close();
            System.out.println("Saved TimeTable!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file");
        }
    }

    // MODIFIES: tt
    // EFFECTS: reads from save file and loads timetable
    private void loadTimeTable() {
        try {
            tt = jsonReader.read();
            System.out.println("Loaded timetable!");
        } catch (IOException e) {
            System.out.println("Unable to read file, or file missing!");
        }
    }
}

