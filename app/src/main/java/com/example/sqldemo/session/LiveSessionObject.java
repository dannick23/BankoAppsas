package com.example.sqldemo.session;

public class LiveSessionObject {
    private static String email;
    private static String password;

    public LiveSessionObject(String email, String password) {
        LiveSessionObject.email = email;
        LiveSessionObject.password = password;
    }

    public static String getUserEmail() {
        return email;
    }

    public static String getUserPassword() {
        return password;
    }
    public static void forgetUser(){
        email = null;
        password = null;
    }
}
