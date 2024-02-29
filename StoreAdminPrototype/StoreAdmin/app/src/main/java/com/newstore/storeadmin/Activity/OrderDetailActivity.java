package com.newstore.storeadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newstore.storeadmin.Adapter.OrderDetailAdapter;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.StoreOrderDetailModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    TextView orderNo, orderAmount, orderStatus, orderDate;
    TextView userName, userEmail, userMobile;
    public String order_id, orderID, uId, o_status;
    TextView finalAmount;
    AppCompatButton storeQR;

    ProgressBar pdLay;
    NestedScrollView mainLay;
    RelativeLayout lay3;

    RecyclerView productListRec;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = getIntent().getStringExtra("oId");

        share = new Share(this);
        back = findViewById(R.id.back);
        orderNo = findViewById(R.id.orderNo);
        orderAmount = findViewById(R.id.orderAmount);
        orderStatus = findViewById(R.id.orderStatus);
        orderDate = findViewById(R.id.orderDate);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userMobile = findViewById(R.id.userMobile);
        finalAmount = findViewById(R.id.finalAmount);
        pdLay = findViewById(R.id.pdLay);
        mainLay = findViewById(R.id.mainLay);
        lay3 = findViewById(R.id.lay3);
        productListRec = findViewById(R.id.productListRec);
        storeQR = findViewById(R.id.storeQR);

        getOrderDetail(order_id);

        storeQR.setOnClickListener(v -> {
            Intent i = new Intent(OrderDetailActivity.this, QRActivity.class);
            i.putExtra("orderId", orderID);
            startActivity(i);
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });
    }

    private void getOrderDetail(String o_id) {
        pdLay.setVisibility(View.VISIBLE);
        mainLay.setVisibility(View.GONE);
        lay3.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<StoreOrderDetailModel> responseModelCall = apiMaster.storeOrderDetail(o_id, share.getStoreId());
        responseModelCall.enqueue(new Callback<StoreOrderDetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<StoreOrderDetailModel> call, @NotNull Response<StoreOrderDetailModel> response) {
                StoreOrderDetailModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {

                    orderNo.setText(responseModel.getOrderId());
                    orderID = responseModel.getOrderId();

                    orderAmount.setText(responseModel.getFinalAmount());
                    finalAmount.setText(responseModel.getFinalAmount());

                    String oStatus = responseModel.getOrderStatus();
                    switch (oStatus) {
                        case "0":
                            orderStatus.setText("Order Pending");
                            orderStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                            storeQR.setEnabled(true);
                            o_status = responseModel.getOrderStatus();
                            break;
                        case "1":
                            orderStatus.setText("Order Complete");
                            orderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                            storeQR.setEnabled(false);
                            o_status = responseModel.getOrderStatus();
                            break;
                        case "2":
                            orderStatus.setText("Order Cancel");
                            orderStatus.setTextColor(getResources().getColor(R.color.red));
                            storeQR.setEnabled(false);
                            o_status = responseModel.getOrderStatus();
                            break;
                    }

                    orderDate.setText(responseModel.getOrderDate());
                    userName.setText(responseModel.getuFName() + " " + responseModel.getuLName());
                    userEmail.setText(responseModel.getuEmail());
                    userMobile.setText(responseModel.getuMobile());
                    uId = responseModel.getuId();

                    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(responseModel.getStoreOrderDetails(), OrderDetailActivity.this);
                    productListRec.setAdapter(orderDetailAdapter);
                    productListRec.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                    productListRec.setNestedScrollingEnabled(false);

                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.VISIBLE);
                    lay3.setVisibility(View.VISIBLE);
                } else {
                    Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg2));
                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.GONE);
                    lay3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<StoreOrderDetailModel> call, @NotNull Throwable t) {
                Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                mainLay.setVisibility(View.GONE);
                lay3.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getOrderDetail(order_id);
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
