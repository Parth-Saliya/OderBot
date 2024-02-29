package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreOrderListModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<StoreOrderList> storeOrderLists;

    public String getStatus() {
        return status;
    }

    public List<StoreOrderList> getStoreOrderLists() {
        return storeOrderLists;
    }

    public static class StoreOrderList implements Serializable {

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
