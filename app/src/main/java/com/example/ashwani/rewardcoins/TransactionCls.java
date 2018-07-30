package com.example.ashwani.rewardcoins;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionCls implements Parcelable {
// "_id": "5b5b14d5eae79d42b4eed64d",
//         "date": "7/27/2018, 6:19:25 PM",
//         "amount": 105,
//         "actRecAmt": 100,
//         "offCreAmt": 5

    String date, amount, actualRechargeAmount, offerCredit;

    public TransactionCls(String date, String amount, String actualRechargeAmount, String offerCredit) {
        this.date = date;
        this.amount = amount;
        this.actualRechargeAmount = actualRechargeAmount;
        this.offerCredit = offerCredit;
    }

    public static final Creator<TransactionCls> CREATOR = new Creator<TransactionCls>() {
        @Override
        public TransactionCls createFromParcel(Parcel in) {
            return new TransactionCls(in);
        }

        @Override
        public TransactionCls[] newArray(int size) {
            return new TransactionCls[size];
        }
    };

    protected TransactionCls(Parcel in) {
        date = in.readString();
        amount = in.readString();
        actualRechargeAmount = in.readString();
        offerCredit = in.readString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getActualRechargeAmount() {
        return actualRechargeAmount;
    }

    public void setActualRechargeAmount(String actualRechargeAmount) {
        this.actualRechargeAmount = actualRechargeAmount;
    }

    public String getOfferCredit() {
        return offerCredit;
    }

    public void setOfferCredit(String offerCredit) {
        this.offerCredit = offerCredit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(amount);
        dest.writeString(actualRechargeAmount);
        dest.writeString(offerCredit);
    }
}
