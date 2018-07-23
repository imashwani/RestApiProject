package com.example.ashwani.rewardcoins;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
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
import com.example.ashwani.rewardcoins.Data.OtpHVerifyResponse;
import com.example.ashwani.rewardcoins.Data.OtpLVerifyResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Character.isDigit;

public class OtpVerifyActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 22;
    private static final String TAG = "opt verify activity";

    BroadcastReceiver broadcastReceiver;
    TextView userPhoneNumber, msgUnderPB, timer;
    Button submitBT;
    EditText otpET;
    ProgressBar progressBar;
    String mobileNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        init();
        mobileNo = getSharedPreferences("com.example.app", Context.MODE_PRIVATE).getString(Util.getPhonenoKey(), "");
        Log.d(TAG, "onCreate: mobile no: " + mobileNo);
        if (mobileNo.length() > 0)
            userPhoneNumber.setText("+91 " + mobileNo);


        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpET.getText().toString();
                if (code.length() == 4) {
                    sendCodeToServer(code);
                }
            }
        });

        otpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                otpET.setSelection(otpET.length());

                boolean b = true;
                if (s.length() != 4) {
                    b = false;

                } else {

                    for (int i = 0; i < 4; i++)
                        if (!isDigit(s.charAt(i))) {
                            b = false;
                            break;
                        }
                }

                if (b) {
                    sendCodeToServer(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final int[] time = {30};
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("0:" + checkDigit(time[0]));
                time[0]--;
            }

            public void onFinish() {
                //when the timer reaches 0:00
                otpNotDetected();

            }

        }.start();


        broadcastReceiver = new MyBroadcastReceiver() {
            @Override
            void updateUI(String code) {
                Log.d(TAG, "updateUI: code received : " + code);
                if (code != null)
                    otpET.setText(code);
                else {
                    Toast.makeText(OtpVerifyActivity.this, "Enter the OTP manually",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void otpNotDetected() {
        msgUnderPB.setText("Please enter the OTP manually");
        submitBT.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.GONE);
    }

    private void sendCodeToServer(String code) {
        //send phone and 4 digit otp and on success start memberActivity
        //show PB with msg verifying OTP
        progressBar.setVisibility(View.VISIBLE);
        msgUnderPB.setText("Verifying the OTP");

        String whereTo = null;
        if (getIntent().hasExtra(Util.getWheretoKey())) {
            whereTo = getIntent().getStringExtra(Util.getWheretoKey());

            switch (whereTo) {
                case "createNewPin":
                    sendToNewPin(code);
                    break;

                case "homePage"://memberActivity

                    sendToMemberAct(code);
                    break;

                case "changePin"://forget password
                    break;

                default:
                    Log.d(TAG, "sendCodeToServer: Invalid whereto: " + whereTo);
            }
        } else {
            Log.d(TAG, "sendCodeToServer: whereTo not found in intent extra:" + whereTo);
        }


    }

    private void sendToMemberAct(String code) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        SharedPreferences pref =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        mobileNo = pref.getString(Util.getPhonenoKey(), "");
        Log.d(TAG, "sendToMemberAct: mobno" + mobileNo);
        jsonParams.put("mob", mobileNo);
        jsonParams.put("otp", code);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...

        Call<OtpHVerifyResponse> response = CoinApi.getCoinService().verifyHomeotp(body);

        response.enqueue(new Callback<OtpHVerifyResponse>() {
            @Override
            public void onResponse(Call<OtpHVerifyResponse> call, Response<OtpHVerifyResponse> response) {
                if (response.body().getSuccess() == true) {

                    Intent intent = new Intent(OtpVerifyActivity.this, MemberActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OtpHVerifyResponse> call, Throwable t) {

            }
        });

    }

    private void sendToNewPin(String code) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put("mob", mobileNo);
        jsonParams.put("otp", code);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...

        Call<OtpLVerifyResponse> response = CoinApi.getCoinService().verifyLoginOtp(body);

        response.enqueue(new Callback<OtpLVerifyResponse>() {
            @Override
            public void onResponse(Call<OtpLVerifyResponse> call, Response<OtpLVerifyResponse> response) {

                if (response.body().getSuccess().toString().equals("true")) {
                    Intent intent = new Intent(OtpVerifyActivity.this, CreatePinActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OtpLVerifyResponse> call, Throwable t) {
            }
        });

    }

    private void init() {
        otpET = findViewById(R.id.et_otp);
        progressBar = findViewById(R.id.progress_bar_otp);
        submitBT = findViewById(R.id.submit_bt_otp);
        userPhoneNumber = findViewById(R.id.phone_no_otp);
        msgUnderPB = findViewById(R.id.msg_under_pb_otp_tv);
        timer = findViewById(R.id.timer_otp);

        timer.setText("");
        submitBT.setVisibility(View.INVISIBLE);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("OTP Verification");

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // SMS related task you need to do.
                    registerSmsReceiver();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    otpNotDetected();

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerSmsReceiver();
        if (!isSmsPermissionGranted()) {
            requestReadAndSendSmsPermission();
        }
    }

    private void registerSmsReceiver() {
        //registering the BR
        Log.d(TAG, "registerSmsReceiver: initiated");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //un registering the BR
        unregisterReceiver(broadcastReceiver);
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

}
