package com.example.ashwani.rewardcoins.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SplashResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("al")
    @Expose
    private String al;
    @SerializedName("coins")
    @Expose
    private Integer coins;
    @SerializedName("name")
    @Expose
    private String name;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getAl() {
        return al;
    }

    public void setAl(String al) {
        this.al = al;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
