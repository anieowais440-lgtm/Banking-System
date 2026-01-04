package com.example.mpay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    int otp = 1111;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        amount = getIntent().getIntExtra("amount", 0);

        EditText otpInput = findViewById(R.id.otpInput);
        TextView status = findViewById(R.id.status);
        Button verify = findViewById(R.id.verifyBtn);

        verify.setOnClickListener(v -> {
            if (Integer.parseInt(otpInput.getText().toString()) == otp) {
                MainActivity.balance -= amount;
                MainActivity.history.add("Debit " + amount);
                status.setText("Payment success. Balance " + MainActivity.balance);
            } else {
                status.setText("Invalid OTP");
            }
        });
    }
}