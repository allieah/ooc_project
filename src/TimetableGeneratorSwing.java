import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimetableGeneratorSwing extends JFrame {
    private int days = 0;
    private int numSubjects = 0;
    private List<Subject> subjects = new ArrayList<>();
    private List<String> classTimings = new ArrayList<>();
    private Timetable timetable = null;

    public TimetableGeneratorSwing() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Timetable Generator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);

       


        JMenuItem exitMenuItem = new JMenuItem("Exit");
        optionsMenu.add(exitMenuItem);

        

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Make the text area read-only
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        
        JButton generateButton = new JButton("Generate Timetable");
        JButton modifyButton = new JButton("Modify Timetable");
        JButton displayButton = new JButton("Display Timetable");
        JButton extraClassButton = new JButton("Add Extra Class");
        JButton commentButton = new JButton("Add Comment");
        JButton exitButton = new JButton("Exit");
        JButton reschedduleButton = new JButton("reschadule");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generateButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(extraClassButton);
        buttonPanel.add(commentButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(reschedduleButton);
        add(buttonPanel, BorderLayout.SOUTH);

        
    
    generateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateTimetable();
            displayTimetable(textArea);
        }
    });

    modifyButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            modifyTimetable(textArea);
        }
    });

    displayButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayTimetable(textArea);
        }
    });

    extraClassButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addExtraClass(textArea);
        }
    });

    commentButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            comment(textArea);
        }
    });
     exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        reschedduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                rescheduleClassesWithUnavailableDays(textArea);
            }
        });
}
    

    private void generateTimetable() {
        
        
            JTextField daysField = new JTextField();
            JTextField numSubjectsField = new JTextField();
        
            JPanel inputPanel = new JPanel(new GridLayout(0, 2));
            inputPanel.add(new JLabel("Enter the number of days in the timetable:"));
            inputPanel.add(daysField);
            inputPanel.add(new JLabel("Enter the number of subjects:"));
            inputPanel.add(numSubjectsField);
        
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Generate Timetable", JOptionPane.OK_CANCEL_OPTION);
        
            if (result == JOptionPane.OK_OPTION) {
                try {
                    days = Integer.parseInt(daysField.getText());
                    numSubjects = Integer.parseInt(numSubjectsField.getText());
        
                    if (days <= 0 || numSubjects <= 0) {
                        JOptionPane.showMessageDialog(this, "Please enter valid numbers for days and subjects.");
                        return;
                    }
        
                    subjects.clear(); // Clear any existing subjects
                    classTimings.clear(); // Clear any existing class timings
        
                    for (int i = 0; i < numSubjects; i++) {
                        JTextField nameField = new JTextField();
                        JTextField creditsField = new JTextField();
        
                        JPanel subjectPanel = new JPanel(new GridLayout(0, 2));
                        subjectPanel.add(new JLabel("Enter subject name for Subject " + (i + 1) + ":"));
                        subjectPanel.add(nameField);
                        subjectPanel.add(new JLabel("Enter subject credits for Subject " + (i + 1) + ":"));
                        subjectPanel.add(creditsField);
        
                        int subjectResult = JOptionPane.showConfirmDialog(this, subjectPanel, "Subject Details", JOptionPane.OK_CANCEL_OPTION);
        
                        if (subjectResult != JOptionPane.OK_OPTION) {
                            JOptionPane.showMessageDialog(this, "Subject details for Subject " + (i + 1) +"were not entered.");
                            return;
                        }
        
                        String subjectName = nameField.getText();
                        int subjectCredits = Integer.parseInt(creditsField.getText());
        
                        if (subjectCredits <= 0) {
                            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for subject credits.");
                            return;
                        }
        
                        subjects.add(new Subject(subjectName, subjectCredits));
                    }
        
                    classTimings.add("9:30 AM - 10:30 AM");
                    classTimings.add("10:30 AM - 11:30 AM");
                    classTimings.add("11:30 AM - 12:30 PM");
                    classTimings.add("01:30 PM - 02:30 PM");
                    classTimings.add("02:30 PM - 03:30 PM");
                    classTimings.add("03:30 PM - 04:30 PM");
                    classTimings.add("04:30 PM - 05:30 PM");
        
                    timetable = new Timetable(days, classTimings.size());
        
                    Random random = new Random();
                    int subjectIndex = 0;
        
                    for (int day = 0; day < days; day++) {
                        List<String> assignedSubjects = new ArrayList<>(); // To keep track of assigned subjects
        
                        for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
                            Subject subject = subjects.get(subjectIndex);
        
                            if (subject.credits > 0) {
                                // Check if the slot is available and before 12:30
                                if (classIndex < 3 && timetable.getClass(day, classIndex) == null) {
                                    // Check if the subject is not already assigned on the same day
                                    if (!assignedSubjects.contains(subject.name)) {
                                        timetable.addClass(day, classIndex, subject.name);
                                        subject.credits--;
                                        assignedSubjects.add(subject.name);
                                    }
                                }
                            }
        
                            subjectIndex = (subjectIndex + 1) % numSubjects;
                        }
                    }
        
                    JOptionPane.showMessageDialog(this, "Timetable generated successfully.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers for days and subjects.");
                }
            }
        
        
        
        
    }

    private void modifyTimetable(JTextArea textArea) {
        // Implement your code for modifying the timetable here
        // You can use the 'timetable' object and update it as needed.
        // After modifications, update the text area using 'textArea.setText()' and 'displayTimetable()'.
        
    if (timetable == null) {
        JOptionPane.showMessageDialog(this, "Please generate the timetable first.");
    } else {
        int modifyOption = JOptionPane.showOptionDialog(
            this,
            "1. Modify existing class\n2. Cancel a class (set to null)",
            "Modify Timetable",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[] { "Modify", "Cancel" },
            "Modify"
        );

        if (modifyOption == 0) {
            String dayToModifyStr = JOptionPane.showInputDialog(this, "Enter the day to modify (1-" + days + "):");
            String classIndexToModifyStr = JOptionPane.showInputDialog(this, "Enter the class index to modify (1-" + classTimings.size() + "):");

            try {
                int dayToModify = Integer.parseInt(dayToModifyStr);
                int classIndexToModify = Integer.parseInt(classIndexToModifyStr);

                dayToModify--; // Convert to 0-based index
                classIndexToModify--; // Convert to 0-based index

                if (dayToModify < 0 || dayToModify >= days || classIndexToModify < 0 || classIndexToModify >= classTimings.size()) {
                    JOptionPane.showMessageDialog(this, "Invalid day or class index. Please try again.");
                } else {
                    String currentClass = timetable.getClass(dayToModify, classIndexToModify);
                    if (currentClass == null) {
                        JOptionPane.showMessageDialog(this, "Cannot modify a null slot. Please try again.");
                    } else {
                        String newSubjectName = JOptionPane.showInputDialog(this, "Enter the new subject name:");
                        timetable.addClass(dayToModify, classIndexToModify, newSubjectName);
                        JOptionPane.showMessageDialog(this, "Class modified.");
                        displayTimetable(textArea);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for day and class index.");
            }
        } else if (modifyOption == 1) {
            String dayToCancelStr = JOptionPane.showInputDialog(this, "Enter the day to cancel a class (1-" + days + "):");
            String classIndexToCancelStr = JOptionPane.showInputDialog(this, "Enter the class index to cancel (1-" + classTimings.size() + "):");

            try {
                int dayToCancel = Integer.parseInt(dayToCancelStr);
                int classIndexToCancel = Integer.parseInt(classIndexToCancelStr);

                dayToCancel--; // Convert to 0-based index
                classIndexToCancel--; // Convert to 0-based index

                if (dayToCancel < 0 || dayToCancel >= days || classIndexToCancel < 0 || classIndexToCancel >= classTimings.size()) {
                    JOptionPane.showMessageDialog(this, "Invalid day or class index. Please try again.");
                } else {
                    String currentClass = timetable.getClass(dayToCancel, classIndexToCancel);
                    if (currentClass == null) {
                        JOptionPane.showMessageDialog(this, "The selected slot is already null.");
                    } else {
                        timetable.addClass(dayToCancel, classIndexToCancel, null);
                        JOptionPane.showMessageDialog(this, "Class canceled.");
                        displayTimetable(textArea);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for day and class index.");
            }
        }
    }
   

            
        
    }

    private void displayTimetable(JTextArea textArea) {
        if (timetable == null) {
            textArea.setText("Please generate the timetable first.");
        } else {
            // Create a panel for the timetable
            JPanel timetablePanel = new JPanel(new GridLayout(days + 1, classTimings.size() + 1));
    
            // Add a border to the timetable panel
            timetablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            // Create labels for days and class timings
            timetablePanel.add(new JLabel("Time/Day"));
            for (String timing : classTimings) {
                JLabel timingLabel = new JLabel(timing);
                timingLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                timetablePanel.add(timingLabel);
            }
    
            // Populate the timetable panel with data
            for (int day = 0; day < days; day++) {
                JLabel dayLabel = new JLabel("Day " + (day + 1));
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                timetablePanel.add(dayLabel);
    
                for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
                    String className = timetable.getClass(day, classIndex);
                    JLabel classLabel = new JLabel(className != null ? className : "FREE");
                    classLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
                    // Set a blue background for non-free classes
                    if (className != null) {
                        classLabel.setOpaque(true);
                        classLabel.setBackground(new Color(173, 216, 230)); // Light blue color

                    }
    
                    timetablePanel.add(classLabel);
                }
            }
    
            // Create a scroll pane for the timetable panel
            JScrollPane scrollPane = new JScrollPane(timetablePanel);
    
            // Create a new frame to display the timetable
            JFrame timetableFrame = new JFrame("Timetable");
            timetableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            timetableFrame.setSize(800, 600); // Adjust the size as needed
            timetableFrame.setLocationRelativeTo(this); // Center the frame relative to the main frame
            timetableFrame.add(scrollPane);
            timetableFrame.setVisible(true);
        }
    }
    
    
    

    private void addExtraClass(JTextArea textArea) {
        // Implement your code for adding an extra class here
        // You can use the 'timetable' object and update it as needed.
        // After adding an extra class, update the text area using 'textArea.setText()' and 'displayTimetable()'.
        
    if (timetable == null) {
        JOptionPane.showMessageDialog(this, "Please generate the timetable first.");
    } else {
        String dayToAddClassStr = JOptionPane.showInputDialog(this, "Enter the day to add an extra class (1-" + days + "):");
        
        try {
            int dayToAddClass = Integer.parseInt(dayToAddClassStr);
            dayToAddClass--; // Convert to 0-based index

            if (dayToAddClass < 0 || dayToAddClass >= days) {
                JOptionPane.showMessageDialog(this, "Invalid day. Please try again.");
            } else {
                String hoursToAddStr = JOptionPane.showInputDialog(this, "Enter the number of hours for the extra class:");
                int hoursToAdd = Integer.parseInt(hoursToAddStr);

                boolean canAddClass = true;
                int classIndexToAdd = -1;

                for (int i = 0; i < classTimings.size(); i++) {
                    System.out.println((i + 1) + ". " + classTimings.get(i));
                }

                String selectedTimeStr = JOptionPane.showInputDialog(this, "Select the starting time for the extra class (1-" + classTimings.size() + "):");
                int selectedTime = Integer.parseInt(selectedTimeStr);

                if (selectedTime < 1 || selectedTime > classTimings.size()) {
                    JOptionPane.showMessageDialog(this, "Invalid starting time selection. Please try again.");
                } else {
                    classIndexToAdd = selectedTime - 1;

                    if (classIndexToAdd + hoursToAdd > classTimings.size()) {
                        JOptionPane.showMessageDialog(this, "Not enough time slots available for the specified duration.");
                    } else {
                        for (int i = classIndexToAdd; i < classIndexToAdd + hoursToAdd; i++) {
                            if (timetable.getClass(dayToAddClass, i) != null) {
                                canAddClass = false;
                                break;
                            }
                        }

                        if (canAddClass) {
                            String subjectNameToAdd = JOptionPane.showInputDialog(this, "Enter the subject name for the extra class:");

                            for (int i = classIndexToAdd; i < classIndexToAdd + hoursToAdd; i++) {
                                timetable.addClass(dayToAddClass, i, subjectNameToAdd);
                            }

                            JOptionPane.showMessageDialog(this, "Extra class added to the timetable.");
                            displayTimetable(textArea);
                        } else {
                            JOptionPane.showMessageDialog(this, "Cannot add the class at the provided time. The slots are not available.");
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for day, hours, and class index.");
        }
    }
    

    }
    public void comment(JTextArea textArea) {
        if (timetable == null) {
            JOptionPane.showMessageDialog(this, "Please generate the timetable first.");
        } else {
            String dayToAddClassStr = JOptionPane.showInputDialog(this, "Enter the day to add a comment (1-" + days + "):");
    
            try {
                int dayToAddComment = Integer.parseInt(dayToAddClassStr);
                dayToAddComment--; // Convert to 0-based index
    
                if (dayToAddComment < 0 || dayToAddComment >= days) {
                    JOptionPane.showMessageDialog(this, "Invalid day. Please try again.");
                } else {
                    String indexToAddCommentStr = JOptionPane.showInputDialog(this, "Enter the time slot index to add a comment (1-" + classTimings.size() + "):");
    
                    try {
                        int indexToAddComment = Integer.parseInt(indexToAddCommentStr);
                        indexToAddComment--; // Convert to 0-based index
    
                        if (indexToAddComment < 0 || indexToAddComment >= classTimings.size()) {
                            JOptionPane.showMessageDialog(this, "Invalid time slot index. Please try again.");
                        } else {
                            String className = timetable.getClass(dayToAddComment, indexToAddComment);
                            String comment = JOptionPane.showInputDialog(this, "Enter a comment for Day " + (dayToAddComment + 1) + " at " + classTimings.get(indexToAddComment) + ":");
    
                            JLabel classLabel = new JLabel(className != null ? className : "");
                            
                            // Set red color for comments
                            classLabel.setForeground(Color.RED);
    
                            if (className != null) {
                                classLabel.setText(className + " (" + comment + ")");
                            } else {
                                classLabel.setText(comment);
                            }
    
                            timetable.addClass(dayToAddComment, indexToAddComment, classLabel.getText());
    
                            JOptionPane.showMessageDialog(this, "Comment added to the timetable.");
                            displayTimetable(textArea);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid time slot index.");
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid day number.");
            }
        }
    }
    
    private void rescheduleClassesWithUnavailableDays(JTextArea textArea) {
        if (timetable == null) {
            JOptionPane.showMessageDialog(this, "Please generate the timetable first.");
        } else {
            // Prompt user for preferred days
            String preferenceInput = JOptionPane.showInputDialog(this, "Enter days you prefer to take lectures (comma-separated, e.g., 2,4,6):");
    
            List<Integer> preferredDays = new ArrayList<>();
            try {
                if (preferenceInput != null && !preferenceInput.trim().isEmpty()) {
                    String[] preferredDaysStr = preferenceInput.split(",");
                    for (String dayStr : preferredDaysStr) {
                        int day = Integer.parseInt(dayStr.trim()) - 1; // Convert to 0-based index
                        if (day >= 0 && day < days) {
                            preferredDays.add(day);
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid day input for preferences. Ignoring preferences.");
                            return;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No preferences provided. Aborting operation.");
                    return;
                }
    
                // Prompt user for preferred subjects
                String subjectsInput = JOptionPane.showInputDialog(this, "Enter subjects you prefer (comma-separated, e.g., Math,Science):");
    
                List<String> preferredSubjects = new ArrayList<>();
                if (subjectsInput != null && !subjectsInput.trim().isEmpty()) {
                    String[] subjectsArray = subjectsInput.split(",");
                    for (String subject : subjectsArray) {
                        preferredSubjects.add(subject.trim());
                    }
                }
    
                // Reschedule classes considering preferences
                for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
                    for (int preferredDay : preferredDays) {
                        if (timetable.getClass(preferredDay, classIndex) == null) {
                            // Find the first available slot on the preferred day
                            for (int day = 0; day < days; day++) {
                                if (timetable.getClass(day, classIndex) != null) {
                                    String className = timetable.getClass(day, classIndex);
                                    if (preferredSubjects.isEmpty() || preferredSubjects.contains(className)) {
                                        timetable.addClass(day, classIndex, null);
                                        timetable.addClass(preferredDay, classIndex, className);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
    
                JOptionPane.showMessageDialog(this, "Classes rescheduled considering preferences and subjects.");
                displayTimetable(textArea);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input format. Please enter valid day numbers.");
            }
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TimetableGeneratorSwing app = new TimetableGeneratorSwing();
                app.setVisible(true);
            }
        });
    }

    class Subject {
        String name;
        int credits;

        public Subject(String name, int credits) {
            this.name = name;
            this.credits = credits;
        }
    }

    class Timetable {
        String[][] schedule;

        public Timetable(int days, int classTimings) {
            schedule = new String[days][classTimings];
        }

        public void addClass(int day, int classIndex, String className) {
            schedule[day][classIndex] = className;
        }

        public String getClass(int day, int classIndex) {
            return schedule[day][classIndex];
        }

        public void displayTimetable(JTextArea textArea) {
            // Implement your code for displaying the timetable in the text area
        }
    }
}
