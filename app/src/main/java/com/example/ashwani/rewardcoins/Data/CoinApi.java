package com.example.ashwani.rewardcoins.Data;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class CoinApi {

    private static final String baseUrl = "http://87fd451e.ngrok.io/";

    public static CoinService coinService = null;

    //to implement singleton pattern
    public static CoinService getCoinService() {

        if (coinService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            coinService = retrofit.create(CoinService.class);
        }
        return coinService;
    }

    public interface CoinService {

        @POST("house/dhloginChk")
        Call<LoginResponse> dhlogin(@Body RequestBody jsonObject);

        @POST("house/dhsplash")
        Call<SplashResponse> splashVerify(@Body RequestBody jsonObject);

        @POST("house/dhLogin")
        Call<EnterPinResponse> enterPinCall(@Body RequestBody jsonObject);

        @POST("house/creRPIN")
        Call<CreatePinResponse> createPinCall(@Body RequestBody jsonObject);

        @POST("house/verROTPH")
        Call<OtpHVerifyResponse> verifyHomeotp(@Body RequestBody jsonObject);

        @POST("house/verROTPL")
        Call<OtpLVerifyResponse> verifyLoginOtp(@Body RequestBody jsonObject);

        @POST("house/tnpcheck")
        Call<UserDataResponse> userDataFromPhoneNo(@Body RequestBody jsonObject);

        @POST("house/transfer")
        Call<TransactionResponse> coinTransaction(@Body RequestBody jsonObject);

        @POST("house/tranHis")
        Call<TransactionHistoryResponse> coinTxnHistory(@Body RequestBody jsonObject);

    }
}
