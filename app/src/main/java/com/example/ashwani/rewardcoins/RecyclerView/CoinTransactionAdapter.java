package com.example.ashwani.rewardcoins.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashwani.rewardcoins.R;
import com.example.ashwani.rewardcoins.TransactionCls;

import java.util.ArrayList;

public class CoinTransactionAdapter extends RecyclerView.Adapter<CoinTransactionAdapter.CoinTransactionViewHolder> {

    private ArrayList<TransactionCls> transactionList;
    private Context context;

    public CoinTransactionAdapter(Context context, ArrayList<TransactionCls> transactionList) {
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

        //holder.receiverPhonenoTxn.setText(transactionList.get(position).getReceiverPhoneNo());
        holder.timeTxn.setText(transactionList.get(position).getDate());
        holder.actualRechargeAmount.setText(String.valueOf(transactionList.get(position).getActualRechargeAmount()));
        holder.actualAmount.setText(String.valueOf(transactionList.get(position).getAmount()));
        holder.offerCredit.setText(String.valueOf(transactionList.get(position).getOfferCredit()));

//        holder.receiverPhonenoTxn.setText(transactionList.get(position).get());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class CoinTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView actualRechargeAmount, offerCredit, actualAmount, timeTxn;

        public CoinTransactionViewHolder(View itemView) {
            super(itemView);
//            receiverPhonenoTxn = itemView.findViewById(R.id.receiver_phone_no_tv_txn);
            actualAmount = itemView.findViewById(R.id.no_of_coins_txn);
            offerCredit = itemView.findViewById(R.id.offer_credit_tv_txn);
            actualRechargeAmount = itemView.findViewById(R.id.actual_rec_tv_txn);
            timeTxn = itemView.findViewById(R.id.time_tv_txn);
        }
    }
}
