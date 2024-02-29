package com.newstore.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.newstore.store.R;
import com.newstore.store.Utils.Share;

public class ThankYouActivity extends AppCompatActivity {

    Share share;

    TextView firstName, lastName, userMobile, userEmail;
    TextView orderId, finalAmount, orderDate, orderItem, orderStatus;
    AppCompatButton homeBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        share = new Share(this);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userMobile = findViewById(R.id.userMobile);
        userEmail = findViewById(R.id.userEmail);
        orderId = findViewById(R.id.orderId);
        finalAmount = findViewById(R.id.finalAmount);
        orderDate = findViewById(R.id.orderDate);
        orderItem = findViewById(R.id.orderItem);
        orderStatus = findViewById(R.id.orderStatus);
        homeBtn = findViewById(R.id.homeBtn);

        firstName.setText(share.getUserFName());
        lastName.setText(share.getUserLName());
        userMobile.setText(share.getUserMobile());
        userEmail.setText(share.getUserEmail());

        orderId.setText(getIntent().getStringExtra("orderId"));
        finalAmount.setText(getIntent().getStringExtra("finalAmount"));
        orderDate.setText(getIntent().getStringExtra("orderDate"));

        String oItem = getIntent().getStringExtra("orderItem") + " Item(s) / "
                + getIntent().getStringExtra("orderQty") + " Quantity";
        orderItem.setText(oItem);

        String oStatus = getIntent().getStringExtra("orderStatus");
        assert oStatus != null;
        if (oStatus.equals("0")) {
            orderStatus.setText("Order Pending");
        } else {
            orderStatus.setText("Order Complete");
        }

        homeBtn.setOnClickListener(v -> {
            Intent i = new Intent(ThankYouActivity.this, DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ThankYouActivity.this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
