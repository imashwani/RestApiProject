package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.Tran;
import com.example.ashwani.rewardcoins.Data.TransactionHistoryResponse;
import com.example.ashwani.rewardcoins.Fragments.CreditTxnFragment;
import com.example.ashwani.rewardcoins.Fragments.DebitTxnFragment;
import com.example.ashwani.rewardcoins.Fragments.FragmentActionListener;

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

public class TransactionHistoryActivity extends AppCompatActivity implements FragmentActionListener {

    private static final String TAG = "TrxHistoryActivity";

    String userType, mobileNo;

    ArrayList<TransactionCls> creditTxnList, debitTxnList;
    boolean isCreditReq = false, isDebitReq = false;

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.app", Context.MODE_PRIVATE);

        userType = sharedPreferences.getString(Util.getAccessLevelKey(), "");
        mobileNo = sharedPreferences.getString(Util.getPhonenoKey(), "");

        tabLayout = findViewById(R.id.tabs_transaction_history);
        viewPager = findViewById(R.id.viewpager_transaction_history);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        if (isDebitReq == false) {
                            isDebitReq = true;
                            if (debitTxnList == null)
                                loadList(position);
                            else
                                updateAdapter(position);
                        }
                        break;
                    case 1:
                        if (isCreditReq == false) {
                            isCreditReq = true;
                            if (creditTxnList == null)
                                loadList(position);
                            else updateAdapter(position);
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Log.d(TAG, "onCreate: view pager current" + viewPager.getCurrentItem());

        loadList(0);
//        int delayDuration = 500;
//        new Handler().postDelayed(() -> {
//            Log.d(TAG, "onCreate: ");
//            updateAdapter(0);
//
//        }, delayDuration);


//        startLoading();

    }

    private void loadList(int position) {
        String mode = "Debit";

        if (position == 1) mode = "Credit";

        ArrayList<TransactionCls> arrayList = new ArrayList<>();

//        arrayList.add(new TransactionCls("14 aug 2017 12:55 AM", "200", "122", "56" ));
//         arrayList.add(new CoinTransaction(250, "Ranjit", "manoj", "99445566886", "14 aug 2017", "12:55 AM"));

        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null

        jsonParams.put("TOS", userType);
        jsonParams.put("ttype", mode);
        jsonParams.put("mob", mobileNo);
        Log.d(TAG, "onClick: json sending data : " + jsonParams.toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...
        Call<TransactionHistoryResponse> transactionHistoryResponseCall = CoinApi.getCoinService().coinTxnHistory(body);

        transactionHistoryResponseCall.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: in trans history resp from server" + response.body().getSuccess());
                    if (response.body().getSuccess()) {

                        List<Tran> trans = response.body().getTrans();
                        if (trans.size() > 0)
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

                                arrayList.add(new TransactionCls(date, amount,
                                        "", ""));
                            }
                        }

//                    if (arrayList.isEmpty()) {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        emptyMsgTV.setVisibility(View.VISIBLE);
//                        emptyMsgTV.setText("No items of transaction are available");
//                        mRecyclerView.setVisibility(View.INVISIBLE);
//                    } else setDataToRV(arrayList);

                        if (position == 0)
                            debitTxnList = arrayList;
                        else
                            creditTxnList = arrayList;

                        updateAdapter(position);

                    } else
                        Toast.makeText(TransactionHistoryActivity.this, "response success" + response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TransactionHistoryActivity.this, "response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Toast.makeText(TransactionHistoryActivity.this, "Failed Response from server, check your connection", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CreditTxnFragment creditTxnFragment = new CreditTxnFragment();
        creditTxnFragment.setFragmentActionListener(this);
        DebitTxnFragment debitTxnFragment = new DebitTxnFragment();
        debitTxnFragment.setFragmentActionListener(this);

        adapter.addFragment(debitTxnFragment, "Debit Txn");
        Log.d(TAG, "setupViewPager: " + String.valueOf(userType != "C"));
        Log.d(TAG, "setupViewPager: " + userType);
        if (!userType.equals("C"))
            adapter.addFragment(creditTxnFragment, "Credit Txn");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActionPerformed(Bundle bundle) {
        Log.d(TAG, "onActionPerformed: ");
    }

    void updateAdapter(int position) {
        if (position == 0)
            adapter.update(position, debitTxnList);
        else adapter.update(position, creditTxnList);
    }

}
