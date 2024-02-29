package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreOrderDetailModel implements Serializable {

    @SerializedName("status")
    private String status;

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

    @SerializedName("um_id")
    private String uId;

    @SerializedName("um_f_name")
    private String uFName;

    @SerializedName("um_l_name")
    private String uLName;

    @SerializedName("um_contact")
    private String uMobile;

    @SerializedName("um_email")
    private String uEmail;

    @SerializedName("um_img")
    private String uImage;

    @SerializedName("data")
    private List<StoreOrderDetail> storeOrderDetails;

    public String getStatus() {
        return status;
    }

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

    public String getuId() {
        return uId;
    }

    public String getuFName() {
        return uFName;
    }

    public String getuLName() {
        return uLName;
    }

    public String getuMobile() {
        return uMobile;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuImage() {
        return uImage;
    }

    public List<StoreOrderDetail> getStoreOrderDetails() {
        return storeOrderDetails;
    }

    public static class StoreOrderDetail implements Serializable {

        @SerializedName("pId")
        private String pId;

        @SerializedName("pName")
        private String pName;

        @SerializedName("pAmount")
        private String pAmount;

        @SerializedName("pQty")
        private String pQty;

        @SerializedName("pImage")
        private String pImage;

        public String getpId() {
            return pId;
        }

        public String getpName() {
            return pName;
        }

        public String getpAmount() {
            return pAmount;
        }

        public String getpQty() {
            return pQty;
        }

        public String getpImage() {
            return pImage;
        }
    }
}
