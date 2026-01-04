Banking System 
MPAY PROJECT - ALL CODE IN ONE FILE
==============================

SECTION 1: C CONSOLE APP
==============================
#include <stdio.h>

#define MAX_TX 50
#define FILE_NAME "mpay_data.txt"

int balance = 5000;
int pin = 1234;
int loggedIn = 0;
int otp = 1111;

int transactions[MAX_TX];
int txCount = 0;

void saveData() {
    FILE *fp = fopen(FILE_NAME, "w");
    int i;
    if (fp == NULL) return;
    fprintf(fp, "%d %d %d\n", balance, pin, txCount);
    for (i = 0; i < txCount; i++) {
        fprintf(fp, "%d\n", transactions[i]);
    }
    fclose(fp);
}

void loadData() {
    FILE *fp = fopen(FILE_NAME, "r");
    int i;
    if (fp == NULL) return;
    fscanf(fp, "%d %d %d", &balance, &pin, &txCount);
    for (i = 0; i < txCount; i++) {
        fscanf(fp, "%d", &transactions[i]);
    }
    fclose(fp);
}

void generateOTP() {
    otp = 1111;
    printf("OTP sent. Use 1111\n");
}

int verifyOTP() {
    int input;
    printf("Enter OTP: ");
    scanf("%d", &input);
    if (input == otp) return 1;
    printf("Invalid OTP\n");
    return 0;
}

void login() {
    int inputPin;
    printf("Enter PIN: ");
    scanf("%d", &inputPin);
    if (inputPin == pin) {
        loggedIn = 1;
        printf("Login successful\n");
    } else {
        printf("Wrong PIN\n");
    }
}

void checkBalance() {
    printf("Balance: %d\n", balance);
}

void sendMoney() {
    int amount;
    printf("Enter amount: ");
    scanf("%d", &amount);
    if (amount > 0 && amount <= balance) {
        generateOTP();
        if (verifyOTP()) {
            balance -= amount;
            transactions[txCount++] = -amount;
            saveData();
            printf("Money sent\n");
        }
    } else {
        printf("Insufficient balance\n");
    }
}

void qrPay() {
    int qr, amount;
    printf("Enter QR number: ");
    scanf("%d", &qr);
    printf("Enter amount: ");
    scanf("%d", &amount);
    if (amount > 0 && amount <= balance) {
        generateOTP();
        if (verifyOTP()) {
            balance -= amount;
            transactions[txCount++] = -amount;
            saveData();
            printf("QR payment successful\n");
        }
    }
}

void addMoney() {
    int amount;
    printf("Enter amount: ");
    scanf("%d", &amount);
    if (amount > 0) {
        balance += amount;
        transactions[txCount++] = amount;
        saveData();
        printf("Money added\n");
    }
}

void menu() {
    int choice;
    while (loggedIn) {
        printf("\n1 Balance\n2 Send\n3 Add\n4 QR Pay\n5 History\n6 Logout\n");
        scanf("%d", &choice);
        if (choice == 1) checkBalance();
        else if (choice == 2) sendMoney();
        else if (choice == 3) addMoney();
        else if (choice == 4) qrPay();
        else if (choice == 5) {
            for (int i = 0; i < txCount; i++) {
                if (transactions[i] < 0)
                    printf("Debit %d\n", -transactions[i]);
                else
                    printf("Credit %d\n", transactions[i]);
            }
        } else if (choice == 6) loggedIn = 0;
    }
}

int main() {
    int option;
    loadData();
    while (1) {
        printf("1 Login\n2 Exit\n");
        scanf("%d", &option);
        if (option == 1) {
            login();
            if (loggedIn) menu();
        } else break;
    }
    return 0;
}

==============================
SECTION 2: ANDROID JAVA APP
==============================

MainActivity.java
----------------
package com.example.mpay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int balance = 5000;
    int pin = 1234;
    int otp = 1111;
    EditText input;
    TextView output, historyView;
    SharedPreferences sp;
    String history = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
        historyView = findViewById(R.id.historyText);

        sp = getSharedPreferences("MPAY_PREF", MODE_PRIVATE);
        balance = sp.getInt("balance", 5000);
        history = sp.getString("history", "");
        historyView.setText(history);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button sendBtn = findViewById(R.id.sendBtn);
        Button addBtn = findViewById(R.id.addBtn);
        Button refreshBtn = findViewById(R.id.refreshBtn);

        loginBtn.setOnClickListener(v -> login());
        sendBtn.setOnClickListener(v -> sendMoney());
        addBtn.setOnClickListener(v -> addMoney());
        refreshBtn.setOnClickListener(v -> refreshHistory());
    }

    void login() {
        int entered = Integer.parseInt(input.getText().toString());
        if (entered == pin) output.setText("Login success. Balance " + balance);
        else output.setText("Wrong PIN");
    }

    void sendMoney() {
        int amount = Integer.parseInt(input.getText().toString());
        if (amount <= 0) { output.setText("Enter valid amount"); return; }
        if (amount > balance) { output.setText("Insufficient balance"); return; }
        output.setText("OTP sent: 1111 (use this for demo)");
        int enteredOtp = otp;
        if (enteredOtp == otp) {
            balance -= amount;
            history += "Debit " + amount + "\n";
            saveData();
            output.setText("Money sent. Balance " + balance);
            historyView.setText(history);
        } else {
            output.setText("Invalid OTP");
        }
    }

    void addMoney() {
        int amount = Integer.parseInt(input.getText().toString());
        if (amount <= 0) { output.setText("Enter valid amount"); return; }
        balance += amount;
        history += "Credit " + amount + "\n";
        saveData();
        output.setText("Money added. Balance " + balance);
        historyView.setText(history);
    }

    void refreshHistory() {
        historyView.setText(history);
    }

    void saveData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("balance", balance);
        editor.putString("history", history);
        editor.apply();
    }
}

==============================
activity_main.xml
----------------
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter PIN or Amount"
        android:inputType="number" />

    <Button
        android:id="@+id/loginBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Login" />

    <Button
        android:id="@+id/sendBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Send Money" />

    <Button
        android:id="@+id/addBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Add Money" />

    <Button
        android:id="@+id/refreshBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Refresh History" />

    <TextView
        android:id="@+id/output"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Status"
        android:padding="8dp" />

    <TextView
        android:id="@+id/historyText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="History"
        android:padding="8dp" />

</LinearLayout>

==============================
END OF FILE
==============================
