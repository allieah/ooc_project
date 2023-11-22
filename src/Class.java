

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Class{
	DefaultTableModel model = new DefaultTableModel();
	Connection con;
	int check;
	JButton edit;
	JButton delete;
	JButton add;
	
	public void classView() {
		JFrame frame = new JFrame();
		Font text = new Font("Times New Roman", Font.PLAIN, 18);
		Font btn = new Font("Times New Roman", Font.BOLD, 20);
		
		//------------------------CLOSE---------------------------
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
		//--------------------ID-----------------------------------
		JLabel id = new JLabel("ID : ");
		id.setFont(text);
		id.setBounds(25, 150, 40, 20);
		id.setForeground(Color.decode("#DEE4E7"));
		 layeredPane.add(id, JLayeredPane.MODAL_LAYER);

		JTextField idbox= new JTextField();
		idbox.setBounds(60, 150, 50, 25);
		idbox.setBackground(Color.decode("#DEE4E7"));
		idbox.setFont(text);
		idbox.setForeground(Color.decode("#37474F"));
		idbox.setEditable(false);
		 layeredPane.add(idbox, JLayeredPane.MODAL_LAYER);

		//--------------------------------------------------------
		
		//-------------------NAME----------------------------------
		JLabel nm = new JLabel("NAME : ");
		nm.setFont(text);
		nm.setBounds(25, 240, 150, 20);
		nm.setForeground(Color.decode("#DEE4E7"));
		 layeredPane.add(nm, JLayeredPane.MODAL_LAYER);

		JTextField name= new JTextField();
		name.setBounds(25, 270, 400, 35);
		name.setBackground(Color.decode("#DEE4E7"));
		name.setFont(text);
		name.setForeground(Color.decode("#37474F"));
		name.setEditable(false);
		 layeredPane.add(name, JLayeredPane.MODAL_LAYER);

		//--------------------------------------------------------
		 //--------------------NO OF DAYS-------------------------------
	        JLabel daysLabel = new JLabel("NO OF DAYS PER WEEK: ");
	        daysLabel.setFont(text);
	        daysLabel.setBounds(25, 330, 300, 20);
	        daysLabel.setForeground(Color.decode("#DEE4E7"));
	        layeredPane.add(daysLabel, JLayeredPane.MODAL_LAYER);

	        JTextField daysField = new JTextField();
	        daysField.setBounds(25, 360, 50, 25);
	        daysField.setBackground(Color.decode("#DEE4E7"));
	        daysField.setFont(text);
	        daysField.setForeground(Color.decode("#37474F"));
	        daysField.setEditable(false);
	        layeredPane.add(daysField, JLayeredPane.MODAL_LAYER);
		//--------------------SAVEBUTTON---------------------------
		JButton save = new JButton("SAVE");
		save.setBounds(25, 500, 125, 50);
		save.setFont(btn);
		save.setBackground(Color.decode("#DEE4E7"));
		save.setForeground(Color.decode("#37474F"));
		save.setEnabled(false);
		 layeredPane.add(save, JLayeredPane.MODAL_LAYER);

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(check == 1) {
					try {
						 adder(Integer.parseInt(idbox.getText()), name.getText(), Integer.parseInt(daysField.getText()));
					}
					catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				else if(check == 2) {
					save.setEnabled(false);
					try {
						  editor(Integer.parseInt(idbox.getText()), name.getText(), Integer.parseInt(daysField.getText()));
					}
					catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				try {
					 idbox.setText(String.valueOf(getid()));
					edit.setEnabled(false);
					delete.setEnabled(false);
					name.setText("");
					  daysField.setText("");
					while(model.getRowCount() > 0)
						model.removeRow(0);
					tblupdt();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//-------------------------------------------------------
		
		//----------------------EDITBUTTON-----------------------
		edit = new JButton("EDIT");
		edit.setBounds(175, 500, 125, 50);
		edit.setFont(btn);
		edit.setEnabled(false);
		edit.setBackground(Color.decode("#DEE4E7"));
		edit.setForeground(Color.decode("#37474F"));
		 layeredPane.add(edit, JLayeredPane.MODAL_LAYER);

		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				edit.setEnabled(false);
				save.setEnabled(true);
				check = 2;
				name.setEditable(true);
			}
		});
		//-------------------------------------------------------
		
		//--------------------ADDBUTTON-------------------------
		add = new JButton("ADD");
		add.setBounds(325, 500, 125, 50);
		add.setFont(btn);
		add.setBackground(Color.decode("#DEE4E7"));
		add.setForeground(Color.decode("#37474F"));
		 layeredPane.add(add, JLayeredPane.MODAL_LAYER);

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				add.setEnabled(false);
				delete.setEnabled(false);
				save.setEnabled(true);
				name.setEditable(true);
				daysField.setEditable(true);
				check = 1;
				try {
					idbox.setText(String.valueOf(getid()));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//------------------------------------------------------
		
		//------------------------DELETEBUTTON-----------------------
		delete = new JButton("DELETE");
		delete.setBounds(175, 432, 125, 50);
		delete.setFont(btn);
		delete.setBackground(Color.decode("#DEE4E7"));
		delete.setForeground(Color.decode("#37474F"));
		delete.setEnabled(false);
		 layeredPane.add(delete, JLayeredPane.MODAL_LAYER);

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name.setEditable(false);
				edit.setEnabled(false);
				add.setEnabled(true);
				try {
					deleter(Integer.parseInt(idbox.getText()));
					idbox.setText(String.valueOf(getid()));
					name.setText("");
					while(model.getRowCount() > 0)
						model.removeRow(0);
					tblupdt();
				} 
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		//------------------------------------------------------------
		
		//----------------TABLE---------------------------------
		@SuppressWarnings("serial")
		JTable table=new JTable(){
			public boolean isCellEditable(int row,int column){
				return false;
			}
		};
		model = (DefaultTableModel)table.getModel();
		model.addColumn("ID");
		model.addColumn("NAME");
		model.addColumn("DAYS");
		tblupdt();
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		JScrollPane scPane=new JScrollPane(table);
		scPane.setBounds(500, 50, 480, 525);
		 layeredPane.add(scPane, JLayeredPane.MODAL_LAYER);

		//------------------------------------------------------
		
		//-----------------TABLE ACTION----------------------------
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				idbox.setText(String.valueOf(table.getModel().getValueAt(row, 0)));
				name.setText(String.valueOf(table.getModel().getValueAt(row, 1)));
				daysField.setText(String.valueOf(table.getModel().getValueAt(row, 2)));
				edit.setEnabled(true);
				save.setEnabled(false);
				delete.setEnabled(true);
			}
		});
		//-------------------------------------------------------------

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
	
	public void tblupdt() {
		try {
			ResultSet res = dbSearch();
			for(int i=0; res.next(); i++) {
				model.addRow(new Object[0]);
				model.setValueAt(res.getInt("id"), i, 0);
		        model.setValueAt(res.getString("name"), i, 1);
		        model.setValueAt(res.getString("day"), i, 2);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public int getid() throws SQLException {
        Statement stm = con.createStatement();
        ResultSet rst = stm.executeQuery("SELECT MAX(id) from class");
        if(rst.next()) {
            return rst.getInt("MAX(id)")+1;
        }
        else {
            return 1;
        }
    }
	
	public ResultSet dbSearch() throws SQLException {
		//ENTER PORT, USER, PASSWORD.
		String str1 = "SELECT * FROM class";
		String url = "jdbc:mysql://localhost:3306/timetable";
		String user = "root";
		String pass = "W7301@jqir#A";
		con = DriverManager.getConnection(url, user, pass);
		Statement stm = con.createStatement();
		ResultSet rst = stm.executeQuery(str1);
		return rst;
	}
	
	 public void adder(int id, String name, int days) throws SQLException {
	        String adding = "insert into class values (" + id + ", '" + name + "', " + days + ")";
	        Statement stm = con.createStatement();
	        stm.executeUpdate(adding);
	    }

	  
	
	public void deleter(int id) throws SQLException {
		String del = "DELETE FROM class WHERE id = "+id;
        Statement stm = con.createStatement();
        stm.executeUpdate(del);
	}
	 public void editor(int id, String name, int days) throws SQLException {
	        String update = "UPDATE class SET name = '" + name + "', days = " + days + " WHERE id = " + id;
	        Statement stm = con.createStatement();
	        stm.executeUpdate(update);
	    }
}