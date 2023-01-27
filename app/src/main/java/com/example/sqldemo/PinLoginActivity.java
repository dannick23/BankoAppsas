package com.example.sqldemo;

import static com.example.sqldemo.session.LiveSessionObject.forgetUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.sqlControls.services.SQLService;

public class PinLoginActivity extends AppCompatActivity {
    private EditText pinPassword;
    private TextView pinStatus;
    private Button submitPin;
    private Button useDifferentAccount;
    private SQLService sqlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_login);
        pinPassword = findViewById(R.id.edittext_pin_password);
        pinStatus = findViewById(R.id.textview_pin_status);
        submitPin = findViewById(R.id.button_pin_submit);
        submitPin.setOnClickListener(getOnClickListener());
        useDifferentAccount = findViewById(R.id.button_pin_login_another_user);
        useDifferentAccount.setOnClickListener(getClickListener());
        sqlService = new SQLService();
    }

    @NonNull
    private View.OnClickListener getClickListener() {
        return view -> {
            SharedPreferences prefs = getSharedPreferences("LoginHistory", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            forgetUser();
            Intent intent = new Intent(PinLoginActivity.this, MainActivity.class);
            startActivity(intent);
        };
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            String pinPass = pinPassword.getText().toString();
            if (validatePin(pinPass)) {
                if (sqlService.authenticateUserByPin(pinPassword.getText().toString())) {
                    Intent intent = new Intent(PinLoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    pinStatus.setText("Login failed!\n Try again.");
                }
            } else {
                pinStatus.setText("Pin must be between 4 and 6 digits");
            }
        };
    }

    private boolean validatePin(String pinPass) {
        return !pinPass.isEmpty() && pinPass.length() <= 6 && pinPass.length() >= 4;
    }
}
