

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

public class TeacherView {
	public void tcView(int id) throws SQLException {
		GenerateTimetable t = new GenerateTimetable();
		JFrame frame = new JFrame();
		Font btn = new Font("Times New Roman", Font.BOLD, 20);

		  Color primaryColor = Color.decode("#2196F3");
			//------------------Panel----------------------------------
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
			    	//------------------------CLOSE---------------------------
					JLabel x = new JLabel("X");
					x.setForeground(Color.decode("#37474F"));
					x.setBounds(965, 10, 100, 20);
					x.setFont(new Font("Times New Roman", Font.BOLD, 20));
					  layeredPane.add(x, JLayeredPane.POPUP_LAYER);
					x.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							System.exit(0);
						}
					});
					//----------------------------------------------------------
					
					//-----------------------BACK---------------------------------
					JLabel back = new JLabel("< BACK");
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
		
		//-------------------Welcome---------------------------------
		JLabel welcome = new JLabel("Welcome "+getUser(id)+",");
		welcome.setForeground(Color.decode("#DEE4E7"));
		welcome.setBounds(10, 50, 250, 20);
		welcome.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(welcome, JLayeredPane.MODAL_LAYER);
		
		//----------------------ADD Timetable----------------------------
		JButton addtt = new JButton("VIEW TIMETABLE");
		 JTextArea textArea = new JTextArea();
	        textArea.setEditable(false); // Make the text area read-only
	        JScrollPane scrollPane = new JScrollPane(textArea);
		addtt.setBounds(150, 200, 650, 60);
		addtt.setFont(btn);
		addtt.setBackground(Color.decode("#DEE4E7"));
		addtt.setForeground(Color.decode("#37474F"));
		 layeredPane.add(addtt, JLayeredPane.MODAL_LAYER);
		addtt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				try {
					t.displayTimetable(textArea);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//----------------------------------------------------------
		
		//----------------------EDIT TIMETABLE----------------------------
		JButton edittt = new JButton("EDIT timeTable");
		edittt.setBounds(150, 350, 650, 60);
		edittt.setFont(btn);
		edittt.setBackground(Color.decode("#DEE4E7"));
		edittt.setForeground(Color.decode("#37474F"));
		 layeredPane.add(edittt, JLayeredPane.MODAL_LAYER);
		edittt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				try {
					t.editTimetable(textArea);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
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