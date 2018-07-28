package com.example.ashwani.rewardcoins.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("trans")
    @Expose
    private List<Tran> trans = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Tran> getTrans() {
        return trans;
    }

    public void setTrans(List<Tran> trans) {
        this.trans = trans;
    }

}
