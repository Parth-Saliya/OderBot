package com.newstore.store.Utils;

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

import com.newstore.store.R;

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

    public void setStoreLoginValue(String userId, String userFirstName, String userLastName,
                                   String userMobile, String userEmail, String userImage) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Preference.KEY_USER_ID, userId);
        editor.putString(Preference.KEY_USER_FIRST_NAME, userFirstName);
        editor.putString(Preference.KEY_USER_LAST_NAME, userLastName);
        editor.putString(Preference.KEY_USER_MOBILE, userMobile);
        editor.putString(Preference.KEY_USER_EMAIL, userEmail);
        editor.putString(Preference.KEY_USER_IMAGE, userImage);
        editor.putBoolean(Preference.KEY_USER_LOGIN, true);
        editor.apply();
    }

    public boolean IsUserLogin() {
        return sharedpreferences.getBoolean(Preference.KEY_USER_LOGIN, false);
    }

    public String getUserId() {
        return sharedpreferences.getString(Preference.KEY_USER_ID, "");
    }

    public String getUserFName() {
        return sharedpreferences.getString(Preference.KEY_USER_FIRST_NAME, "");
    }

    public String getUserLName() {
        return sharedpreferences.getString(Preference.KEY_USER_LAST_NAME, "");
    }

    public String getUserMobile() {
        return sharedpreferences.getString(Preference.KEY_USER_MOBILE, "");
    }

    public String getUserEmail() {
        return sharedpreferences.getString(Preference.KEY_USER_EMAIL, "");
    }

    public String getUserImage() {
        return sharedpreferences.getString(Preference.KEY_USER_IMAGE, "");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }
}
