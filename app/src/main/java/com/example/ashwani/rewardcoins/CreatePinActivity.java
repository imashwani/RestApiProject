package com.example.ashwani.rewardcoins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

public class CreatePinActivity extends AppCompatActivity {
    TextView error;
    EditText newPin1, newPin2;
    Button createPinBT;
    String mobileNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Pin");

        initView();

        mobileNo = getIntent().getStringExtra(Util.getPhonenoKey());

        createPinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
                int i = checkData();
                if (i == 1) {
                    //TODO: call api to change password and navigate to OTP activity

                    Map<String, Object> jsonParams = new ArrayMap<>();
                    //put something inside the map, could be null
                    jsonParams.put("mob", mobileNo);
                    jsonParams.put("pin", newPin1.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
                    //serviceCaller is the interface initialized with retrofit.create...
                    Call<EnterPinResponse> response = CoinApi.getCoinService().enterPinCall(body);

                    response.enqueue(new Callback<EnterPinResponse>() {
                        @Override
                        public void onResponse(Call<EnterPinResponse> call, Response<EnterPinResponse> response) {
                            if (response.body().getSuccess() == true) {
                                Toast.makeText(CreatePinActivity.this, "New Pin has been created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreatePinActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(CreatePinActivity.this,
                                        "Invalid response from server!\n plz RETRY", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EnterPinResponse> call, Throwable t) {

                        }
                    });

                } else if (i == 0) {
                    showError("Fields can't be empty");
                } else if (i == 2) {
                    showError("new entered pins don't match!");
                }

            }
        });

    }

    private int checkData() {
        //1 represents all things are correct and good to go
        int b = 1;
        String new1 = newPin1.getText().toString(),
                new2 = newPin2.getText().toString();

        if (new1.length() == 0 || new2.length() == 0) {
            b = 0;
            return b;
        }

        if (!newPin1.equals(newPin2)) {
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

        newPin1 = findViewById(R.id.new_pin1);
        newPin2 = findViewById(R.id.new_pin2);
        createPinBT = findViewById(R.id.bt_change_pin);
        error = findViewById(R.id.error_change_pin);
        error.setVisibility(View.GONE);
        error.setText("");
    }
}
