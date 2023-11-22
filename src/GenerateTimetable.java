


import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//import TimetableGenerator.Subject;
//import TimetableGenerator.Timetable;

public class GenerateTimetable extends JFrame{
	Connection con;
	DefaultTableModel model = new DefaultTableModel();
	
	 private int days = 0;
	    private int numSubjects = 0;
	    private List<Subject> subjects = new ArrayList<>();
	    private List<String> classTimings = new ArrayList<>();
	    private Timetable timetable ;
	   

	

    void generateTimetable() {
        
        
            JTextField daysField = new JTextField();
            JTextField numSubjectsField = new JTextField();
            JTextField classNameField = new JTextField();
            JPanel inputPanel = new JPanel(new GridLayout(0, 2));
            inputPanel.add(new JLabel("Enter the number of days in the timetable:"));
            inputPanel.add(daysField);
            inputPanel.add(new JLabel("Enter the number of subjects:"));
            inputPanel.add(numSubjectsField);
            inputPanel.add(new JLabel("Enter the class name:"));
           inputPanel.add(classNameField);

            int classResult = JOptionPane.showConfirmDialog(this, inputPanel, "Class Details", JOptionPane.OK_CANCEL_OPTION);

            if (classResult != JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(this, "Class name not entered. Cannot generate timetable.");
                return;
            }

            String className = classNameField.getText().trim();

            if (className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid class name.");
                return;
            }

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
//            int classId = 0; // Initialize classId here
            try {
                connect();

                int classId = retrieveClassId(className);

                // Ensure classId is initialized before calling insertTimetable
                if (classId != -1) {
                    insertTimetable(classId, timetable.getTimetableData());
                    JOptionPane.showMessageDialog(this, "Timetable generated and stored in the database successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error retrieving class ID. Cannot store timetable data.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error storing timetable data in the database.");
            }
        
        
    }

	//*****

    void displayTimetable(JTextArea textArea) {
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
    private int retrieveClassId(String className) throws SQLException {
        int classId = -1; // Initialize classId to a default value

        String query = "SELECT class_id FROM class WHERE name = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, className);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    classId = rs.getInt("class_id");
                }
            }
        }

        return classId;
    }
	
	
	public void connect() throws SQLException {
		//ENTER PORT, USER, PASSWORD.
		//String str1 = "SELECT * FROM user WHERE prio = 1";
		String url = "jdbc:mysql://localhost:3306/timetable";
		String user = "root";
		String pass = "W7301@jqir#A";

		con = DriverManager.getConnection(url, user, pass);
	}
	
	public void insertTimetable(int classId, List<String> timetableData) {
	    String insertQuery = "INSERT INTO timetable_data (class_id, day, timing, subject) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
	        for (int day = 0; day < timetableData.size(); day++) {
	            for (int classTime = 0; classTime < timetableData.get(day).length(); classTime++) {
	                String subject = timetableData.get(day).charAt(classTime) + "";
	                pst.setInt(1, classId);
	                pst.setString(2,  "Day " + (day + 1));
	                pst.setInt(3, classTime + 1);
	                pst.setString(4, subject);
	                pst.executeUpdate();
	            }
	        }
	        System.out.println("Timetable data inserted successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error inserting timetable data: " + e.getMessage());
	    }
	}

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
    private String[][] timetableData;
    public Timetable(int days, int classTimings) {
        schedule = new String[days][classTimings];
        timetableData = new String[days][classTimings]; // Initialize the timetableData array
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
    

    public List<String> getTimetableData() {
        List<String> timetableList = new ArrayList<>();

        for (int day = 0; day < schedule.length; day++) {
            StringBuilder row = new StringBuilder();
            for (int classTime = 0; classTime < schedule[day].length; classTime++) {
                String subject = schedule[day][classTime];
                row.append(subject != null ? subject : " ");
            }
            timetableList.add(row.toString());
        }

        return timetableList;
    }
    public List<String> scheduleToList() {
        List<String> timetableList = new ArrayList<>();

        for (int day = 0; day < schedule.length; day++) {
            StringBuilder row = new StringBuilder();
            for (int classTime = 0; classTime < schedule[day].length; classTime++) {
                String subject = schedule[day][classTime];
                row.append(subject != null ? subject : " ");
            }
            timetableList.add(row.toString());
        }

        return timetableList;
    }

   
}