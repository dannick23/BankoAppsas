package com.example.sqldemo.sqlControls.services;

import android.os.StrictMode;

import com.example.sqldemo.sqlControls.objectTypes.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnectionService {
    private static SQLConnectionService sqlConnectionService;
    private Connection connection;

    private SQLConnectionService() {
    }

    public static SQLConnectionService autoWireObject() {
        if (sqlConnectionService == null) {
            sqlConnectionService = new SQLConnectionService();
            sqlConnectionService.initConnection();
            return sqlConnectionService;
        }
        return sqlConnectionService;
    }

    private void initConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("[DEBUG] INITIALIZING CONNECTION");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2/sqldemo", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FAILURE WHILE CONNECTING");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
//"root","","10.0.2.2","sqldemo","3306"