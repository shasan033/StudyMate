package ui;

import model.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Class;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

// Code adapted from:
// - CPSC 210 DrawingEditor sample (used for general layout)

// Represents the UI interface of StudyMateApp
public class StudyMateAppUI extends JFrame implements ActionListener, MouseListener {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 500;
    private static final String JSON_STORE = "./data/SavedData.json";
    private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
    private static final ImageIcon checkMarkImage = new ImageIcon("./data/checkmark.png");

    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    TimeTable tt;

    JPanel centerPanel;
    JTextArea textArea;

    JTable table;
    JLabel calendarText;
    JButton goBack;
    JButton goForward;

    JMenuBar menuBar;
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem exitItem;
    JMenuItem classItem;
    JMenuItem assignmentItem;
    JMenuItem examItem;

    JScrollPane classInfo;

    Calendar today;

    // EFFECTS: initializes main UI interface
    public StudyMateAppUI() {
        super("StudyMate");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        tt = new TimeTable();
        initializeGraphics();
    }

    // EFFECTS: initializes main window graphics UI
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);

        // Added log printing when "X" is pressed
        // Mostly generated from built-in IntelliJ autocomplete
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                super.windowClosing(e);
            }
        });

        setLocationRelativeTo(null);

        initializeDate();
        initializeCenterPanel();
        initializeMenuBar();
        initializeCalendar();
        initializeInfoMenu();
        setVisible(true);
    }

    // EFFECTS: initializes today w/ current date
    private void initializeDate() {
        today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
    }

    // EFFECTS: constructs the layout for the center portion of the app
    private void initializeCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2));
        add(centerPanel, BorderLayout.CENTER);
    }

    // EFFECTS: constructs the menu bar UI
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu addMenu = new JMenu("Add");
        //JMenu findMenu = new JMenu("Find");
        initializeMenuItems();

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        addMenu.add(classItem);
        addMenu.add(assignmentItem);
        addMenu.add(examItem);
        menuBar.add(fileMenu);
        menuBar.add(addMenu);

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        classItem.addActionListener(this);
        assignmentItem.addActionListener(this);
        examItem.addActionListener(this);
        add(menuBar, BorderLayout.NORTH);
    }

    // EFFECTS: constructs the specific menu bar items
    private void initializeMenuItems() {
        loadItem = new JMenuItem("Load");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        classItem = new JMenuItem("Class");
        assignmentItem = new JMenuItem("Assignment");
        examItem = new JMenuItem("Exam");
    }

    // EFFECTS: aligns the calendar portion of the UI
    private void initializeCalendar() {
        initializeCalendarTable();

        JPanel topCalendar = initializeTopCalendar();
        JPanel totalCalendar = new JPanel(new BorderLayout());

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        calendarPanel.add(table, BorderLayout.CENTER);
        totalCalendar.add(topCalendar, BorderLayout.NORTH);
        totalCalendar.add(calendarPanel, BorderLayout.CENTER);
        centerPanel.add(totalCalendar);
    }

    // EFFECTS: constructs the calendar UI
    private void initializeCalendarTable() {
        Object[][] data = generateCalendarValues(today);
        table = new JTable(data, DAYS_OF_WEEK) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(65);
        table.addMouseListener(this);
    }

    // EFFECTS: updates the calendar UI
    private void updateCalendar() {
        DefaultTableModel newData = new DefaultTableModel(generateCalendarValues(today), DAYS_OF_WEEK);
        table.setModel(newData);

        int year = today.get(Calendar.YEAR);
        String month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
        calendarText.setText(month + " " + year);
    }

    // EFFECTS: constructs the buttons and label at the top of the calendar
    private JPanel initializeTopCalendar() {
        int year = today.get(Calendar.YEAR);
        String month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
        calendarText = new JLabel(month + " " + year);
        calendarText.setHorizontalAlignment(JLabel.CENTER);

        goBack = new JButton("<-");
        goBack.addActionListener(this);
        goBack.setFocusable(false);

        goForward = new JButton("->");
        goForward.addActionListener(this);
        goForward.setFocusable(false);

        JPanel topCalendar = new JPanel(new BorderLayout());
        topCalendar.add(goBack, BorderLayout.WEST);
        topCalendar.add(calendarText, BorderLayout.CENTER);
        topCalendar.add(goForward, BorderLayout.EAST);
        return topCalendar;
    }

    // EFFECTS: generates the day values of a month
    private Object[][] generateCalendarValues(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

        Object[][] data = new Object[weeksInMonth][7];
        int pos = dayOfWeek - 1;
        for (int day = 1; day <= daysInMonth; day++) {
            data[pos / 7][pos % 7] = day;
            pos += 1;
        }
        return data;
    }

    // EFFECTS: constructs the information panel
    private void initializeInfoMenu() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(generateDayInfo(today));
        textArea.setEditable(false);
        textArea.setFont(new Font(null, Font.PLAIN, 20));
        classInfo = new JScrollPane(textArea);

        String month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
        int numDay = today.get(Calendar.DAY_OF_MONTH);
        int year = today.get(Calendar.YEAR);
        JLabel dateText = new JLabel("Today is " + month + " " + numDay + ", " + year);

        infoPanel.add(dateText, BorderLayout.NORTH);
        infoPanel.add(classInfo, BorderLayout.CENTER);
        centerPanel.add(infoPanel);
    }

    // EFFECTS: generates classes, assignments and exams in a day
    private String generateDayInfo(Calendar calendar) {
        CalendarTime singleDay = generateCalendarDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        String text = "";

        //CalendarTime singleDay = new CalendarTime(calendar, endTime);
        ArrayList<Class> classesForDay = tt.getClassesForRange(singleDay);
        ArrayList<Assignment> assignmentsForDay = tt.getAssignmentsForRange(singleDay);
        ArrayList<Exam> examsForDay = tt.getExamsForRange(singleDay);

        text += "What's happening on " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
        text += " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR) + "?:\n";
        text += "===================================\n";
        text += getClassText(classesForDay);
        text += getAssignmentText(assignmentsForDay);
        text += getExamText(examsForDay);

        return text;
    }

    // EFFECTS: generates output text for given classes
    private String getClassText(ArrayList<Class> classesForDay) {
        String text = "Classes:\n---------------------------------------------------------\n";
        if (classesForDay.size() == 0) {
            text += "No classes\n";
        } else {
            for (Class c : classesForDay) {
                text += c.getClassName() + " with " + c.getProfName() + "\n";
            }
        }
        return text;
    }

    // EFFECTS: generates output text for given assignments
    private String getAssignmentText(ArrayList<Assignment> assignmentsForDay) {
        String text = "Assignments:\n---------------------------------------------------------\n";
        if (assignmentsForDay.size() == 0) {
            text += "No assignments\n";
        } else {
            for (Assignment a : assignmentsForDay) {
                text += a.getEventName() + ": " + a.getProgression() + "% completed" + "\n";
            }
        }
        return text;
    }

    // EFFECTS: generates output text for given exams
    private String getExamText(ArrayList<Exam> examsForDay) {
        String text = "Exams:\n---------------------------------------------------------\n";
        if (examsForDay.size() == 0) {
            text += "No exams\n";
        } else {
            for (Exam e : examsForDay) {
                text += e.getEventName() + ": worth " + e.getWeight() + "%" + "\n";
            }
        }
        return text;
    }

    // EFFECTS: generates CalendarTime encompassing the range of a single day
    private CalendarTime generateCalendarDay(int year, int month, int dayOfMonth) {
        Calendar startTime = new GregorianCalendar(year, month, dayOfMonth, 0, 0);
        Calendar endTime = new GregorianCalendar(year, month, dayOfMonth, 23, 59);
        startTime.add(Calendar.MINUTE, -1);
        endTime.add(Calendar.MINUTE, 1);

        return new CalendarTime(startTime, endTime);
    }

    // MODIFIES: this
    // EFFECTS: action listener for events occurring through app
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem || e.getSource() == loadItem || e.getSource() == saveItem) {
            fileMenuPerformed(e);
        } else if (e.getSource() == classItem) {
            addClassMenu();
        } else if (e.getSource() == assignmentItem) {
            addAssignmentMenu();
        } else if (e.getSource() == examItem) {
            addExamMenu();
        } else {
            buttonPerformed(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: generates class addition dialog and adds class to timetable
    private void addClassMenu() {
        Object[] options = {"Add", "Cancel"};
        AddClassUI addClass = new AddClassUI();
        int n = JOptionPane.showOptionDialog(this, addClass,
                "Add a new class",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (n) {
            case -1:
            case 1:
                break;
            case 0:
                try {
                    tt.addClass(addClass.getData());
                } catch (ParseException exception) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding class",
                            "Error: Add a new class",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: generates assignment addition dialog and adds assignment to timetable
    private void addAssignmentMenu() {
        Object[] options = {"Add", "Cancel"};
        AddAssignmentUI addAssignment = new AddAssignmentUI(tt.getClasses());
        int n = JOptionPane.showOptionDialog(this, addAssignment,
                "Add a new assignment",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (n) {
            case -1:
            case 1:
                break;
            case 0:
                try {
                    Class chosenClass = tt.findClass(addAssignment.getChosenClass());
                    int indexOfClass = tt.getClasses().indexOf(chosenClass);
                    Assignment newAssignment = addAssignment.getData();
                    tt.getClasses().get(indexOfClass).addAssignment(newAssignment);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding assignment", "Error: Add a new assignment",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: generates exam addition dialog and adds exam to timetable
    private void addExamMenu() {
        Object[] options = {"Add", "Cancel"};
        AddExamUI addExam = new AddExamUI(tt.getClasses());
        int n = JOptionPane.showOptionDialog(this, addExam,
                "Add a new exam",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (n) {
            case -1:
            case 1:
                break;
            case 0:
                try {
                    Class chosenClass = tt.findClass(addExam.getChosenClass());
                    int indexOfClass = tt.getClasses().indexOf(chosenClass);
                    Exam newExam = addExam.getData();
                    tt.getClasses().get(indexOfClass).addExam(newExam);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding assignment", "Error: Add a new assignment",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: performs functions in file menu
    private void fileMenuPerformed(ActionEvent e) {
        if (e.getSource() == exitItem) {
            printLog(EventLog.getInstance());
            System.exit(0);
        } else if (e.getSource() == loadItem) {
            loadTimeTable();
        } else if (e.getSource() == saveItem) {
            saveTimeTable();
        }
    }

    // EFFECTS: prints out event log of application
    private void printLog(EventLog el) {
        for (Event e : el) {
            System.out.println(e.toString());
        }
    }

    // MODIFIES: this
    // EFFECTS: switches calendar month and updates UI
    public void buttonPerformed(ActionEvent e) {
        if (e.getSource() == goBack) {
            today.add(Calendar.MONTH, -1);
            today.set(Calendar.DAY_OF_MONTH, 1);
            updateCalendar();
            textArea.setText(generateDayInfo(today));
        } else if (e.getSource() == goForward) {
            today.add(Calendar.MONTH, 1);
            today.set(Calendar.DAY_OF_MONTH, 1);
            updateCalendar();
            textArea.setText(generateDayInfo(today));
        }
    }

    // MODIFIES: this
    // EFFECTS: mouse listener for mouse press events
    @Override
    public void mousePressed(MouseEvent e) {
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        try {
            Integer value = (Integer) table.getValueAt(row, column);
            Calendar selectedValue = new GregorianCalendar(year, month, value);
            textArea.setText("");
            textArea.setText(generateDayInfo(selectedValue));
        } catch (NullPointerException exception) {
            // do nothing
        }
    }

    // EFFECTS: mouse listener for click events
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // EFFECTS: mouse listener for mouse release events
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // EFFECTS: mouse listener for mouse entering events
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // EFFECTS: mouse listener for mouse exiting events
    @Override
    public void mouseExited(MouseEvent e) {
    }

    // MODIFIES: ./data/SavedData.json
    // EFFECTS: saves state of timetable to JSON
    private void saveTimeTable() {
        try {
            jsonWriter.open();
            jsonWriter.write(tt);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this,
                    "Data successfully saved",
                    "Saved",
                    JOptionPane.PLAIN_MESSAGE);
            EventLog.getInstance().logEvent(new Event("Saved data to save file"));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to write to file",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE,
                    checkMarkImage);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads saved state of timetable
    private void loadTimeTable() {
        try {
            tt = jsonReader.read();
            JOptionPane.showMessageDialog(this,
                    "Data successfully loaded",
                    "Loaded",
                    JOptionPane.PLAIN_MESSAGE,
                    checkMarkImage);
            EventLog.getInstance().logEvent(new Event("Loaded data from save file"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to read file, or file missing",
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
