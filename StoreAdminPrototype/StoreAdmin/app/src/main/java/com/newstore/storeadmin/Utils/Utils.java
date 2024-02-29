package com.newstore.storeadmin.Utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static String getBase64ImageStringFromBitmap(Bitmap bitmap) {
        String imgString;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] profileImage = byteArrayOutputStream.toByteArray();

            imgString = "data:image/jpeg;base64," + Base64.encodeToString(profileImage, Base64.NO_WRAP);
        } else {
            imgString = "";
        }
        return imgString;
    }

}
