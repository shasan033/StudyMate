package ui;

import model.Class;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// Represents abstract UI interface that adds an event
public abstract class EventUI extends JPanel implements DateToCalendar {

    ArrayList<Class> classList;

    GridBagConstraints constraint;

    JTextField eventName;
    JSpinner dueDate;
    JComboBox classSelection;

    Map<String, Integer> dayToInteger;

    // MODIFIES: this
    // EFFECTS: constructs main structure of event UI
    public EventUI(ArrayList<Class> classList) {
        this.classList = classList;
        setLayout(new GridBagLayout());
        constraint = new GridBagConstraints();
        initializeMap();
        initializeLabels();
        initializeComboBox();
        initializeTextFields();
        initializeSpinners();
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
    private void initializeLabels() {
        JLabel eventNameText = new JLabel("Name: ");
        constraint.gridx = 0;
        constraint.gridy = 0;
        add(eventNameText, constraint);

        JLabel dueDateText = new JLabel("Due Date: ");
        constraint.gridy = 1;
        add(dueDateText, constraint);

        JLabel classSelectionText = new JLabel("Class: ");
        constraint.gridy = 3;
        add(classSelectionText, constraint);
    }

    // EFFECTS: initializes construction of combo boxes for UI
    private void initializeComboBox() {
        String[] classNames = new String[classList.size() + 1];
        for (int i = 0; i < classList.size(); i++) {
            classNames[i] = classList.get(i).getClassName();
        }
        classNames[classList.size()] = "No class";
        classSelection = new JComboBox(classNames);
        classSelection.setSelectedIndex(classList.size());

        constraint.gridx = 1;
        constraint.gridy = 3;
        add(classSelection, constraint);
    }

    // EFFECTS: initializes construction of text fields for UI
    private void initializeTextFields() {
        eventName = new JTextField(10);
        constraint.gridx = 1;
        constraint.gridy = 0;
        add(eventName, constraint);
    }

    // EFFECTS: initializes construction of spinners for UI
    private void initializeSpinners() {
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();

        SpinnerDateModel dueDateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);

        dueDate = new JSpinner(dueDateModel);

        dueDate.setEditor(new JSpinner.DateEditor(dueDate, "HH:mm/dd/MM/yyyy"));

        constraint.gridwidth = 2;
        constraint.gridx = 0;
        constraint.gridy = 2;
        add(dueDate, constraint);
    }

    // EFFECTS: returns combo box selection
    public String getChosenClass() {
        return (String) classSelection.getSelectedItem();
    }
}
