package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
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
    ImageView coinIconIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        coinIconIV = findViewById(R.id.coin_icon_splash);
        animateIcon(coinIconIV);
        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Util.getTokenKey(), "");
        mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");
        Log.d(TAG, "onCreate: token: " + token);
        Log.d(TAG, "onCreate: " + mobileNo);
        int delayDuration = 500;
        new Handler().postDelayed(() -> {
            Log.d(TAG, "onCreate: after " + delayDuration + " delay");
            Intent intent = null;
            if (token.equals("")) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
//TODO: un comment the below line
                makeApiRequest();
//                intent = new Intent(SplashActivity.this, MemberActivity.class);
//                startActivity(intent);
//                finish();

            }

        }, delayDuration);
    }

    void makeApiRequest() {
        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put("token", "Bearer " + token);
        jsonParams.put("mob", mobileNo);
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

                        intentW.putExtra(Util.getUsernameKey(), username);
                        intentW.putExtra(Util.getAccessLevelKey(), al);
                        intentW.putExtra(Util.getCoinsKey(), coins);
                        intentW.putExtra(Util.getPhonenoKey(), mobileNo);

                        startActivity(intentW);
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this,
                            "Failed response from Server, Retrying...", Toast.LENGTH_SHORT).show();
                    makeApiRequest();
                }
            }

            @Override
            public void onFailure(Call<SplashResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Server Under Maintenance(check network),Retrying...", Toast.LENGTH_SHORT).show();
                makeApiRequest();
            }
        });
    }

    void animateIcon(ImageView imageView) {
        //fading in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(500);
        imageView.setAnimation(fadeIn);
        // Scaling
        Animation scale = new ScaleAnimation(imageView.getScaleX() + 20, imageView.getScaleX(), imageView.getScaleY() + 20, imageView.getScaleY(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
// 1 second duration
        scale.setDuration(1000);
// Moving up
        Animation slideUp = new TranslateAnimation(imageView.getX(), imageView.getX(), 0, imageView.getY());
// 1 second duration
        slideUp.setDuration(1000);
// Animation set to join both scaling and moving
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(true);
        animSet.addAnimation(scale);
        animSet.addAnimation(slideUp);
// Launching animation set
        imageView.startAnimation(animSet);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Application is Starting", Toast.LENGTH_SHORT).show();
    }
}
