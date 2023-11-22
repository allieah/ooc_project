

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Home{
	GenerateTimetable addtimet = new GenerateTimetable();
	public void homeView(int id) throws SQLException {
		JFrame frame = new JFrame();
		Font btn = new Font("Times New Roman", Font.BOLD, 20);
		Admin adm = new Admin();
		  JTextArea textArea = new JTextArea();
	        textArea.setEditable(false); // Make the text area read-only
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        Color primaryColor = Color.decode("#2196F3"); // Material Design Blue
	        
	        JLayeredPane layeredPane = new JLayeredPane();
	        layeredPane.setBounds(0, 0, 1000, 600);	
	        
			JPanel panel1 = new  JPanel();
			panel1.setBounds(0, 0, 1000, 35);
			panel1.setBackground(Color.decode("#DAE8E7"));
			  layeredPane.add(panel1, JLayeredPane.MODAL_LAYER);
			//------------------Panel----------------------------------
			  JPanel panel = new JPanel() {
		            @Override
		            protected void paintComponent(Graphics g) {
		                super.paintComponent(g);
		                Graphics2D g2d = (Graphics2D) g;
		                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230), 0, getHeight(), primaryColor.darker());
		                g2d.setPaint(gradient);
		                g2d.fillRect(0, 15, getWidth(), getHeight());
		            }
		        };
		        panel.setBounds(000, 15, 1000, 600);
		        
		        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
		           
	        
		//------------------------CLOSE---------------------------
		JLabel x = new JLabel("X");
		x.setForeground(Color.decode("#37474F"));
		x.setBounds(980, 10, 100, 20);
		x.setFont(new Font("Times New Roman", Font.BOLD, 20));
		 layeredPane.add(x, JLayeredPane.POPUP_LAYER);
		x.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		//----------------------------------------------------------
        
		JLabel back = new JLabel("LOGOUT");
		back.setForeground(Color.decode("#37474F"));
		back.setFont(new Font("Times New Roman", Font.BOLD, 17));
		back.setBounds(18, 10, 100, 20);
		  layeredPane.add(back, JLayeredPane.POPUP_LAYER);
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
   
		//-----------------------MINIMIZE-----------------------------
		JLabel min = new JLabel("_");
		min.setForeground(Color.decode("#37474F"));
		min.setBounds(935, 0, 100, 20);
		layeredPane.add(min, JLayeredPane.POPUP_LAYER);
		min.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setState(JFrame.ICONIFIED);
			}
		});
		//-------------------------------------------------------------

//			JPanel panelTop = new  JPanel();
//			panel.setBounds(0, 0, 1000, 35);
//			panel.setBackground(primaryColor);
//			 layeredPane.add(panelTop, JLayeredPane.MODAL_LAYER);
		        
		//---------------------------------------------------------
		
		//-------------------Welcome---------------------------------
		JLabel welcome = new JLabel("Welcome "+getUser(id)+",");
		welcome.setForeground(Color.decode("#DEE4E7"));
		welcome.setBounds(10, 50, 250, 20);
		welcome.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		frame.add(welcome);
		//-----------------------------------------------------------
		
		//----------------------STUDENTS----------------------------
		JButton students = new JButton("ADD STUDENT");
		students.setBounds(650,200, 200, 220);
		students.setFont(btn);
		students.setBackground(Color.decode("#DEE4E7"));
		students.setForeground(Color.decode("#37474F"));
		frame.add(students);
		students.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Students std = new Students();
				try {
					std.studentView();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		  layeredPane.add(students, JLayeredPane.MODAL_LAYER);
		//----------------------------------------------------------
		
		//----------------------ADDATTENDANCE----------------------------
		JButton addtimetable = new JButton("ADD TIMETABLE");
		
		
		
	       
		addtimetable.setBounds(200, 100, 250, 70);
		addtimetable.setFont(btn);
		addtimetable.setBackground(Color.decode("#DEE4E7"));
		addtimetable.setForeground(Color.decode("#37474F"));
		 layeredPane.add(addtimetable, JLayeredPane.MODAL_LAYER);
		addtimetable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//	TimetableGenerator addtimet = new TimetableGenerator();
				addtimet.generateTimetable();
				addtimet.displayTimetable(textArea);
			}
		});
		//----------------------------------------------------------
		
		//----------------------EDIT----------------------------
		JButton editTimetable = new JButton("EDIT TIMETABLE");
		editTimetable.setBounds(500, 100, 250, 70);
		editTimetable.setFont(btn);
		editTimetable.setBackground(Color.decode("#DEE4E7"));
		editTimetable.setForeground(Color.decode("#37474F"));
		 layeredPane.add(editTimetable, JLayeredPane.MODAL_LAYER);
		 editTimetable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					//	EditAttendance editatt = new EditAttendance();
				//	TimetableGenerator viewtimet = new TimetableGenerator();
					//editatt.editView();
					addtimet.displayTimetable(textArea);
		            // Your implementation for editing attendance
		          //  System.out.println("Editing attendance...");
		        } catch (Exception e1) {
		            e1.printStackTrace();
		        }
		
				
			}
		});
		//----------------------------------------------------------
		
		//----------------------TEACHERS----------------------------
		JButton teacher = new JButton("ADD TEACHER");
		teacher.setBounds(400, 200, 200, 220);
		teacher.setFont(new Font("Times New Roman", Font.BOLD, 20));
		teacher.setBackground(Color.decode("#DEE4E7"));
		teacher.setForeground(Color.decode("#37474F"));
		 layeredPane.add(teacher, JLayeredPane.MODAL_LAYER);
		teacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Teachers teacher = new Teachers();
				try {
					teacher.teachersView();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//----------------------------------------------------------
		
		//----------------------USER----------------------------
		JButton admin = new JButton("ADD ADMIN");
		admin.setBounds(150, 200, 200, 220);
		admin.setFont(new Font("Times New Roman", Font.BOLD, 20));
		admin.setBackground(Color.decode("#DEE4E7"));
		admin.setForeground(Color.decode("#37474F"));
		 layeredPane.add(admin, JLayeredPane.MODAL_LAYER);
		admin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					adm.adminView();
				} 
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//----------------------------------------------------------
		
		//----------------------CLASS----------------------------
		JButton classes = new JButton("ADD CLASS");
		classes.setBounds(380, 450, 400, 60);
		classes.setFont(new Font("Times New Roman", Font.BOLD, 20));
		classes.setBackground(Color.decode("#DEE4E7"));
		classes.setForeground(Color.decode("#37474F"));
		 layeredPane.add(classes, JLayeredPane.MODAL_LAYER);
		classes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Class classroom = new Class();
				classroom.classView();
			}
		});
		//----------------------------------------------------------
		
		//-------------------------------------------------------
		frame.setSize(1000,600);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);  
		frame.setVisible(true);
		frame.setFocusable(true);
	    frame.setContentPane(layeredPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//--------------------------------------------------------------
	}
	
	public String getUser(int id) throws SQLException {
	    //ENTER PORT, USER, PASSWORD.
		String url = "jdbc:mysql://localhost:3306/timetable";
		String user = "root";
		String pass = "W7301@jqir#A";
		Connection con = DriverManager.getConnection(url, user, pass);
		String str = "SELECT name FROM user WHERE id = "+id;
		Statement stm = con.createStatement();
		ResultSet rst = stm.executeQuery(str);
		rst.next();
		return rst.getString("name");
	}
}