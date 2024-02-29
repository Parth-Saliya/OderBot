package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreLoginModel implements Serializable {

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

    @SerializedName("sm_status")
    private String sMapFlag;

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

    public String getsMapFlag() {
        return sMapFlag;
    }
}
