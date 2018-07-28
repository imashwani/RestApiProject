package com.example.ashwani.rewardcoins.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tran {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("actRecAmt")
    @Expose
    private Double actRecAmt;
    @SerializedName("offCreAmt")
    @Expose
    private Double offCreAmt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getActRecAmt() {
        return actRecAmt;
    }

    public void setActRecAmt(Double actRecAmt) {
        this.actRecAmt = actRecAmt;
    }

    public Double getOffCreAmt() {
        return offCreAmt;
    }

    public void setOffCreAmt(Double offCreAmt) {
        this.offCreAmt = offCreAmt;
    }
}
