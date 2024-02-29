package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserOrderListModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<UserOrderList> userOrderLists;

    public String getStatus() {
        return status;
    }

    public List<UserOrderList> getUserOrderLists() {
        return userOrderLists;
    }

    public static class UserOrderList implements Serializable {

        @SerializedName("o_id")
        private String oId;

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("total_amount")
        private String finalAmount;

        @SerializedName("o_date")
        private String orderDate;

        @SerializedName("o_status")
        private String orderStatus;

        public String getoId() {
            return oId;
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
    }

}
