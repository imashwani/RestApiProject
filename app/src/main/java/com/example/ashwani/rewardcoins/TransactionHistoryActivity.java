package com.example.ashwani.rewardcoins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.ashwani.rewardcoins.RecyclerView.CoinTransaction;
import com.example.ashwani.rewardcoins.RecyclerView.CoinTransactionAdapter;

import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity {

    private static final String TAG = "TransactionHistoryActivity";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CoinTransactionAdapter mComplaintAdapter;
    //    View rootView;
    ProgressBar progressBar;
    ArrayList<CoinTransaction> compList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        progressBar = findViewById(R.id.progressBar_txn);
        startLoading();

    }

    @SuppressLint("LongLogTag")
    private void startLoading() {
        mRecyclerView = findViewById(R.id.recycler_view_txn);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        compList = new ArrayList<>();
        compList = getArrayList();

        mComplaintAdapter = new CoinTransactionAdapter(this, compList);
        mRecyclerView.setAdapter(mComplaintAdapter);

        Log.d(TAG, "onCreateView: list hai " + compList);

    }

    public ArrayList<CoinTransaction> getArrayList() {
        final ArrayList<CoinTransaction> arrayList = new ArrayList<>();
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        arrayList.add(new CoinTransaction(250, "Ranjit", "manoj", "99445566886", "14 aug 2017", "12:55 AM"));
        arrayList.add(new CoinTransaction(250, "Ranjit", "manoj", "99445566886", "14 aug 2017", "12:55 AM"));

        return arrayList;
    }
}
