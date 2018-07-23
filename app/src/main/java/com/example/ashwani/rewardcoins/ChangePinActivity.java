package com.example.ashwani.rewardcoins;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePinActivity extends AppCompatActivity {
    TextView error;
    EditText previousPin, newPin1, newPin2;
    Button changeBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");

        initView();

        changeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
                int i = checkData();
                if (i == 1) {
                    //TODO: call api to change password and navigate to OTP activity


                } else if (i == 0) {
                    showError("Fields can't be empty");
                } else if (i == 2) {
                    showError("new entered pins don't match!");
                } else if (i == 3) {
                    showError("New pin is same as previous one!");
                }

            }
        });


    }


    private int checkData() {
        //1 represents all things are correct and good to go
        int b = 1;
        String pre = previousPin.getText().toString(),
                new1 = newPin1.getText().toString(),
                new2 = newPin2.getText().toString();

        if (pre.length() == 0 || new1.length() == 0 || new2.length() == 0) {
            b = 0;
            return b;
        }

        if (!newPin1.equals(newPin2)) {
            b = 2;//2 represents new entered pins don't match
            return b;
        }

        if (pre.equals(new1) | pre.equals(new2)) {
            b = 3;//3 represents old and new are same pin
            return b;
        }

        return b;
    }

    private void showError(String s) {
        error.setVisibility(View.VISIBLE);
        error.setText(s);
    }

    private void initView() {
        previousPin = findViewById(R.id.prev_pin);
        newPin1 = findViewById(R.id.new_pin1);
        newPin2 = findViewById(R.id.new_pin2);
        changeBT = findViewById(R.id.bt_change_pin);
        error = findViewById(R.id.error_change_pin);
        error.setVisibility(View.GONE);
        error.setText("");
    }
}
