package com.qq.server.db;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.*;

public class SqlConnection {
//这里是SqlConnection 类

    /*
     *java连接mysql数据库
     *1、加载驱动程序
     *2、数据库连接字符串"jdbc:mysql://localhost:3306/数据库名?"
     *3、数据库登录名
     *3、数据库登录密码
     */

    private static final String URL="jdbc:mysql://localhost:3306/User?useUnicode=true&characterEncoding=utf-8&useSSL=false";//数据库连接字符串
    private static final String NAME="root";//登录名
    private static final String PASSWORD="123";//密码
    Statement stmt=null;
    ResultSet result=null;

    public boolean TheSqlConnection(String userName,String userPasswd,int userType)
    {
        boolean sym = false;
        //1.加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
            //添加一个println，如果加载驱动异常，检查是否添加驱动，或者添加驱动字符串是否错误
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库连接成功！");
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
            System.out.println("获取数据库连接失败！");
            //添加一个println，如果连接失败，检查连接字符串或者登录名以及密码是否错误
            e.printStackTrace();
        }
        //数据库打开后就要关闭
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
