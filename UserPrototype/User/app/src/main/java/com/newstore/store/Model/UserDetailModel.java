package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetailModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("total_order")
    private String OrderTotal;

    @SerializedName("pending_order")
    private String OrderPending;

    @SerializedName("complete_order")
    private String OrderComplete;

    @SerializedName("cancel_order")
    private String OrderCancel;

    public String getStatus() {
        return status;
    }

    public String getOrderTotal() {
        return OrderTotal;
    }

    public String getOrderPending() {
        return OrderPending;
    }

    public String getOrderComplete() {
        return OrderComplete;
    }

    public String getOrderCancel() {
        return OrderCancel;
    }
}
