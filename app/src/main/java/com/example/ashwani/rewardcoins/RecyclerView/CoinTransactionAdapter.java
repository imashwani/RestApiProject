package com.example.ashwani.rewardcoins.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashwani.rewardcoins.R;

import java.util.ArrayList;

public class CoinTransactionAdapter extends RecyclerView.Adapter<CoinTransactionAdapter.CoinTransactionViewHolder> {

    private ArrayList<CoinTransaction> transactionList;
    private Context context;

    public CoinTransactionAdapter(Context context, ArrayList<CoinTransaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;

    }

    @NonNull
    @Override
    public CoinTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coin_transaction_single_item, parent, false);

        return new CoinTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinTransactionViewHolder holder, int position) {
        //set data to each of textViews
        holder.timeTxn.setText(transactionList.get(position).getTime());
        holder.receiverPhonenoTxn.setText(transactionList.get(position).getReceiverPhoneNo());
        holder.noOfCoinsTxn.setText(String.valueOf(transactionList.get(position).getNumberOfCoins()));
        holder.receiverPhonenoTxn.setText(transactionList.get(position).getReceiverPhoneNo());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class CoinTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView receiverPhonenoTxn, noOfCoinsTxn, timeTxn;

        public CoinTransactionViewHolder(View itemView) {
            super(itemView);
            receiverPhonenoTxn = itemView.findViewById(R.id.receiver_phone_no_tv_txn);
            noOfCoinsTxn = itemView.findViewById(R.id.no_of_coins_txn);
            timeTxn = itemView.findViewById(R.id.time_tv_txn);
        }
    }
}
