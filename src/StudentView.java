

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StudentView {
	
	Connection con;
	JFrame frame = new JFrame();
	DefaultTableModel model = new DefaultTableModel();
	
	public void stView(int id) throws SQLException {
		

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
		//-----------------------------------------------------------
		
		
		//----------------TABLE---------------------------------
		JTable table=new JTable();
		model = (DefaultTableModel)table.getModel();
		model.addColumn("DATE");
		model.addColumn("STATUS");
		JScrollPane scPane=new JScrollPane(table);
		scPane.setBounds(500, 50, 480, 525);
		table.setFont(new Font("Times New Roman", Font.BOLD, 20));
		table.setRowHeight(50);
		 layeredPane.add(scPane, JLayeredPane.MODAL_LAYER);
		//------------------------------------------------------
		
		//--------------------------INFO------------------------
		JLabel totalclass = new JLabel("TOTAL CLASSES : ");
		totalclass.setBounds(25, 180, 250, 20);
		totalclass.setForeground(Color.decode("#DEE4E7"));
		totalclass.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(totalclass, JLayeredPane.MODAL_LAYER);
		JLabel ttbox = new JLabel("");
		ttbox.setBounds(60, 230, 250, 20);
		ttbox.setForeground(Color.decode("#DEE4E7"));
		ttbox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(ttbox, JLayeredPane.MODAL_LAYER);
		JLabel classAtt = new JLabel("CLASSES ATTENDED : ");
		classAtt.setBounds(25, 280, 250, 20);
		classAtt.setForeground(Color.decode("#DEE4E7"));
		classAtt.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(classAtt, JLayeredPane.MODAL_LAYER);
		JLabel atbox = new JLabel("");
		atbox.setBounds(60, 330, 250, 20);
		atbox.setForeground(Color.decode("#DEE4E7"));
		atbox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(atbox, JLayeredPane.MODAL_LAYER);
		JLabel classAbs = new JLabel("CLASSES MISSED : ");
		classAbs.setBounds(25, 380, 250, 20);
		classAbs.setForeground(Color.decode("#DEE4E7"));
		classAbs.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(classAbs, JLayeredPane.MODAL_LAYER);
		JLabel mtbox = new JLabel("");
		mtbox.setBounds(60, 430, 250, 20);
		mtbox.setForeground(Color.decode("#DEE4E7"));
		mtbox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(mtbox, JLayeredPane.MODAL_LAYER);
		JLabel AttPer = new JLabel("ATTENDANCE PERCENTAGE : ");
		AttPer.setBounds(25, 480, 300, 20);
		AttPer.setForeground(Color.decode("#DEE4E7"));
		AttPer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(AttPer, JLayeredPane.MODAL_LAYER);
		JLabel prbox = new JLabel("");
		prbox.setBounds(60, 530, 250, 20);
		prbox.setForeground(Color.decode("#DEE4E7"));
		prbox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		 layeredPane.add(prbox, JLayeredPane.MODAL_LAYER);
		//------------------------------------------------------
		
		//----------------------SETVALUES---------------------------
		int[] arr = stat(4);
		ttbox.setText(String.valueOf(arr[0]));
		atbox.setText(String.valueOf(arr[1]));
		mtbox.setText(String.valueOf(arr[2]));
		prbox.setText(String.valueOf(arr[3])+"%");
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
		con = DriverManager.getConnection(url, user, pass);
		String str = "SELECT name FROM user WHERE id = "+id;
		Statement stm = con.createStatement();
		ResultSet rst = stm.executeQuery(str);
		rst.next();
		return rst.getString("name");
	}
	
	public void tblupdt(int id) {
		try {
			ResultSet res = dbSearch(id);
			for(int i=0; res.next(); i++) {
				model.addRow(new Object[0]);
				model.setValueAt(res.getString("dt"), i, 0);
		        model.setValueAt(res.getString("status"), i, 1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public int[] stat(int id) throws SQLException {
		String str = "SELECT COUNT(*) AS pre FROM attend WHERE stid = "+id+" AND status = 'Present'";
		String str2 = "SELECT COUNT(*) AS abs FROM attend WHERE stid = "+id+" AND status = 'Absent'";
		int[] x = new int[4];
		Statement stm = con.createStatement();
		ResultSet rst = stm.executeQuery(str);
		rst.next();
		x[1] = rst.getInt("pre");
		rst = stm.executeQuery(str2);
		rst.next();
		x[2] = rst.getInt("abs");
		x[0] = x[1] + x[2];
	//	x[3] = (x[1]*100)/x[0];
		tblupdt(id);
		return x;
	}
	
	public ResultSet dbSearch(int id) throws SQLException {
		String str1 = "SELECT * from attend where stid = "+id+" ORDER BY dt desc";
		Statement stm = con.createStatement();
		ResultSet rst = stm.executeQuery(str1);
		return rst;
	}
}