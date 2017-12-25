package com.qq.server.view;

import com.qq.server.model.MyQQServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyServerFrame extends JFrame implements ActionListener{

    JPanel jp1;
    JButton jb1,jb2;
    MyQQServer myQQServer;

    public static void main(String[] args)
    {
        MyServerFrame myServerFrame = new MyServerFrame();
    }

    public MyServerFrame()
    {
        jp1 = new JPanel();
        jb1 = new JButton("启动服务器");
        jb1.addActionListener(this);
        jb2 = new JButton("关闭服务器");
        jb2.addActionListener(this);
        jp1.add(jb1);
        jp1.add(jb2);

        this.add(jp1);
        this.setSize(300,35);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jb1)
        {
            myQQServer = new MyQQServer();
            myQQServer.start();
        }
        if(e.getSource() == jb2)
        {
            myQQServer.stop();
        }
    }
}
