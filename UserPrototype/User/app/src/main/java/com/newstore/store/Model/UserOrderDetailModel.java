package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserOrderDetailModel implements Serializable {

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

    @SerializedName("sm_id")
    private String sId;

    @SerializedName("sm_name")
    private String sName;

    @SerializedName("sm_address")
    private String sAddress;

    @SerializedName("sm_contact")
    private String sMobile;

    @SerializedName("sm_email")
    private String sEmail;

    @SerializedName("sm_img")
    private String sImage;

    @SerializedName("sm_latitude")
    private String sLat;

    @SerializedName("sm_longitude")
    private String sLog;

    @SerializedName("data")
    private List<UserOrderDetail> userOrderDetails;

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

    public String getsId() {
        return sId;
    }

    public String getsName() {
        return sName;
    }

    public String getsAddress() {
        return sAddress;
    }

    public String getsMobile() {
        return sMobile;
    }

    public String getsEmail() {
        return sEmail;
    }

    public String getsImage() {
        return sImage;
    }

    public String getsLat() {
        return sLat;
    }

    public String getsLog() {
        return sLog;
    }

    public List<UserOrderDetail> getUserOrderDetails() {
        return userOrderDetails;
    }

    public static class UserOrderDetail implements Serializable {

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

        @SerializedName("pReview")
        private String pReview;

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

        public String getpReview() {
            return pReview;
        }
    }

}
