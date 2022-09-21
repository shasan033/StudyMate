package ui;

import model.CalendarTime;
import model.Class;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

// Represents the UI that adds classes to timetable
public class AddClassUI extends JPanel implements DateToCalendar {

    GridBagConstraints constraint;

    JTextField className;
    JTextField profName;
    JSpinner startDate;
    JSpinner endDate;
    JSpinner startTime;
    JSpinner endTime;
    JList daySelect;

    Map<String, Integer> dayToInteger;

    // MODIFIES: this
    // EFFECTS: initializes entire UI
    public AddClassUI() {
        setLayout(new GridBagLayout());
        constraint = new GridBagConstraints();
        initializeMap();
        initializeLabels();
        initializeTextFields();
        initializeSpinners();
        initializeList();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: generates hashmap of day of week to integer val
    private void initializeMap() {
        dayToInteger = new HashMap<>();
        String[] days = {"Su", "M", "Tu", "W", "Th", "F", "Sa"};
        for (int i = 1; i < 8; i++) {
            dayToInteger.put(days[i - 1], i);
        }
    }

    // EFFECTS: initializes construction of labels for UI
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void initializeLabels() {
        JLabel classNameText = new JLabel("Class Name: ");
        constraint.gridx = 0;
        constraint.gridy = 0;
        add(classNameText, constraint);

        JLabel profNameText = new JLabel("Professor's Name: ");
        constraint.gridy = 1;
        add(profNameText, constraint);

        JLabel startDateText = new JLabel("Class Start Date: ");
        constraint.gridy = 2;
        add(startDateText, constraint);

        JLabel endDateText = new JLabel("Class End Date: ");
        constraint.gridy = 4;
        add(endDateText, constraint);

        JLabel startTimeText = new JLabel("Class Start Time: ");
        constraint.gridy = 6;
        add(startTimeText, constraint);

        JLabel endTimeText = new JLabel("Class End Time: ");
        constraint.gridx = 1;
        constraint.gridy = 6;
        add(endTimeText, constraint);

        JLabel daysOfWeek = new JLabel("Days of week that class is on: ");
        constraint.gridx = 0;
        constraint.gridy = 8;
        add(daysOfWeek, constraint);
    }

    // EFFECTS: initializes construction of text fields UI
    private void initializeTextFields() {
        className = new JTextField(10);
        constraint.gridx = 1;
        constraint.gridy = 0;
        add(className, constraint);

        profName = new JTextField(10);
        constraint.gridy = 1;
        add(profName, constraint);
    }

    // EFFECTS: initializes construction of spinners UI
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void initializeSpinners() {
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();

        SpinnerDateModel startDateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        SpinnerDateModel endDateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        SpinnerDateModel startTimeModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.HOUR);
        SpinnerDateModel endTimeModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.HOUR);

        startDate = new JSpinner(startDateModel);
        endDate = new JSpinner(endDateModel);
        startTime = new JSpinner(startTimeModel);
        endTime = new JSpinner(endTimeModel);

        startDate.setEditor(new JSpinner.DateEditor(startDate, "dd/MM/yyyy"));
        endDate.setEditor((new JSpinner.DateEditor(endDate, "dd/MM/yyyy")));
        startTime.setEditor(new JSpinner.DateEditor(startTime, "HH:mm"));
        endTime.setEditor(new JSpinner.DateEditor(endTime, "HH:mm"));

        constraint.gridwidth = 2;
        constraint.gridx = 0;
        constraint.gridy = 3;
        add(startDate, constraint);

        constraint.gridy = 5;
        add(endDate, constraint);

        constraint.gridwidth = 1;
        constraint.gridy = 7;
        add(startTime, constraint);

        constraint.gridx = 1;
        add(endTime, constraint);
    }

    // EFFECTS: initializes construction of JList UI
    private void initializeList() {
        String[] days = {"Su", "M", "Tu", "W", "Th", "F", "Sa"};
        daySelect = new JList(days);
        daySelect.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        daySelect.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        daySelect.setVisibleRowCount(1);

        constraint.gridx = 0;
        constraint.gridy = 9;
        constraint.gridwidth = 2;
        add(daySelect, constraint);
    }

    // EFFECTS: parses input data and returns it
    // throws ParseException if date input cannot be parsed
    public Class getData() throws ParseException {
        Class newClass = new Class(className.getText(), profName.getText());
        addClassTimeToClass(newClass);
        return newClass;
    }

    // EFFECTS: parses date data and adds it to class
    // throws ParseException if date input cannot be parsed
    private void addClassTimeToClass(Class inputClass) throws ParseException {
        Calendar startDateValue = dateToCalendar((Date) startDate.getValue());
        Calendar endDateValue = dateToCalendar((Date) endDate.getValue());
        startDateValue.set(Calendar.HOUR, 0);
        startDateValue.set(Calendar.MINUTE, 0);
        endDateValue.set(Calendar.HOUR, 23);
        endDateValue.set(Calendar.MINUTE, 59);

        int startHourValue = dateToCalendar((Date) startTime.getValue()).get(Calendar.HOUR);
        int startMinuteValue = dateToCalendar((Date) startTime.getValue()).get(Calendar.MINUTE);
        int durationHourValue = dateToCalendar((Date) endTime.getValue()).get(Calendar.HOUR) - startHourValue;
        int durationMinuteValue = dateToCalendar((Date) endTime.getValue()).get(Calendar.MINUTE) - startMinuteValue;

        CalendarTime range = new CalendarTime(startDateValue, endDateValue);
        Set<Integer> days = getClassDays();
        inputClass.addClassTimes(range, durationHourValue, durationMinuteValue, startHourValue, startMinuteValue, days);
    }

    // EFFECTS: parses JList and returns Calendar compatible values
    private Set<Integer> getClassDays() {
        List<String> daySelectString = (List<String>) daySelect.getSelectedValuesList();
        Set<Integer> daysInteger = new HashSet<>();
        for (String s : daySelectString) {
            daysInteger.add(dayToInteger.get(s));
        }
        return daysInteger;
    }
}
