package com.example.ashwani.rewardcoins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ashwani.rewardcoins.RecyclerView.CoinTransactionAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DebitTxnFragment extends Fragment {
    FragmentActionListener fragmentActionListener;
    TextView tv;
    Bundle bundle;
    TextView emptyMsgTV;
    View root;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CoinTransactionAdapter mCoinTransactionAdapter;
    ProgressBar progressBar;

    public DebitTxnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_debit_txn, container, false);
        tv = root.findViewById(R.id.debit_txn_history_msg_tv);
        emptyMsgTV = root.findViewById(R.id.debit_txn_history_msg_tv);
        mRecyclerView = root.findViewById(R.id.recycler_view_debit_txn);
        progressBar = root.findViewById(R.id.progressBar_txn_debit);

        return root;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        if (getArguments() != null) {
            Log.d(TAG, "onResume: " + getArguments().get("data"));

            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            setDataToRV(getArguments().getParcelableArrayList("data"));

        }
    }

    @SuppressLint("LongLogTag")
    void setDataToRV(ArrayList<TransactionCls> transactionList) {
        Log.d(TAG, "setDataToRV: " + transactionList);

        if (transactionList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyMsgTV.setVisibility(View.INVISIBLE);
            mCoinTransactionAdapter = new CoinTransactionAdapter(getContext(), transactionList);

            mRecyclerView.setAdapter(mCoinTransactionAdapter);
            mRecyclerView.setHasFixedSize(true);
            progressBar.setVisibility(View.INVISIBLE);
        } else {

            progressBar.setVisibility(View.INVISIBLE);
            emptyMsgTV.setVisibility(View.VISIBLE);
            emptyMsgTV.setText("No Debit Transtion done yet");
        }

        Log.d(TAG, "onCreateView: list hai " + transactionList.toString());
    }

}
