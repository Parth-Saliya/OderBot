package com.newstore.store.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreListModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<StoreList> storeLists;

    public String getStatus() {
        return status;
    }

    public List<StoreList> getStoreLists() {
        return storeLists;
    }

    public static class StoreList implements Serializable {

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
    }

}
