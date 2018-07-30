package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.LoginResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int SMS_PERMISSION_CODE = 22;
    EditText phoneNumberET;
    Button submitBT;
    ProgressBar progressBar;
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                int delayDuration = 100;
                new Handler().postDelayed(() -> {
                    askForResponse();
                }, delayDuration);
            }

        });

        phoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isShowLoginButton(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isShowLoginButton(s.toString());
            }
        });

        phoneNumberET.setText("9911416637");
    }

    private void isShowLoginButton(String s) {
        phoneNumberET.setSelection(phoneNumberET.length());
        if (!isValidMobileNb(s)) {
            phoneNumberET.setError("Not a VALID mobile number");
            submitBT.setVisibility(View.INVISIBLE);

        } else {
            submitBT.setVisibility(View.VISIBLE);
        }
    }


    private void askForResponse() {
        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        String mobileNo = phoneNumberET.getText().toString();
        jsonParams.put("mob", mobileNo);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...
        Call<LoginResponse> response = CoinApi.getCoinService().dhlogin(body);

        Log.d(TAG, "onClick: " + jsonParams);

        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getSuccess());

                    if (response.body().getSuccess().toString().equals("true")) {

                        SharedPreferences.Editor editor =
                                getSharedPreferences("com.example.app", Context.MODE_PRIVATE).edit();
                        editor.putString(Util.getPhonenoKey(), mobileNo);
                        editor.apply();

                        Boolean cpin = response.body().getCpin();

                        String msg = response.body().getMsg();
                        Intent intent = null;
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if (cpin) {
                            //He is a new user
                            intent = new Intent(MainActivity.this, OtpVerifyActivity.class);
                            intent.putExtra(Util.getWheretoKey(), "createNewPin");
                        } else {
                            intent = new Intent(MainActivity.this, EnterPinActivity.class);
                        }
                        intent.putExtra(Util.getPhonenoKey(), mobileNo);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("Invalid username or password");
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Server under maintenance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                String err = "Failed response from server\nFailed to Login";
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(err);
                Toast.makeText(MainActivity.this, "Failed response from server\nFailed to Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initViews() {

        phoneNumberET = findViewById(R.id.input_phone_number);
        submitBT = findViewById(R.id.submit_button_main);
        tvError = findViewById(R.id.login_error_tv);
        progressBar = findViewById(R.id.progress_bar_login);

        submitBT.setVisibility(View.INVISIBLE);
    }


    private boolean isValidMobileNb(CharSequence s) {
        Log.d(TAG, "isValidMobileNb: phone number" + s + "\tlength:" + s.toString().length());
        return s.length() == 10;

    }


}
