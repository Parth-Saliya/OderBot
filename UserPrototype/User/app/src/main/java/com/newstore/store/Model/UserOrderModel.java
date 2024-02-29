package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserOrderModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("order_amount")
    private String finalAmount;

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("total_item")
    private String totalItem;

    @SerializedName("total_qty")
    private String totalQty;

    public String getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public String getTotalQty() {
        return totalQty;
    }
}
