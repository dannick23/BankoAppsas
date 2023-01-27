package com.example.sqldemo;

import static com.example.sqldemo.dtos.builders.AccountDTOBuilder.anAccountDTO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.dtos.AccountDTO;
import com.example.sqldemo.dtos.builders.AccountDTOBuilder;
import com.example.sqldemo.sqlControls.services.SQLService;

public class AddAccountActivity extends AppCompatActivity {
    private TextView addAccountStatus;
    private EditText bankName;
    private EditText bankAccountNumber;
    private EditText smartIdHash;
    private EditText balance;
    private Button cancel;
    private Button addAccount;
    private SQLService sqlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        addAccountStatus = findViewById(R.id.textview_add_account_status);
        bankName = findViewById(R.id.edittext_new_account_banko_pavadinimas);
        bankAccountNumber = findViewById(R.id.edittext_new_account_saskaitos_kodas);
        smartIdHash = findViewById(R.id.edittext_add_account_smart_id_hash);
        balance = findViewById(R.id.edittext_add_account_balance);
        cancel = findViewById(R.id.button_add_account_cancel);
        addAccount = findViewById(R.id.button_add_account_add);
        cancel.setOnClickListener(getOnClickListener());
        addAccount.setOnClickListener(getClickListener());
        sqlService = new SQLService();
    }

    @NonNull
    private View.OnClickListener getClickListener() {
        return view -> {
            AccountDTO accountDTO = anAccountDTO()
                    .withBankName(bankName.getText().toString())
                    .withBankAccountNumber(bankAccountNumber.getText().toString())
                    .withSmartIdHash(smartIdHash.getText().toString())
                    .withBalance(balance.getText().toString())
                    .build();
            String valid = validateFields(accountDTO);
            if (valid.equals("YES")) {
                if (sqlService.addNewAccount(accountDTO)) {
                    Intent intent = new Intent(AddAccountActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    addAccountStatus.setText("Something went wrong!\n Try again");
                }
            } else {
                addAccountStatus.setText(valid);
            }
        };
    }

    private String validateFields(AccountDTO accountDTO) {
        if (!fieldsArePopulated(accountDTO)) {
            return "The necessary fields are not populated!";
        }
        if (accountDTO.getBankName().matches(".*\\d.*")) {
            return "Bank name cannot contain numbers!";
        }

        return "YES";
    }

    private boolean fieldsArePopulated(AccountDTO accountDTO) {
        return !accountDTO.getBankAccountNumber().isEmpty() &&
               !accountDTO.getBankName().isEmpty() &&
               !accountDTO.getBalance().isEmpty();
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            Intent intent = new Intent(AddAccountActivity.this, IndexActivity.class);
            startActivity(intent);
        };
    }
}
