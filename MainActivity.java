package com.example.mpay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static int balance = 5000;
    static int pin = 1234;
    static ArrayList<String> history = new ArrayList<>();

    EditText input;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        output = findViewById(R.id.output);

        findViewById(R.id.loginBtn).setOnClickListener(v -> login());
        findViewById(R.id.sendBtn).setOnClickListener(v -> send());
        findViewById(R.id.addBtn).setOnClickListener(v -> add());
        findViewById(R.id.historyBtn).setOnClickListener(v -> history());
    }

    void login() {
        if (Integer.parseInt(input.getText().toString()) == pin)
            output.setText("Login success. Balance " + balance);
        else
            output.setText("Wrong PIN");
    }

    void send() {
        Intent i = new Intent(this, OtpActivity.class);
        i.putExtra("amount", Integer.parseInt(input.getText().toString()));
        startActivity(i);
    }

    void add() {
        int amt = Integer.parseInt(input.getText().toString());
        balance += amt;
        history.add("Credit " + amt);
        output.setText("Added. Balance " + balance);
    }

    void history() {
        startActivity(new Intent(this, HistoryActivity.class));
    }
}