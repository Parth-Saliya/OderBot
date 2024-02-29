package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreLatLangModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("sm_latitude")
    private String sLat;

    @SerializedName("sm_longitude")
    private String sLog;

    public String getStatus() {
        return status;
    }

    public String getsLat() {
        return sLat;
    }

    public String getsLog() {
        return sLog;
    }
}
