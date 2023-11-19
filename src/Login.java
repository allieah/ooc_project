import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login {

    int usr = 0;

    public void loginView() {
        JFrame frame = new JFrame();
        Font text = new Font("Arial", Font.PLAIN, 20); // Changed font to Arial

        // Using more vibrant colors
        Color primaryColor = Color.decode("#2196F3"); // Material Design Blue
        Color secondaryColor = Color.decode("#FFBDA0"); // Material Design Pink

        Home hm = new Home();
        TeacherView tview = new TeacherView();
        StudentView sview = new StudentView();
        
        // Create a layered pane to hold components with different layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1000, 600);

        //-------------------------Blurred Background Image---------------------
        JLabel backgroundLabel = new JLabel();
        ImageIcon backgroundIcon = createBlurredImageIcon("t1.jpg", 200, 650); // Replace with your image path
        backgroundLabel.setIcon(backgroundIcon);
        backgroundLabel.setBounds(-50, 0, 600, 600);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        
        
        //-------------------------LOGO--------------------------
        JLabel attendance = new JLabel("TIMETABLE");
        attendance.setForeground(primaryColor);
        attendance.setBounds(100, 75, 400, 50);
        attendance.setFont(new Font("Verdana", Font.BOLD, 30));
    //    frame.add(attendance);
        layeredPane.add(attendance, JLayeredPane.MODAL_LAYER);
        JLabel management = new JLabel("MANAGEMENT SYSTEM");
        management.setForeground(primaryColor);
        management.setBounds(280, 100, 400, 50);
        management.setFont(new Font("Verdana", Font.BOLD, 15));
//        frame.add(management);
        layeredPane.add(management, JLayeredPane.MODAL_LAYER);
        //-------------------------------------------------------

        //------------------Panel----------------------------------
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230), 0, getHeight(), primaryColor.darker());
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBounds(500, 0, 500, 600);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        //---------------------------------------------------------

        //------------------------CLOSE---------------------------
        JLabel x = new JLabel("X");
        x.setForeground(primaryColor);
        x.setBounds(965, 20, 100, 20);
        x.setFont(new Font("Arial", Font.BOLD, 20)); // Changed font
      //  frame.add(x);
        layeredPane.add(x, JLayeredPane.MODAL_LAYER);
        x.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        //----------------------------------------------------------

        //-----------------------MINIMIZE-----------------------------
        JLabel min = new JLabel("_");
        min.setForeground(primaryColor);
        min.setBounds(935, 10, 100, 20);
        min.setFont(new Font("Arial", Font.BOLD, 20)); // Changed font
//        frame.add(min);
        layeredPane.add(min, JLayeredPane.MODAL_LAYER);
        min.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }
        });
        //-------------------------------------------------------------

        //--------------------------LOGINTEXT---------------------------
        JLabel lgn = new JLabel("LOGIN");
        lgn.setForeground(primaryColor.darker().darker());
        
        lgn.setBounds(625, 100, 350, 75);
        lgn.setFont(new Font("Arial", Font.BOLD, 75)); // Changed font
      //  frame.add(lgn);
        layeredPane.add(lgn, JLayeredPane.MODAL_LAYER);
        //---------------------------------------------------------------

        //-------------------------USER--------------------------
        JLabel user = new JLabel("Username");
        user.setForeground(primaryColor.darker().darker());
        user.setBounds(570, 250, 100, 20);
        user.setFont(text);
      //  frame.add(user);
        layeredPane.add(user, JLayeredPane.MODAL_LAYER);
        //-------------------------------------------------------

        //-----------------------USERFIELD-----------------------
        JTextField username = new JTextField();
        username.setBounds(570, 285, 360, 35);
     //   username.setBackground(primaryColor.brighter()); // Lighter shade
        username.setForeground(Color.BLACK); // White text color
        username.setFont(new Font("Arial", Font.BOLD, 15));
     //   frame.add(username);
        layeredPane.add(username, JLayeredPane.MODAL_LAYER);
        //---------------------------------------------------------

        //-------------------------Password--------------------------
        JLabel pass = new JLabel("Password");
        pass.setForeground(primaryColor.darker().darker());
        pass.setBounds(570, 350, 100, 20);
        pass.setFont(text);
    //    frame.add(pass);
        layeredPane.add(pass, JLayeredPane.MODAL_LAYER);
        //-------------------------------------------------------

        //-----------------------PASSWORDFIELD-----------------------
        JPasswordField password = new JPasswordField();
        password.setBounds(570, 385, 360, 35);
     //   password.setBackground(primaryColor.brighter());
  //      password.setForeground(Color.WHITE);
        layeredPane.add(password, JLayeredPane.MODAL_LAYER);
        //---------------------------------------------------------

        //-------------------------WARNING--------------------------
        JLabel warning = new JLabel();
        warning.setForeground(Color.RED);
        warning.setBounds(625, 450, 250, 20);
        warning.setHorizontalAlignment(warning.CENTER);
        layeredPane.add(warning, JLayeredPane.MODAL_LAYER);
        //-------------------------------------------------------

        //----------------------LOGIN----------------------------
        JButton login = new JButton("LOGIN");
        login.setBounds(625, 500, 250, 50);
        login.setFont(new Font("Arial", Font.BOLD, 20)); // Changed font
        login.setBackground(primaryColor.darker().darker()); // Secondary color for button
        login.setForeground(Color.WHITE); // White text color
        layeredPane.add(login, JLayeredPane.MODAL_LAYER);
        login.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int res = dbCheck(username.getText(), password.getText());
                    if (res == 0) {
                        warning.setText("NO USER FOUND!!!");
                        username.setText("");
                        password.setText("");
                    } else if (res == -1) {
                        warning.setText("WRONG PASSWORD!!!");
                        username.setText("");
                        password.setText("");
                    } else {
                        if (res == 1)
                            hm.homeView(usr);
                        else if (res == 2)
                            tview.tcView(usr);
                        else if (res == 3)
                            sview.stView(usr);

                        frame.dispose();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //----------------------------------------------------------

        //-------------------------------------------------------
        frame.setContentPane(layeredPane);

        //-------------------------------------------------------
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //--------------------------------------------------------------
    }

    public int dbCheck(String name, String password) throws SQLException {
        // ENTER PORT, USER, PASSWORD.
        String url = "jdbc:mysql://localhost:3306/timetable";
        String user = "root";
        String pass = "W7301@jqir#A";
        String str = "SELECT * FROM user WHERE username = '" + name + "'";
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement stm = con.createStatement();
        ResultSet rst = stm.executeQuery(str);
        if (rst.next()) {
            if (rst.getString("password").equals(password)) {
                usr = rst.getInt("id");
                return rst.getInt("prio");
            } else
                return -1;
        } else {
            return 0;
        }
    }
    private ImageIcon createBlurredImageIcon(String imagePath, int blurRadius, int width) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();

        // Create a blurred image
        Image blurredImage = originalImage.getScaledInstance(width, 600, Image.SCALE_SMOOTH);
        ImageIcon blurredIcon = new ImageIcon(blurredImage);

        return blurredIcon;
    }
}
