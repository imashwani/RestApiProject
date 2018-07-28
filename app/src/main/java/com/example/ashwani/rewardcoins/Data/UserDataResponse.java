package com.example.ashwani.rewardcoins.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("nm")
    @Expose
    private String nm;
    @SerializedName("TOR")
    @Expose
    private String tOR;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getTOR() {
        return tOR;
    }

    public void setTOR(String tOR) {
        this.tOR = tOR;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
