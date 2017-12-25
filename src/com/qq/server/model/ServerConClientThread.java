package com.qq.server.model;

import com.qq.common.Message;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.lang.*;
import java.util.Iterator;

import com.qq.common.*;
public class ServerConClientThread extends Thread{

    Socket s;

    public ServerConClientThread(Socket s)
    {
        //�ѷ�������ÿͻ��˵����Ӹ���s
        this.s = s;
    }

    //�ø��߳�֪ͨ�����û�
    public void notifyOther(String iam)
    {
        //�õ��������ߵ��˵��߳�
        HashMap hm = ManageClientThread.hm;
        Iterator it = hm.keySet().iterator();

        while(it.hasNext())
        {
            Message ms = new Message();
            ms.setCon(iam);
            ms.setMesType(MessageType.message_ret_onLineFriend);
            //ȡ�������˵�QQ
            String onLineUserName = it.next().toString();
            try {
                ObjectOutputStream oos = new ObjectOutputStream
                        (ManageClientThread.getClientThread(onLineUserName).s.getOutputStream());
                ms.setGetter(onLineUserName);
                oos.writeObject(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    public void run()
    {
        while(true)
        {
            //������߳̾Ϳ��Խ��տͻ��˵���Ϣ
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message ms = (Message)ois.readObject();

//                System.out.println(ms.getSender()+"�� "+ms.getGetter()+" ˵: "+ms.getCon());

                //�Դӿͻ���ȡ�õ���Ϣ���������ж�,Ȼ������Ӧ�Ĵ���
                if(ms.getMesType().equals(MessageType.message_comm_mes))
                {
                    //һ�����ת������
                    //ȡ�ý����˵�ͨѶ�߳�
                    ServerConClientThread sc = ManageClientThread.getClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
                    oos.writeObject(ms);
                }
                else if(ms.getMesType().equals(MessageType.message_get_onLineFriend))
                {
                    //���ڷ������ĺ��ѷ���
                    String res = ManageClientThread.getAllOnLineUserName();
                    Message ms2 = new Message();
                    ms2.setMesType(MessageType.message_ret_onLineFriend);
                    ms2.setCon(res);
                    ms2.setGetter(ms.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(ms2);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
