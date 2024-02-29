package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductListModel implements Serializable {

    @SerializedName("status")
    private String status;

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
    private List<ProductList> productLists;

    public String getStatus() {
        return status;
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

    public List<ProductList> getProductLists() {
        return productLists;
    }

    public static class ProductList implements Serializable {

        @SerializedName("pm_id")
        private String productId;

        @SerializedName("pm_img")
        private String productImage;

        @SerializedName("pm_name")
        private String productName;

        @SerializedName("pm_description")
        private String productDetail;

        @SerializedName("pm_qty")
        private String productQty;

        @SerializedName("pm_price")
        private String productAmount;

        public String getProductId() {
            return productId;
        }

        public String getProductImage() {
            return productImage;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductDetail() {
            return productDetail;
        }

        public String getProductQty() {
            return productQty;
        }

        public String getProductAmount() {
            return productAmount;
        }
    }
}
