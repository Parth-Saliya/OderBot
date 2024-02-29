package com.newstore.storeadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserReviewShowModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("rm_q1")
    private String reviewQ1;

    @SerializedName("rm_q2")
    private String reviewQ2;

    @SerializedName("rm_review")
    private String reviewText;

    public String getStatus() {
        return status;
    }

    public String getReviewQ1() {
        return reviewQ1;
    }

    public String getReviewQ2() {
        return reviewQ2;
    }

    public String getReviewText() {
        return reviewText;
    }
}
