package com.qq.server.db;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.*;

public class SqlConnection {
//������SqlConnection ��

    /*
     *java����mysql���ݿ�
     *1��������������
     *2�����ݿ������ַ���"jdbc:mysql://localhost:3306/���ݿ���?"
     *3�����ݿ��¼��
     *3�����ݿ��¼����
     */

    private static final String URL="jdbc:mysql://localhost:3306/User?useUnicode=true&characterEncoding=utf-8&useSSL=false";//���ݿ������ַ���
    private static final String NAME="root";//��¼��
    private static final String PASSWORD="123";//����
    Statement stmt=null;
    ResultSet result=null;

    public boolean TheSqlConnection(String userName,String userPasswd,int userType)
    {
        boolean sym = false;
        //1.��������
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("δ�ܳɹ������������������Ƿ�����������");
            //���һ��println��������������쳣������Ƿ����������������������ַ����Ƿ����
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("��ȡ���ݿ����ӳɹ���");
            if(userType == 1) {
                String con_text = "select * from userdb where Username='" + userName + "' and Userpasswd='" + userPasswd + "'";
                stmt = conn.createStatement();
                result = stmt.executeQuery(con_text);
                while (result.next()) {
                    sym = true;
                    System.out.print(result.getString(1) + '\t');
                    System.out.println(result.getString(2));
//                System.out.println("success");
                }
                result.close();
                stmt.close();
            }
            else if(userType == 2)
            {
                String con_text = "select * from userdb where Username='" + userName + "' and Userpasswd='" + userPasswd + "'";
                String ins_text = "insert into userdb values('"+userName+"','"+userPasswd+"')";
                stmt = conn.createStatement();
                result = stmt.executeQuery(con_text);
                if(!result.next())
                {
                    PreparedStatement pst = conn.prepareStatement(ins_text);
                    pst.executeUpdate();
                    sym = true;
                    pst.close();
                }
                result.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("��ȡ���ݿ�����ʧ�ܣ�");
            //���һ��println���������ʧ�ܣ���������ַ������ߵ�¼���Լ������Ƿ����
            e.printStackTrace();
        }
        //���ݿ�򿪺��Ҫ�ر�
        if(conn!=null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                conn=null;

            }
        }
        return sym;
    }

    public static void main(String[] args)
    {
        new SqlConnection().TheSqlConnection("2","123",1);
    }
}
