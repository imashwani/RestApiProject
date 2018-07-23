package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.SplashResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    String token, mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Util.getTokenKey(), "");
        mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");

        int delayDuration = 800;
        new Handler().postDelayed(() -> {

            Intent intent = null;
            if (token.length() < 1) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                makeApiRequest();
            }

        }, delayDuration);
    }

    void makeApiRequest() {
        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put(Util.getTokenKey(), token);
        jsonParams.put(Util.getPhonenoKey(), mobileNo);
//        jsonParams.put("pin", pinET.getText().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...
        Call<SplashResponse> response = CoinApi.getCoinService().splashVerify(body);

        Log.d(TAG, "onClick: " + jsonParams);

        response.enqueue(new Callback<SplashResponse>() {
            @Override
            public void onResponse(Call<SplashResponse> call, Response<SplashResponse> response) {
                if (response.body() != null) {
                    String success = response.body().getSuccess().toString();
                    if (success == "true") {
                        String username = response.body().getName().toString(), al, coins;
                        al = response.body().getAl().toString();
                        coins = response.body().getCoins().toString();

                        Intent intentW = new Intent(SplashActivity.this, MemberActivity.class);

                        intentW.putExtra("username", username);
                        intentW.putExtra("acessLevel", al);
                        intentW.putExtra("coins", coins);

                        startActivity(intentW);
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "No response from Server, Retrying...", Toast.LENGTH_SHORT).show();
                    makeApiRequest();
                }
            }

            @Override
            public void onFailure(Call<SplashResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Server Under Maintenance,Retrying...", Toast.LENGTH_SHORT).show();
                makeApiRequest();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Application is Starting", Toast.LENGTH_SHORT).show();
    }
}
