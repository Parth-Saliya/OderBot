package com.newstore.store.Activity;

import androidx.annotation.Nullable;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.newstore.store.Adapter.OrderDetailAdapter;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.QRModel;
import com.newstore.store.Model.UserOrderCancelModel;
import com.newstore.store.Model.UserOrderDetailModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

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
    TextView storeName, storeAddress, storeEmail, storeMobile, buyAgain, cancelOrder;
    public String oId, sID, sImage, sLat, sLog;
    public String order_id, orderID, o_status;
    TextView finalAmount;
    AppCompatButton scanOrder;

    ProgressBar pdLay;
    NestedScrollView mainLay;
    RelativeLayout lay3;

    RecyclerView productListRec;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    IntentIntegrator dqrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = getIntent().getStringExtra("oId");

        share = new Share(this);
        dqrScan = new IntentIntegrator(this);
        back = findViewById(R.id.back);
        orderNo = findViewById(R.id.orderNo);
        orderAmount = findViewById(R.id.orderAmount);
        orderStatus = findViewById(R.id.orderStatus);
        orderDate = findViewById(R.id.orderDate);
        storeName = findViewById(R.id.storeName);
        storeAddress = findViewById(R.id.storeAddress);
        storeEmail = findViewById(R.id.storeEmail);
        storeMobile = findViewById(R.id.storeMobile);
        buyAgain = findViewById(R.id.buyAgain);
        cancelOrder = findViewById(R.id.cancelOrder);
        finalAmount = findViewById(R.id.finalAmount);
        scanOrder = findViewById(R.id.scanOrder);
        pdLay = findViewById(R.id.pdLay);
        mainLay = findViewById(R.id.mainLay);
        lay3 = findViewById(R.id.lay3);
        productListRec = findViewById(R.id.productListRec);


        getOrderDetail(order_id);

        scanOrder.setOnClickListener(v -> dqrScan.initiateScan());

        cancelOrder.setOnClickListener(v -> userCancelOrder(order_id));

        buyAgain.setOnClickListener(v -> {
            Intent i = new Intent(OrderDetailActivity.this, ProductActivity.class);
            i.putExtra("sLat", sLat);
            i.putExtra("sLog", sLog);
            startActivity(i);
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Share.showToast(OrderDetailActivity.this, "Result Not Found");
            } else {
                String mainString = orderID + sID;

                if (result.getContents().equals(mainString)) {
                    QRScan(order_id);
                    System.out.println("Main String === " + result.getContents());
                } else {
                    Share.showToast(OrderDetailActivity.this, "Order Not match");
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        Call<UserOrderDetailModel> responseModelCall = apiMaster.userOrderDetail(o_id, share.getUserId());
        responseModelCall.enqueue(new Callback<UserOrderDetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<UserOrderDetailModel> call, @NotNull Response<UserOrderDetailModel> response) {
                UserOrderDetailModel responseModel = response.body();

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
                            scanOrder.setEnabled(true);
                            cancelOrder.setVisibility(View.VISIBLE);
                            o_status = responseModel.getOrderStatus();
                            break;
                        case "1":
                            orderStatus.setText("Order Complete");
                            orderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                            scanOrder.setEnabled(false);
                            cancelOrder.setVisibility(View.GONE);
                            o_status = responseModel.getOrderStatus();
                            break;
                        case "2":
                            orderStatus.setText("Order Cancel");
                            orderStatus.setTextColor(getResources().getColor(R.color.red));
                            scanOrder.setEnabled(false);
                            cancelOrder.setVisibility(View.GONE);
                            o_status = responseModel.getOrderStatus();
                            break;
                    }

                    orderDate.setText(responseModel.getOrderDate());
                    storeName.setText(responseModel.getsName());
                    storeAddress.setText(responseModel.getsAddress());
                    storeEmail.setText(responseModel.getsEmail());
                    storeMobile.setText(responseModel.getsMobile());

                    oId = responseModel.getoId();
                    sID = responseModel.getsId();
                    sImage = responseModel.getsImage();
                    sLat = responseModel.getsLat();
                    sLog = responseModel.getsLog();

                    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(responseModel.getUserOrderDetails(), OrderDetailActivity.this);
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
            public void onFailure(@NotNull Call<UserOrderDetailModel> call, @NotNull Throwable t) {
                Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                mainLay.setVisibility(View.GONE);
                lay3.setVisibility(View.GONE);
            }
        });
    }

    private void QRScan(String o_id) {
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
        Call<QRModel> responseModelCall = apiMaster.userScan(o_id, share.getUserId());
        responseModelCall.enqueue(new Callback<QRModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<QRModel> call, @NotNull Response<QRModel> response) {
                QRModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {

                    getOrderDetail(order_id);

                } else {
                    Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg2));
                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.GONE);
                    lay3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QRModel> call, @NotNull Throwable t) {
                Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                mainLay.setVisibility(View.GONE);
                lay3.setVisibility(View.GONE);
            }
        });
    }

    private void userCancelOrder(String o_id) {
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
        Call<UserOrderCancelModel> responseModelCall = apiMaster.userOrderCancel(o_id, share.getUserId());
        responseModelCall.enqueue(new Callback<UserOrderCancelModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<UserOrderCancelModel> call, @NotNull Response<UserOrderCancelModel> response) {
                UserOrderCancelModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {

                    getOrderDetail(order_id);

                } else {
                    Share.showToast(OrderDetailActivity.this, getResources().getString(R.string.server_msg2));
                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.GONE);
                    lay3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserOrderCancelModel> call, @NotNull Throwable t) {
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
