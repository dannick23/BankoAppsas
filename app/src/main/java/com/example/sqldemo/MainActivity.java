package com.example.sqldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.session.LiveSessionObject;
import com.example.sqldemo.sqlControls.services.SQLService;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    private TextView mainStatus;
    private EditText email;
    private EditText pass;
    private CheckBox rememberMe;
    private SQLService sqlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.search);
        registerButton = findViewById(R.id.main_register);
        mainStatus = findViewById(R.id.textview_main_status);
        pass = findViewById(R.id.password);
        email = findViewById(R.id.email);
        rememberMe = findViewById(R.id.checkBox_remember_me);
        loginButton.setOnClickListener(login());
        registerButton.setOnClickListener(getOnClickListener());
        sqlService = new SQLService();
        usePinLoginIfUserIsRemembered();
    }

    private void usePinLoginIfUserIsRemembered() {
        SharedPreferences prefs = getSharedPreferences("LoginHistory", Context.MODE_PRIVATE);
        String emailLocal = prefs.getString("email", "");
        String passwordLocal = prefs.getString("password", "");
        if (!emailLocal.isEmpty() && !passwordLocal.isEmpty()) {
            new LiveSessionObject(emailLocal, passwordLocal);
            Intent intent = new Intent(MainActivity.this, PinLoginActivity.class);
            startActivity(intent);
        }
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        };
    }

    @SuppressLint("SetTextI18n")
    private View.OnClickListener login() {
        return view -> {
            boolean isGood = sqlService.authenticateUser(email.getText().toString(),pass.getText().toString());
                if(isGood){
                    if(rememberMe.isChecked()){
                        SharedPreferences prefs = getSharedPreferences("LoginHistory", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", email.getText().toString());
                        editor.putString("password", pass.getText().toString());
                        editor.apply();
                    }
                    new LiveSessionObject(email.getText().toString(), pass.getText().toString());
                    Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                    startActivity(intent);
                }else{
                    mainStatus.setText("Login failed!\n Try again");
                }
        };
    }
}