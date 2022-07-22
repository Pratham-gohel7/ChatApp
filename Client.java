package ChatApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.Socket;
import java.awt.event.*;
import java.text.*;
import java.util.Calendar;
import java.io.*;

public class Client implements ActionListener{
    static JFrame f = new JFrame();
    static JPanel p1 , p2;
    JTextField messBox;
    JButton send;
    static Box vertical = Box.createVerticalBox();

    static DataInputStream din;
    static DataOutputStream dout;

    Client(){
        f.setLayout(null);

        p1 = new JPanel();
        p1.setBackground(new Color(30,138,57));
        p1.setBounds(0 , 0 ,350 , 50);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(5 , 15 , 20, 20);
        p1.add(l1);

        p1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(35, 6 , 40, 40);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(235 , 15 , 20, 20);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(275 , 16 , 20, 20);
        p1.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel dots = new JLabel(i15);
        dots.setBounds(310 , 15 , 10, 20);
        p1.add(dots);

        JLabel name = new JLabel("Vishal");
        name.setBounds(90 , 15 , 100 , 15);
        name.setForeground(Color.white);
        name.setFont(new Font("sans-serif" , Font.BOLD , 18));
        p1.add(name);

        JLabel status = new JLabel("online");
        status.setBounds(90 , 32 , 60 , 10);
        status.setForeground(Color.white);
        status.setFont(new Font("sans-serif" , Font.BOLD , 8));
        p1.add(status);

        p2 = new JPanel();
        p2.setBounds(5 , 55 , 340 , 450 );
        f.add(p2);

        messBox = new JTextField();
        messBox.setBounds(5 , 510 , 260 , 35);
        messBox.setFont(new Font("Arial" , Font.PLAIN , 16));
        messBox.setBorder(null);
        f.add(messBox);
        
        send = new JButton("Send");
        send.setBounds(267 , 510 , 80 , 35);
        send.setBackground(new Color(30,138,57));
        messBox.setFont(new Font("Arial" , Font.PLAIN , 16));
        send.setForeground(Color.white);
        f.add(send);
        send.addActionListener(this);

        f.setUndecorated(true);
        f.setShape(new RoundRectangle2D.Double(0 , 0 , 350 , 550 , 25 , 25));
        f.setSize(350 , 550);
        f.setLocation(700 , 50);
        f.getContentPane().setBackground(new Color(193 , 221 , 232));

        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae){
        try{
            String txt = messBox.getText();

            JPanel p3 = formatLabel(txt);
        
            p2.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p3 , BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            p2.add(vertical , BorderLayout.PAGE_START);

            dout.writeUTF(txt);

            messBox.setText("");
            messBox.requestFocusInWindow();

            f.repaint();
            f.invalidate();
            f.validate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static JPanel formatLabel(String txt){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style = \"width:13s0\">" +txt+ "</p></html>");
        output.setFont(new Font("Arial" , Font.PLAIN , 16));
        output.setBackground(new Color(24 , 84 , 46));
        output.setForeground(Color.white);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15 , 15 , 15 , 50));

        panel.add(output);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }
    public static void main(String[] args) {
        new Client();
        try{
            Socket s = new Socket("127.0.0.1" , 6001);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
        

            while(true){
                p2.setLayout(new BorderLayout());
                String msg = din.readUTF();

                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel , BorderLayout.LINE_START);
                vertical.add(left);                
                vertical.add(Box.createVerticalStrut(15));

                p2.add(vertical , BorderLayout.PAGE_START);
                
                f.validate();

                s.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

