package com.qq.server.model;

import java.net.*;
import java.io.*;
import java.util.*;
import com.qq.common.*;
import com.qq.server.db.*;
public class MyQQServer extends Thread {

    public MyQQServer()
    {
        try {
            //在8888端口监听
            System.out.println("我是服务器,在8888监听");
            ServerSocket ss = new ServerSocket(8888);
            //阻塞,等待连接
            while(true) {
                Socket s = ss.accept();

                //接收客户端发来的信息
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                User u = (User) ois.readObject();
                Message ms = new Message();
                SqlConnection sqlConnection = new SqlConnection();
                if(u.getType() == 1) {
                    if (sqlConnection.TheSqlConnection(u.getUserName(), u.getUserPasswd(),u.getType())) {
                        //返回一个成功登录的信息包
                        ms.setMesType("1");
                        oos.writeObject(ms);

                        //单开一个线程,让该线程与该客户端保持通讯
                        ServerConClientThread scct = new ServerConClientThread(s);
                        ManageClientThread.addClientThread(u.getUserName(), scct);
                        //启动与该客户端通讯的线程
                        scct.start();

                        //通知其他在线用户
                        scct.notifyOther(u.getUserName());

                    } else {
                        ms.setMesType("2");
                        oos.writeObject(ms);
                        s.close();
                    }
                }
                else if(u.getType() == 2)
                {
                    if(sqlConnection.TheSqlConnection(u.getUserName(), u.getUserPasswd(),u.getType()))
                    {
                        ms.setMesType("1");
                        oos.writeObject(ms);
                    }
                    else
                    {
                        ms.setMesType("2");
                        oos.writeObject(ms);
                        s.close();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args)
//    {
//    }
}
