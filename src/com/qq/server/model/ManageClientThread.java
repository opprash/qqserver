package com.qq.server.model;

import java.util.*;
public class ManageClientThread {

    public static HashMap hm = new HashMap<String,ServerConClientThread>();

    public static void addClientThread(String uid,ServerConClientThread ct)
    {
        hm.put(uid,ct);
    }

    public static ServerConClientThread getClientThread(String  uid)
    {
        return (ServerConClientThread)hm.get(uid);
    }

    //���ص�ǰ���ߵ��˵����
    public static String getAllOnLineUserName()
    {
        //�������
        Iterator it = hm.keySet().iterator();
        String res = "";
        while(it.hasNext())
        {
            res+=it.next().toString()+" ";
        }
        return res;
    }
}