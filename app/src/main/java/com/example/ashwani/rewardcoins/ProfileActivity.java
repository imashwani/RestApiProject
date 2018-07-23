package com.example.ashwani.rewardcoins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    Button changePassBT;
    TextView userName, userPhone, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Profile");


        changePassBT.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePinActivity.class);
            startActivity(intent);
        });

    }

    private void initView() {
        changePassBT = findViewById(R.id.bt_change_password_profile);
        userName = findViewById(R.id.user_name_profile);
        userEmail = findViewById(R.id.user_email_profile);
        userPhone = findViewById(R.id.user_phone_profile);

        if (getIntent().hasExtra("phone")) {
            userPhone.setText(getIntent().getStringExtra("phone"));
        }

    }
}
