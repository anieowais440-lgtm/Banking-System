# Banking-System
==============================
MPAY PROJECT
ALL CODE IN ONE FILE
==============================


========================================
SECTION 1
C PROGRAM
MPAY CONSOLE APPLICATION
========================================

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


========================================
SECTION 2
ANDROID JAVA CODE
========================================

MainActivity.java

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
            output.setText("Login success Balance " + balance);
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
        output.setText("Added Balance " + balance);
    }

    void history() {
        startActivity(new Intent(this, HistoryActivity.class));
    }
}


OtpActivity.java

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
                status.setText("Payment success Balance " + MainActivity.balance);
            } else {
                status.setText("Invalid OTP");
            }
        });
    }
}


HistoryActivity.java

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


========================================
SECTION 3
ANDROID XML FILES
========================================

activity_main.xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 android:orientation="vertical"
 android:padding="16dp"
 android:layout_width="match_parent"
 android:layout_height="match_parent">

 <EditText android:id="@+id/input"
  android:hint="Enter PIN or Amount"
  android:inputType="number"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <Button android:id="@+id/loginBtn" android:text="Login"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <Button android:id="@+id/sendBtn" android:text="Send Money"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <Button android:id="@+id/addBtn" android:text="Add Money"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <Button android:id="@+id/historyBtn" android:text="History"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <TextView android:id="@+id/output"
  android:text="Status"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>
</LinearLayout>


activity_otp.xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 android:orientation="vertical"
 android:padding="16dp"
 android:layout_width="match_parent"
 android:layout_height="match_parent">

 <EditText android:id="@+id/otpInput"
  android:hint="Enter OTP"
  android:inputType="number"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <Button android:id="@+id/verifyBtn"
  android:text="Verify OTP"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>

 <TextView android:id="@+id/status"
  android:text="Waiting"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>
</LinearLayout>


activity_history.xml

<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
 android:layout_width="match_parent"
 android:layout_height="match_parent">

 <TextView android:id="@+id/historyText"
  android:text="No data"
  android:padding="16dp"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>
</ScrollView>

==============================
END OF FILE
==============================
