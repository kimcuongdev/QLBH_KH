package com.project.qlbh_kh.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class JDBCUtil {
    public static Connection getConnection()
    {
        Connection c = null;
        try{
            //dang ky driver
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

            //lay thong tin
            String url= "jdbc:sqlserver://localhost:1433;databaseName=BTL_QL_BanHang;encrypt=true;trustServerCertificate=true";
            String userName = "sa";
            String password = "admin";

            //ket noi
            c = DriverManager.getConnection(url,userName,password);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return c;
    }
    public static void  closeConnection(Connection c)
    {
        try
        {
            if (c != null)
            {
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void  printInfo(Connection c)
    {
        try
        {
            if (c != null)
            {
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName());
                System.out.println(mtdt.getDatabaseProductVersion());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
