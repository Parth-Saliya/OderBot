package com.newstore.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.newstore.store.Activity.DashboardActivity;
import com.newstore.store.Activity.LoginActivity;
import com.newstore.store.Utils.Share;

public class MainActivity extends AppCompatActivity {

    Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share = new Share(this);

        new Handler().postDelayed(() -> {

            if (share.IsUserLogin()){
                Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }else {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

        }, 2000);
    }
}
