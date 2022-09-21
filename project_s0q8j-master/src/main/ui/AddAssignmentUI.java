package ui;

import model.Assignment;
import model.CalendarTime;
import model.Class;

import javax.swing.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// Represents the UI that adds assignments to classes
public class AddAssignmentUI extends EventUI {

    JTextArea descriptionText;

    // EFFECTS: initializes the entire UI
    public AddAssignmentUI(ArrayList<Class> classList) {
        super(classList);
        initializeNewLabels();
        initializeTextAreas();
    }

    // EFFECTS: initializes construction of labels for UI
    private void initializeNewLabels() {
        JLabel descriptionText = new JLabel("Description: ");
        constraint.gridx = 0;
        constraint.gridy = 4;
        add(descriptionText, constraint);
    }

    // EFFECTS: initializes construction of text areas for UI
    private void initializeTextAreas() {
        descriptionText = new JTextArea(5, 10);
        JScrollPane scrollPane = new JScrollPane(descriptionText);
        constraint.gridwidth = 2;
        constraint.gridheight = 2;
        constraint.gridx = 0;
        constraint.gridy = 5;
        add(scrollPane, constraint);
    }

    // EFFECTS: parses input data and returns it
    // throws ParseException if data cannot be parsed
    public Assignment getData() throws ParseException {
        Calendar dueDateValue = dateToCalendar((Date) dueDate.getValue());

        CalendarTime assignmentDueDate = new CalendarTime(dueDateValue, dueDateValue);

        return new Assignment(eventName.getText(), assignmentDueDate, descriptionText.getText());
    }
}
