package com.example.sqldemo;

import static com.example.sqldemo.session.LiveSessionObject.forgetUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqldemo.sqlControls.services.SQLService;

import java.util.List;

public class IndexActivity extends AppCompatActivity {
    private SQLService sqlService = new SQLService();
    private Button button;
    private Button logOut;
    private Button addAccount;
    private Button transferHistory;
    private ListView listView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        List<String> items = sqlService.getAccountsList();
        listView = findViewById(R.id.saskaitos_index);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.transfer);
        logOut = findViewById(R.id.button_index_log_out);
        addAccount = findViewById(R.id.button_index_add_account);
        transferHistory = findViewById(R.id.button_index_transfer_history);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        button.setOnClickListener(getOnClickListener());
        logOut.setOnClickListener(getClickListener());
        addAccount.setOnClickListener(getListener());
        transferHistory.setOnClickListener(getL());
    }

    @NonNull
    private View.OnClickListener getL() {
        return view -> {
            Intent intent = new Intent(IndexActivity.this, TransferHistoryActivity.class);
            startActivity(intent);
        };
    }

    @NonNull
    private View.OnClickListener getListener() {
        return view -> {
            Intent intent = new Intent(IndexActivity.this, AddAccountActivity.class);
            startActivity(intent);
        };
    }

    @NonNull
    private View.OnClickListener getClickListener() {
        return view -> {
            SharedPreferences prefs = getSharedPreferences("LoginHistory", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            forgetUser();
            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
            startActivity(intent);
        };
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            Intent intent = new Intent(IndexActivity.this, TransferActivity.class);
            startActivity(intent);
        };
    }
}
