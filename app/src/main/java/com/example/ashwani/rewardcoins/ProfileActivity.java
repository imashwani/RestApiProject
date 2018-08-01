package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    Button changePassBT;
    TextView userNameTv, userPhoneTv, userTypeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        String name, userType, mobileNo;
        name = sharedPreferences.getString(Util.getUsernameKey(), "");
        userType = sharedPreferences.getString(Util.getAccessLevelKey(), "");
        mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");

        //set data to the view
        userTypeTv.setText("User Type: " + userType);
        userPhoneTv.setText("+91 " + mobileNo);
        userNameTv.setText(name);



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
        userNameTv = findViewById(R.id.user_name_tv_profile);
        userTypeTv = findViewById(R.id.user_type_tv_profile);
        userPhoneTv = findViewById(R.id.user_phone_tv_profile);

    }
}
