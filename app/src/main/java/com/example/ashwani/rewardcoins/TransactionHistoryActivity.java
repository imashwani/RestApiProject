package com.example.ashwani.rewardcoins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.Tran;
import com.example.ashwani.rewardcoins.Data.TransactionHistoryResponse;
import com.example.ashwani.rewardcoins.RecyclerView.CoinTransactionAdapter;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryActivity extends AppCompatActivity {

    private static final String TAG = "TransactionHistoryActivity";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CoinTransactionAdapter mCoinTransactionAdapter;
    TextView emptyMsgTV;
    String userType;
    ProgressBar progressBar;
    ArrayList<TransactionCls> txnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        progressBar = findViewById(R.id.progressBar_txn);
        emptyMsgTV = findViewById(R.id.txn_history_msg_tv);

        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        userType = sharedPreferences.getString(Util.getAccessLevelKey(), "");

        startLoading();
    }

    private void startLoading() {
        mRecyclerView = findViewById(R.id.recycler_view_txn);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        txnList = new ArrayList<>();
        getArrayList();

    }

    @SuppressLint("LongLogTag")
    void setDataToRV(ArrayList<TransactionCls> transactionList) {
        mRecyclerView.setVisibility(View.VISIBLE);

        mCoinTransactionAdapter = new CoinTransactionAdapter(this, transactionList);

        mRecyclerView.setAdapter(mCoinTransactionAdapter);
        mRecyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: list hai " + transactionList.toString());
    }

    @SuppressLint("LongLogTag")
    public ArrayList<TransactionCls> getArrayList() {
        final ArrayList<TransactionCls> arrayList = new ArrayList<>();

//        arrayList.add(new TransactionCls("14 aug 2017 12:55 AM", "200", "122", "56" ));
//         arrayList.add(new CoinTransaction(250, "Ranjit", "manoj", "99445566886", "14 aug 2017", "12:55 AM"));

        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
//        token = sharedPreferences.getString(Util.getTokenKey(), "");
        String userType = sharedPreferences.getString(Util.getAccessLevelKey(), "");
        String mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");

        jsonParams.put("TOS", userType);
        jsonParams.put("ttype", "Debit");
        jsonParams.put("mob", mobileNo);
        Log.d(TAG, "onClick: json sending data : " + jsonParams.toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...
        Call<TransactionHistoryResponse> transactionHistoryResponseCall = CoinApi.getCoinService().coinTxnHistory(body);

        transactionHistoryResponseCall.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                Log.d(TAG, "onResponse: in trans history resp from server" + response.body().getSuccess());
                if (response.body().getSuccess()) {

                    List<Tran> trans = response.body().getTrans();
                    Log.d(TAG, "onResponse: " + trans.get(0).toString());
                    String date, amount, actRecAmt, offerCreditAmt;


                    if (userType.equals("C")) {
                        for (Tran t : trans) {

                            String pattern = "dd MMM yyyy\n hh:mm a";
                            Date date1 = null;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            try {
                                date1 = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a").parse(t.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            date = simpleDateFormat.format(date1);
                            amount = "\u20B9 " + t.getAmount().toString();
                            actRecAmt = "Collection: " + "\u20B9" + t.getActRecAmt().toString();
                            offerCreditAmt = "Offer Credit: " + "\u20B9 " + t.getOffCreAmt();

                            arrayList.add(new TransactionCls(date, amount, actRecAmt, offerCreditAmt));
                        }
                    } else {
                        for (Tran t : trans) {
                            arrayList.add(new TransactionCls(t.getDate(), t.getAmount().toString(),
                                    "", ""));
                        }
                    }

                    if (arrayList.isEmpty()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyMsgTV.setVisibility(View.VISIBLE);
                        emptyMsgTV.setText("No items of transaction are available");
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    } else setDataToRV(arrayList);

                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Toast.makeText(TransactionHistoryActivity.this, "Failed Response from server, check your connection", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());

            }
        });


        return arrayList;
    }
}
