package com.example.mpay;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tv = findViewById(R.id.historyText);
        StringBuilder sb = new StringBuilder();
        for (String s : MainActivity.history) sb.append(s).append("\n");
        tv.setText(sb.toString());
    }
}