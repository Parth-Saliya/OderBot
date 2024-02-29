package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDeleteModel implements Serializable {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
