package com.example.ashwani.rewardcoins;

public class TransactionCls {
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
}
