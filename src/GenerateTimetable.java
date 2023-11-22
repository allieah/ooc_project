


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
import java.sql.Types;
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



public class GenerateTimetable extends JFrame {
	Connection con;
	DefaultTableModel model = new DefaultTableModel();
	  private int days = 0;
	    private int numSubjects = 0;
	    private List<Subject> subjects = new ArrayList<>();
	    private List<String> classTimings = new ArrayList<>();
	    private Timetable timetable = null;

	

    void generateTimetable() throws SQLException {
        
        
            JTextField daysField = new JTextField();
            JTextField numSubjectsField = new JTextField();
            JTextField clss = new JTextField();
        
            JPanel inputPanel = new JPanel(new GridLayout(0, 2));
            inputPanel.add(new JLabel("Enter the class:"));
            inputPanel.add(clss);
            inputPanel.add(new JLabel("Enter the number of days in the timetable:"));
            inputPanel.add(daysField);
            inputPanel.add(new JLabel("Enter the number of subjects:"));
            inputPanel.add(numSubjectsField);
  
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Generate Timetable", JOptionPane.OK_CANCEL_OPTION);
        
            if (result == JOptionPane.OK_OPTION) {
                try {
                    days = Integer.parseInt(daysField.getText());
                    numSubjects = Integer.parseInt(numSubjectsField.getText());
                    String className=clss.getText();
                    
                    System.out.println(className+"classesssss\n");
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
                    System.out.println(className+"class1\n");
                    int classId = retrieveClassId(className);
                    timetable = new Timetable(days, classTimings.size());
        
                    Random random = new Random();
                    int subjectIndex = 0;
        
                    connect();
                    String insertQuery = "INSERT INTO timetables (day, classId, t1, t2, t3, t4, t5, t6, t7) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                        for (int day = 0; day < days; day++) {
                            preparedStatement.setInt(1, day + 1); // Set day for the current batch

                            List<String> assignedSubjects = new ArrayList<>(); // To keep track of assigned subjects

                            for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
                                Subject subject = subjects.get(subjectIndex);

                                if (subject.credits > 0) {
                                    // Check if the subject is not already assigned on the same day
                                    if (!assignedSubjects.contains(subject.name)) {
                                        // Assign the subject to the time slot
                                        preparedStatement.setInt(2, classId); // Set classId (You need to implement generateUniqueId)
                                        preparedStatement.setString(classIndex + 3, subject.name); // +3 because the first two parameters are 'day' and 'classId'
                                        subject.credits--;
                                        assignedSubjects.add(subject.name);
                                    } else {
                                        // If subject is already assigned or no subject available, set the column value to NULL
                                        preparedStatement.setNull(classIndex + 3, Types.VARCHAR);
                                    }
                                } else {
                                    // If no subjects are available, set the column value to NULL
                                    preparedStatement.setNull(classIndex + 3, Types.VARCHAR);
                                }

                                subjectIndex = (subjectIndex + 1) % numSubjects;
                            }

                            preparedStatement.addBatch(); // Add the current batch
                        }

                        preparedStatement.executeBatch(); // Execute all batches
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
        
                    JOptionPane.showMessageDialog(this, "Timetable generated successfully.");
                  
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers for days and subjects.");
                }
            }
        
        
        
        
    }
   
	
    void displayTimetable(JTextArea textArea) throws SQLException {

  	  if (classTimings.size() ==0) {
  		  classTimings.add("9:30 AM - 10:30 AM");
            classTimings.add("10:30 AM - 11:30 AM");
            classTimings.add("11:30 AM - 12:30 PM");
            classTimings.add("01:30 PM - 02:30 PM");
            classTimings.add("02:30 PM - 03:30 PM");
            classTimings.add("03:30 PM - 04:30 PM");
            classTimings.add("04:30 PM - 05:30 PM");
  	  }
  	  days=3;
  	  
  	JTextField clss = new JTextField();
  	JPanel inputPanel = new JPanel(new GridLayout(0, 2));
  	inputPanel.add(new JLabel("Enter the class:"));
  	inputPanel.add(clss);
  	
  	int result = JOptionPane.showConfirmDialog(this, inputPanel, "Display Timetable", JOptionPane.OK_CANCEL_OPTION);
System.out.println(classTimings.size());
if (result == JOptionPane.OK_OPTION) {
    // Get the class name entered by the user
    String cName = clss.getText();

    // Output the class name to verify
    System.out.println(cName + " classesssss");

    // Now you can use cName to retrieve the classId
    int cId = retrieveClassId(cName);
    
    if (cId == -1) {
        // Class does not exist, show a warning
        JOptionPane.showMessageDialog(this, "Class does not exist. Please enter a valid class name.", "Warning", JOptionPane.WARNING_MESSAGE);
        return; // Exit the method if the class does not exist
    }
    if(Login.res==3) {
    	 if (!Students.c.equals(cName)) {
    	        System.out.println("You do not belong to this class");
    	        return;
    	    }
	  }
        	  try {
				connect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Make sure to connect to the database before retrieving data
        	  String selectQuery = "SELECT day, classId, t1, t2, t3, t4, t5, t6, t7 FROM timetables WHERE classId=?";

        	  try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
        		    preparedStatement.setInt(1, cId);
        		    ResultSet resultSet = preparedStatement.executeQuery();
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
    
            // Move the cursor to the first row
            boolean hasData = resultSet.next();
            // Populate the timetable panel with data
            for (int day = 0; day < days; day++) {
                JLabel dayLabel = new JLabel("Day " + (day + 1));
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                timetablePanel.add(dayLabel);
    
                for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
                    String className = resultSet.getString(classIndex + 3);
                    JLabel classLabel = new JLabel(className != null ? className : "FREE");
                    classLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
                    // Set a blue background for non-free classes
                    if (className != null) {
                        classLabel.setOpaque(true);
                        classLabel.setBackground(new Color(173, 216, 230)); // Light blue color

                    }
    
                    timetablePanel.add(classLabel);
                }
                // Move to the next row
        //        hasData = resultSet.next();
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
        	    } catch (SQLException e) {
        	        e.printStackTrace();
        	    }
        
}   
    }
    
    void editTimetable(JTextArea textArea) throws SQLException {
        JTextField clss = new JTextField();
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Enter the class:"));
        inputPanel.add(clss);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Timetable", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String className = clss.getText();
            int classId = retrieveClassId(className);

            if (classId == -1) {
                JOptionPane.showMessageDialog(this, "Class does not exist. Please enter a valid class name.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                connect();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String selectQuery = "SELECT day, classId, t1, t2, t3, t4, t5, t6, t7 FROM timetables WHERE classId=?";
            String updateQuery = "UPDATE timetables SET t1=?, t2=?, t3=?, t4=?, t5=?, t6=?, t7=? WHERE day=? AND classId=?";

            try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
                 PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {

                selectStatement.setInt(1, classId);
                ResultSet resultSet = selectStatement.executeQuery();

               JPanel editPanel = new JPanel(new GridLayout(days + 1, classTimings.size() + 1));
//                editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
               editPanel.add(new JLabel("Time/Day"));
//
//                for (String timing : classTimings) {
//                    JLabel timingLabel = new JLabel(timing);
//                    timingLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                    editPanel.add(timingLabel);
//                }
//
//                for (int day = 0; day < days; day++) {
//                    JLabel dayLabel = new JLabel("Day " + (day + 1));
//                    dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                    editPanel.add(dayLabel);
//
//                    for (int classIndex = 0; classIndex < classTimings.size(); classIndex++) {
//                        String classNameInDb = resultSet.getString(classIndex + 3);
//                        JTextField classField = new JTextField(classNameInDb != null ? classNameInDb : "FREE");
//                        classField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                        editPanel.add(classField);
//                    }
//
//                    resultSet.next();
//                }

                int editResult = JOptionPane.showConfirmDialog(this, editPanel, "Edit Timetable", JOptionPane.OK_CANCEL_OPTION);

                if (editResult == JOptionPane.OK_OPTION) {
                    JTextField dayField = new JTextField();
                    JTextField classIndexField = new JTextField();

                    JPanel addSubjectPanel = new JPanel(new GridLayout(0, 2));
                    addSubjectPanel.add(new JLabel("Enter the day where you want to add a subject:"));
                    addSubjectPanel.add(dayField);
                    addSubjectPanel.add(new JLabel("Enter the class index where you want to add a subject:"));
                    addSubjectPanel.add(classIndexField);

                    int addSubjectResult = JOptionPane.showConfirmDialog(this, addSubjectPanel, "Add Subject", JOptionPane.OK_CANCEL_OPTION);

                    if (addSubjectResult == JOptionPane.OK_OPTION) {
                        try {
                            int selectedDay = Integer.parseInt(dayField.getText());
                            int selectedClassIndex = Integer.parseInt(classIndexField.getText());

//                            if (selectedDay < 1 || selectedDay > days || selectedClassIndex < 1 || selectedClassIndex > classTimings.size()) {
//                                JOptionPane.showMessageDialog(this, "Invalid day or class index. Please enter valid values.", "Warning", JOptionPane.WARNING_MESSAGE);
//                                return;
//                            }
System.out.println(resultSet.getString(selectedClassIndex + 2));
                            // Check if the selected slot is null
                            if (resultSet.getString(selectedClassIndex + 2) == null) {
                                // Slot is null, prompt user for subject name
                                String newSubjectName = JOptionPane.showInputDialog(this, "Enter the subject name to be added:");

                                // Update the database with the new subject
                                updateStatement.setString(selectedClassIndex + 1, newSubjectName);
                                updateStatement.setInt(classTimings.size() + 1, selectedDay);
                                updateStatement.setInt(classTimings.size() + 2, classId);
                                updateStatement.addBatch();

                                // Execute the batch update
                                updateStatement.executeBatch();
                                JOptionPane.showMessageDialog(this, "Subject added successfully.");

                            } else {
                                // Slot is not null, display a warning
                                JOptionPane.showMessageDialog(this, "Selected slot is already occupied. Cannot add a new subject.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }

                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Please enter valid numbers for day and class index.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	
	public void connect() throws SQLException {
		//ENTER PORT, USER, PASSWORD.
		String str1 = "SELECT * FROM user WHERE prio = 1";
		String url = "jdbc:mysql://localhost:3306/timetable";
		String user = "root";
		String pass = "W7301@jqir#A";

		con = DriverManager.getConnection(url, user, pass);
	}
	 private int retrieveClassId(String className) throws SQLException {
		 System.out.println(className+"class\n");
			connect();
			    int classId = -1; // Initialize classId to a default value
			    try {
			    String query = "SELECT id FROM class WHERE name = ?";
			    try (PreparedStatement pst = con.prepareStatement(query)) {
			        pst.setString(1, className);

			        try (ResultSet rs = pst.executeQuery()) {
			            if (rs.next()) {
			                classId = rs.getInt("id");
			            }
			        }
			    }

			    System.out.println(classId+"classid\n"); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return classId;
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
}}

//*******************************
