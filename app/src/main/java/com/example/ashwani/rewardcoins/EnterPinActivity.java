package com.example.ashwani.rewardcoins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.EnterPinResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinActivity extends AppCompatActivity {

    private static final String TAG = "EnterPinActivity";
    EditText pinET;
    Button submitBt;
    String mobileNo = null;
    TextView errorTv, userPhoneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        pinET = findViewById(R.id.input_pin);
        errorTv = findViewById(R.id.error_enter_pin);
        submitBt = findViewById(R.id.submit_bt_pin);
        submitBt.setVisibility(View.INVISIBLE);
        userPhoneTV = findViewById(R.id.user_number_enter_pin);

        if (getIntent().hasExtra(Util.getPhonenoKey())) {
            mobileNo = getIntent().getStringExtra(Util.getPhonenoKey());
            userPhoneTV.setText("Phone Number: +91 " + mobileNo);
        }

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // submit pin to server
                Map<String, Object> jsonParams = new ArrayMap<>();
                //put something inside the map, could be null
                jsonParams.put("mob", mobileNo);
                jsonParams.put("pin", pinET.getText().toString());


                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        (new JSONObject(jsonParams)).toString());
                //serviceCaller is the interface initialized with retrofit.create...
                Call<EnterPinResponse> response = CoinApi.getCoinService().enterPinCall(body);

                Log.d(TAG, "onClick: " + jsonParams);

                response.enqueue(new Callback<EnterPinResponse>() {
                    @Override
                    public void onResponse(Call<EnterPinResponse> call, Response<EnterPinResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getSuccess() == true) {
                                Toast.makeText(EnterPinActivity.this,
                                        "Correct PIN entered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EnterPinActivity.this, OtpVerifyActivity.class);
                                intent.putExtra(Util.getWheretoKey(), "homePage");

                                startActivity(intent);
                                finish();
                            } else {
                                errorTv.setVisibility(View.VISIBLE);
                                errorTv.setText("INVALID PIN,PLEASE RETRY!!");


                            }

                        } else {
                            Log.d(TAG, "onResponse: no data" + response.body());
                            Toast.makeText(EnterPinActivity.this, "Server under Maintenance", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EnterPinResponse> call, Throwable t) {
                        errorTv.setVisibility(View.VISIBLE);
                        errorTv.setText("NO RESPONSE FROM SERVER,PLEASE RETRY!!");
                    }
                });


            }
        });

        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorTv.setVisibility(View.GONE);
                if (s.length() == 4) {
                    submitBt.setVisibility(View.VISIBLE);

                } else {
                    submitBt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
