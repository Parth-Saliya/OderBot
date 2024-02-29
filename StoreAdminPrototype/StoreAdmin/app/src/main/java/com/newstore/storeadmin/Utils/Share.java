package com.newstore.storeadmin.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.newstore.storeadmin.R;

import static android.content.Context.MODE_PRIVATE;

public class Share {
    private SharedPreferences sharedpreferences;

    public Share(Context context) {
        sharedpreferences = context.getSharedPreferences(Preference.MyPREFERENCES, MODE_PRIVATE);
    }

    public static void showToast(Activity activity, String msg) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(msg);
        Typeface typeface = ResourcesCompat.getFont(activity, R.font.lato);
        text.setTypeface(typeface);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void setStoreLoginValue(String storeId, String storeName, String storeAddress,
                                   String storeMobile, String storeEmail, String storeImage) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Preference.KEY_STORE_ID, storeId);
        editor.putString(Preference.KEY_STORE_NAME, storeName);
        editor.putString(Preference.KEY_STORE_ADDRESS, storeAddress);
        editor.putString(Preference.KEY_STORE_MOBILE, storeMobile);
        editor.putString(Preference.KEY_STORE_EMAIL, storeEmail);
        editor.putString(Preference.KEY_STORE_IMAGE, storeImage);
        editor.putBoolean(Preference.KEY_STORE_LOGIN, true);
        editor.apply();
    }

    public void setStoreLatLog(String storeLat, String storeLog) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Preference.KEY_STORE_LAT, storeLat);
        editor.putString(Preference.KEY_STORE_LOG, storeLog);
        editor.putBoolean(Preference.KEY_STORE_LAT_LNG, true);
        editor.apply();
    }

    public boolean IsStoreLogin() {
        return sharedpreferences.getBoolean(Preference.KEY_STORE_LOGIN, false);
    }

    public boolean IsStoreLatLng() {
        return sharedpreferences.getBoolean(Preference.KEY_STORE_LAT_LNG, false);
    }

    public String getStoreId() {
        return sharedpreferences.getString(Preference.KEY_STORE_ID, "");
    }

    public String getStoreName() {
        return sharedpreferences.getString(Preference.KEY_STORE_NAME, "");
    }

    public String getStoreAddress() {
        return sharedpreferences.getString(Preference.KEY_STORE_ADDRESS, "");
    }

    public String getStoreMobile() {
        return sharedpreferences.getString(Preference.KEY_STORE_MOBILE, "");
    }

    public String getStoreEmail() {
        return sharedpreferences.getString(Preference.KEY_STORE_EMAIL, "");
    }

    public String getStoreImage() {
        return sharedpreferences.getString(Preference.KEY_STORE_IMAGE, "");
    }

    public String getStoreLat() {
        return sharedpreferences.getString(Preference.KEY_STORE_LAT, "");
    }

    public String getStoreLog() {
        return sharedpreferences.getString(Preference.KEY_STORE_LOG, "");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }
}
