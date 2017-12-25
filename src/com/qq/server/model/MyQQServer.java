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
            //��8888�˿ڼ���
            System.out.println("���Ƿ�����,��8888����");
            ServerSocket ss = new ServerSocket(8888);
            //����,�ȴ�����
            while(true) {
                Socket s = ss.accept();

                //���տͻ��˷�������Ϣ
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                User u = (User) ois.readObject();
                Message ms = new Message();
                SqlConnection sqlConnection = new SqlConnection();
                if(u.getType() == 1) {
                    if (sqlConnection.TheSqlConnection(u.getUserName(), u.getUserPasswd(),u.getType())) {
                        //����һ���ɹ���¼����Ϣ��
                        ms.setMesType("1");
                        oos.writeObject(ms);

                        //����һ���߳�,�ø��߳���ÿͻ��˱���ͨѶ
                        ServerConClientThread scct = new ServerConClientThread(s);
                        ManageClientThread.addClientThread(u.getUserName(), scct);
                        //������ÿͻ���ͨѶ���߳�
                        scct.start();

                        //֪ͨ���������û�
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
