package com.example.ashwani.rewardcoins.RecyclerView;

public class CoinTransaction {
    private int numberOfCoins;
    private String senderName, receiverName, receiverPhoneNo, date, time;

    public CoinTransaction(int numberOfCoins, String senderName, String receiverName, String receiverPhoneNo, String date, String time) {
        this.numberOfCoins = numberOfCoins;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.receiverPhoneNo = receiverPhoneNo;
        this.date = date;
        this.time = time;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public void setNumberOfCoins(int numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhoneNo() {
        return receiverPhoneNo;
    }

    public void setReceiverPhoneNo(String receiverPhoneNo) {
        this.receiverPhoneNo = receiverPhoneNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
