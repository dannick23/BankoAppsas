package com.example.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.sqlControls.services.SQLService;

import java.util.List;

public class TransferHistoryActivity extends AppCompatActivity {
    private ListView transfers;
    private Button backButton;
    private SQLService sqlService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_history);
        sqlService = new SQLService();
        List<String> items = sqlService.getPaymentHistory();
        transfers = findViewById(R.id.listview_transfer_history);
        backButton = findViewById(R.id.button_transher_history_back);
        backButton.setOnClickListener(getOnClickListener());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        transfers.setAdapter(adapter);
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            Intent intent = new Intent(TransferHistoryActivity.this, IndexActivity.class);
            startActivity(intent);
        };
    }
}
