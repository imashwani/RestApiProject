package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
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
import com.example.ashwani.rewardcoins.Data.CreatePinResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePinActivity extends AppCompatActivity {
    private static final String TAG = "CreatePinActivity";
    TextView error;
    EditText newPinET, confirmPinET;
    Button createPinBT;
    String mobileNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Pin");

        initView();
        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
//        token = sharedPreferences.getString(Util.getTokenKey(), "");
        mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");

        createPinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
                int i = checkData();
                Log.d(TAG, "onClick: cheack data" + i);
                if (i == 1) {
                    //TODO: call api to change password and navigate to OTP activity

                    Map<String, Object> jsonParams = new ArrayMap<>();
                    //put something inside the map, could be null
                    jsonParams.put("mob", mobileNo);
                    jsonParams.put("pin", newPinET.getText().toString());
                    Log.d(TAG, "onClick: json sending data : " + jsonParams);

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
                    //serviceCaller is the interface initialized with retrofit.create...
                    Call<CreatePinResponse> response = CoinApi.getCoinService().createPinCall(body);

                    response.enqueue(new Callback<CreatePinResponse>() {
                        @Override
                        public void onResponse(Call<CreatePinResponse> call, Response<CreatePinResponse> response) {
                            if (response == null) {
                                Toast.makeText(CreatePinActivity.this,
                                        "Null response from server", Toast.LENGTH_SHORT).show();
                            } else if (response.body().getSuccess() == true) {
                                Toast.makeText(CreatePinActivity.this, "New Pin has been created", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: msg from server on success" + response.body().getMsg());

                                Intent intent = new Intent(CreatePinActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(CreatePinActivity.this,
                                        "Invalid response from server!\n plz RETRY", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreatePinResponse> call, Throwable t) {
                        }
                    });

                } else if (i == 0) {
                    showError("Fields can't be empty");
                } else if (i == 2) {
                    showError("new entered pins don't match!");
                }
            }
        });

        newPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 4) {
                    newPinET.setError("PIN must of 4 digit");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 4) {
                    newPinET.setError("PIN must of 4 digit");
                }

            }
        });

    }


    private int checkData() {
        //1 represents all things are correct and good to go
        int b = 1;
        String new1 = newPinET.getText().toString(),
                new2 = confirmPinET.getText().toString();

        if (new1.length() == 0 || new2.length() == 0) {
            b = 0;
            return b;
        }

        if (!new1.equals(new2)) {
            b = 2;//2 represents new entered pins don't match
            return b;
        }

        return b;
    }

    private void showError(String s) {
        error.setVisibility(View.VISIBLE);
        error.setText(s);
    }

    private void initView() {

        newPinET = findViewById(R.id.create_new_pin_et);
        confirmPinET = findViewById(R.id.create_confirm_pin_et);
        createPinBT = findViewById(R.id.bt_create_pin);
        error = findViewById(R.id.error_create_pin);

        error.setVisibility(View.GONE);
        error.setText("");
    }
}
