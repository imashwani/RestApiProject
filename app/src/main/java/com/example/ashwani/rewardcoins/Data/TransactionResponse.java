package com.example.ashwani.rewardcoins.Data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("tamt")
    @Expose
    private Integer tamt;
    @SerializedName("ubal")
    @Expose
    private Integer ubal;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getTamt() {
        return tamt;
    }

    public void setTamt(Integer tamt) {
        this.tamt = tamt;
    }

    public Integer getUbal() {
        return ubal;
    }

    public void setUbal(Integer ubal) {
        this.ubal = ubal;
    }

}
