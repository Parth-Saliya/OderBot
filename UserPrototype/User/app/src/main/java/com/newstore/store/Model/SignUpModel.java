package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignUpModel implements Serializable {

    @SerializedName("status")
    private String status;

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

    public String getStatus() {
        return status;
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
}
