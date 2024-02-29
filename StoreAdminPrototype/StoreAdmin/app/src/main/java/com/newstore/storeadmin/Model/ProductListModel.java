package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductListModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<ProductList> productLists;

    public String getStatus() {
        return status;
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
