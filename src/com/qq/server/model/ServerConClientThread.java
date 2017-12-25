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
        //把服务器与该客户端的连接赋给s
        this.s = s;
    }

    //让该线程通知其他用户
    public void notifyOther(String iam)
    {
        //得到所有在线的人的线程
        HashMap hm = ManageClientThread.hm;
        Iterator it = hm.keySet().iterator();

        while(it.hasNext())
        {
            Message ms = new Message();
            ms.setCon(iam);
            ms.setMesType(MessageType.message_ret_onLineFriend);
            //取出在线人的QQ
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
            //这里该线程就可以接收客户端的信息
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message ms = (Message)ois.readObject();

//                System.out.println(ms.getSender()+"给 "+ms.getGetter()+" 说: "+ms.getCon());

                //对从客户端取得的消息进行类型判断,然后做相应的处理
                if(ms.getMesType().equals(MessageType.message_comm_mes))
                {
                    //一会完成转发任务
                    //取得接收人的通讯线程
                    ServerConClientThread sc = ManageClientThread.getClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
                    oos.writeObject(ms);
                }
                else if(ms.getMesType().equals(MessageType.message_get_onLineFriend))
                {
                    //把在服务器的好友返回
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
