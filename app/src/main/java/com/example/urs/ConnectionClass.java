package com.example.urs;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String classs = "com.mysql.jdbc.Driver";
   // String url = "jdbc:mysql://172.21.11.222/db7";
    String url = "jdbc:mysql://192.168.5.14/db7";
    String un = "test";
    String password = "test";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs).newInstance();
            conn = DriverManager.getConnection(url, un, password);
            //conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
