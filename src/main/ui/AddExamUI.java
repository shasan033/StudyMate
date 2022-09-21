package ui;

import model.CalendarTime;
import model.Class;
import model.Exam;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// Represents the UI that adds exams to classes
public class AddExamUI extends EventUI {

    JTextField durationField;
    JTextField weightField;

    // EFFECTS: initializes entire UI
    public AddExamUI(ArrayList<Class> classList) {
        super(classList);
        initializeNewLabels();
        initializeNewTextFields();
    }

    // EFFECTS: initializes construction of labels for UI
    private void initializeNewLabels() {
        JLabel durationText = new JLabel("Duration (min): ");
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.gridx = 0;
        constraint.gridy = 4;
        add(durationText, constraint);

        JLabel weightText = new JLabel("Weight: ");
        constraint.gridy = 5;
        add(weightText, constraint);
    }

    // EFFECTS: initializes construction of labels for UI
    private void initializeNewTextFields() {
        durationField = new JTextField(3);
        constraint.anchor = GridBagConstraints.LINE_END;
        constraint.gridx = 1;
        constraint.gridy = 4;
        add(durationField, constraint);


        weightField = new JTextField(3);
        constraint.gridy = 5;
        add(weightField, constraint);
    }

    // EFFECTS: parses input data and returns it
    // throws ParseException if data cannot be parsed
    // throws NumberFormatException if number input cannot be parsed
    public Exam getData() throws ParseException, NumberFormatException {
        Calendar examDateValue;

        int durationValue = Integer.parseInt(durationField.getText());
        int weightValue = Integer.parseInt(weightField.getText());
        examDateValue = dateToCalendar((Date) dueDate.getValue());

        Calendar endDateValue = (Calendar) examDateValue.clone();
        endDateValue.add(Calendar.MINUTE, durationValue);

        CalendarTime examDate = new CalendarTime(examDateValue, endDateValue);

        return new Exam(eventName.getText(), examDate, weightValue);
    }
}
