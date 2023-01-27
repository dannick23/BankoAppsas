package com.example.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.dtos.UserDTO;
import com.example.sqldemo.dtos.builders.UserDTOBuilder;
import com.example.sqldemo.sqlControls.services.SQLService;

import java.time.LocalDate;

public class RegisterActivity extends AppCompatActivity {
    private Button backButton;
    private Button registerButton;
    private TextView registerStatus;
    private EditText registerVardas;
    private EditText registerPavarde;
    private EditText registerElPastas;
    private EditText registerSlaptazodis;
    private EditText registerAsmensKodas;
    private EditText registerPin;
    private SQLService sqlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        backButton = findViewById(R.id.register_back);
        registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(getClickListener());
        registerStatus = findViewById(R.id.textview_register_status);
        registerVardas = findViewById(R.id.register_name);
        registerPavarde = findViewById(R.id.register_last_name);
        registerElPastas = findViewById(R.id.register_email);
        registerSlaptazodis = findViewById(R.id.register_password);
        registerAsmensKodas = findViewById(R.id.register_ssn);
        registerPin = findViewById(R.id.register_pin);
        backButton.setOnClickListener(getOnClickListener());
        sqlService = new SQLService();
    }

    @NonNull
    private View.OnClickListener getClickListener() {
        return view -> {
            UserDTOBuilder userDTO = new UserDTOBuilder()
                    .withVardas(registerVardas.getText().toString())
                    .withPavarde(registerPavarde.getText().toString())
                    .withElPastas(registerElPastas.getText().toString())
                    .withSlaptazodis(registerSlaptazodis.getText().toString())
                    .withAsmensKodas(registerAsmensKodas.getText().toString())
                    .withPin(registerPin.getText().toString());

            String valid = areFieldsValid(userDTO.build());
            if (valid.equals("YES")) {
                userDTO.withGimimoDiena(getBirthDay(registerAsmensKodas.getText().toString()));
                if (sqlService.registerNewUser(userDTO.build())) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    registerStatus.setText("Register failed!\n Try again");
                }
            } else {
                registerStatus.setText(valid);
            }
        };
    }

    private String areFieldsValid(UserDTO userDTO) {
        if (!areFieldsPopulated(userDTO)) {
            return "All the fields must be filled out!";
        }

        if (userDTO.getVardas().matches(".*\\d.*")) {
            return "The name cannot contain numbers!";
        }
        if (userDTO.getPavarde().matches(".*\\d.*")) {
            return "The last name cannot contain numbers!";
        }
        if (!userDTO.getElPastas().contains("@")) {
            return "Email is invalid!";
        }
        if (userDTO.getSlaptazodis().length() < 8) {
            return "Password must contain atleast 8 symbols!";
        }
        if (userDTO.getAsmensKodas().length() != 11 && registerAsmensKodas.getText().toString().matches(".*\\d.*")) {
            return "Invalid social security number!";
        }
        if (!userDTO.getPin().matches(".*\\d.*") && registerPin.getText().toString().length() <= 6) {
            return "The pin code must not exceed 6 symbols and be only numeric!";
        }

        return "YES";
    }

    private boolean areFieldsPopulated(UserDTO userDTO) {
        return !userDTO.getVardas().isEmpty() &&
               !userDTO.getPavarde().isEmpty() &&
               !userDTO.getElPastas().isEmpty() &&
               !userDTO.getSlaptazodis().isEmpty() &&
               !userDTO.getAsmensKodas().isEmpty() &&
               !userDTO.getPin().isEmpty();
    }

    private LocalDate getBirthDay(String ssn) {
        int year = Integer.parseInt((ssn.startsWith("5") || ssn.startsWith("6") ? "20" : "19") + ssn.charAt(1) + ssn.charAt(2));
        int month = Integer.parseInt(String.valueOf(ssn.charAt(3)) + ssn.charAt(4));
        int day = Integer.parseInt(String.valueOf(ssn.charAt(5)) + ssn.charAt(6));
        System.out.println("[DEBUG] extracting date = " + year + " - " + month + " - " + day);

        return LocalDate.of(year, month, day);
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        };
    }

}
