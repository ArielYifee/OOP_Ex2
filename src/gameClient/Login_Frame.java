package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login_Frame extends JFrame implements ActionListener {
    private String Login_Data;
    private boolean Flag;
    JPanel panel;
    JLabel user_id, scenario_num, message;
    JTextField userID_text, scenario_text;
    JButton start;
    private int _win_h = 700;
    private int _win_w = 1000;

    public Login_Frame(){
        this.Flag = false;
        initGUI();
    }

    public void initGUI(){
        // Username ID
        user_id = new JLabel();
        user_id.setText("User ID :");
        userID_text = new JTextField();
        // Scenario Num
        scenario_num = new JLabel();
        scenario_num.setText("Scenario Num :");
        scenario_text = new JTextField();
        // Start
        start = new JButton("Start!");
        panel = new JPanel(new GridLayout(3, 1));
        panel.add(user_id);
        panel.add(userID_text);
        panel.add(scenario_num);
        panel.add(scenario_text);
        message = new JLabel();
        panel.add(message);
        panel.add(start);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Adding the listeners to components..
        start.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(450,300);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        String user_id = userID_text.getText();
        String scenario = scenario_text.getText();
        int id =0 ;
        if (user_id.length() == 0){
            id = 0;
        }
        else {
            id = Integer.parseInt(user_id);
        }
        int scen = Integer.parseInt(scenario);
        if (scen<=23 && scen>=0) {
            String s = user_id;
            s += ",";
            s+= scenario;
            this.Login_Data = s;
            this.Flag = true;
            this.dispose();
        } else {
            message.setText(" Invalid scenario.. ");
        }
    }
    public String GetData(){
        return this.Login_Data;
    }
    public Boolean Flag(){
        return this.Flag;
    }
}
