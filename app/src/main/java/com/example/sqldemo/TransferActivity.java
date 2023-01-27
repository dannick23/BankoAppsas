package com.example.sqldemo;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

import static com.example.sqldemo.Utils.AccountParser.parseBigDecimalFrom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.actionsServices.TransferActionEvent;
import com.example.sqldemo.sqlControls.services.SQLService;

import java.math.BigDecimal;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private SQLService sqlService = new SQLService();
    private EditText gavejasVardas;
    private EditText gavejasSaskaita;
    private EditText transferSum;
    private ListView saskaitos;
    private TextView transferStatus;
    private Button transferButton;
    private Button backButton;
    private final TransferActionEvent transferActionEvent = new TransferActionEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer);
        gavejasVardas = findViewById(R.id.gavejas_vardas);
        gavejasSaskaita = findViewById(R.id.gavejas_saskaita);
        saskaitos = findViewById(R.id.saskaitos_transfer);
        transferButton = findViewById(R.id.transfer);
        backButton = findViewById(R.id.transfer_back);
        transferSum = findViewById(R.id.tranfer_sum);
        transferStatus = findViewById(R.id.textview_transfer_status);
        List<String> items = sqlService.getAccountsList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, items);
        saskaitos.setAdapter(adapter);
        transferButton.setOnClickListener(getOnClickListener());
        backButton.setOnClickListener(getClickListener());
        saskaitos.setChoiceMode(CHOICE_MODE_SINGLE);
    }

    @NonNull
    private View.OnClickListener getClickListener() {
        return view -> {
            Intent intent = new Intent(TransferActivity.this, IndexActivity.class);
            startActivity(intent);
        };
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {

            String valid = areFieldsValid();

            if (valid.equals("YES")) {
                boolean paymentStatus = transferActionEvent.transferMoney(
                        gavejasSaskaita.getText().toString(),
                        saskaitos.getCheckedItemPosition(),
                        parseBigDecimalFrom(transferSum.getText().toString()),
                        gavejasVardas.getText().toString()
                );

                if (paymentStatus) {
                    Intent intent = new Intent(TransferActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    transferStatus.setText("An unknown error occurred, try again.");
                }
            } else {
                transferStatus.setText(valid);
            }
        };
    }

    private String areFieldsValid() {
        String account = gavejasSaskaita.getText().toString();
        int index = saskaitos.getCheckedItemPosition();
        String sumString = transferSum.getText().toString();
        BigDecimal sum;

        try {
            sum = parseBigDecimalFrom(sumString);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid sum format!";
        }

        if (account.isEmpty()) {
            return "You must enter an account!";
        }
        if (index < 0) {
            return "No account picked from which to send money!";
        }
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            return "Transfer sum must be greater than 0!";
        }

        System.out.println("[DEBUG] parseAccountNumberFrom = " + gavejasSaskaita.getText().toString() +
                "\n getCheckedItemPosition = " + saskaitos.getCheckedItemPosition() +
                "\n parseBigDecimalFrom = " + parseBigDecimalFrom(transferSum.getText().toString()));

        return "YES";
    }
}
