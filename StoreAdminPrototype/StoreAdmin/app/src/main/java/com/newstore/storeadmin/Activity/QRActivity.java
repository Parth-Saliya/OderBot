package com.newstore.storeadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.WriterException;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    ImageView QRImage;

    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

        orderId = getIntent().getStringExtra("orderId");
        share = new Share(this);
        QRImage = findViewById(R.id.QRImage);
        back = findViewById(R.id.back);

        String data = orderId + share.getStoreId();

        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.encodeAsBitmap();
            QRImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
}
