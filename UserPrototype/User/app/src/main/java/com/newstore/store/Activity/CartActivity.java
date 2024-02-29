package com.newstore.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newstore.store.Adapter.CartListAdapter;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Database.MyDatabase;
import com.newstore.store.Model.CartListModel;
import com.newstore.store.Model.UserOrderModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {

    MyDatabase myDatabase;
    Share share;

    RelativeLayout back;
    AppCompatButton placeOrder;
    TextView finalAmount;

    TextView storeName, storeAddress, storeEmail, storeMobile;
    String sId, storeImage;

    ProgressBar pdLay, pdLay2;
    RecyclerView cartListRec;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    JSONObject jsonObject;
    JSONArray jsonArray;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        share = new Share(this);
        myDatabase = new MyDatabase(this);
        dialog = new Dialog(this);

        back = findViewById(R.id.back);
        placeOrder = findViewById(R.id.placeOrder);
        finalAmount = findViewById(R.id.finalAmount);
        pdLay = findViewById(R.id.pdLay);
        pdLay2 = findViewById(R.id.pdLay2);
        cartListRec = findViewById(R.id.cartListRec);
        storeName = findViewById(R.id.storeName);
        storeAddress = findViewById(R.id.storeAddress);
        storeEmail = findViewById(R.id.storeEmail);
        storeMobile = findViewById(R.id.storeMobile);

        storeName.setText(getIntent().getStringExtra("sName"));
        storeAddress.setText(getIntent().getStringExtra("storeAddress"));
        storeEmail.setText(getIntent().getStringExtra("storeEmail"));
        storeMobile.setText(getIntent().getStringExtra("storeMobile"));
        sId = getIntent().getStringExtra("sId");
        storeImage = getIntent().getStringExtra("storeImage");

        pdLay.setVisibility(View.VISIBLE);
        getFinalAmount();
        getCartList();

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

        placeOrder.setOnClickListener(v -> {
            String fAmount = finalAmount.getText().toString();
            getCartData();

            showOrderPopUp(sId, fAmount, jsonArray.toString());

        });
    }

    public void getCartList() {
        List<CartListModel> cartListModels = myDatabase.getCartList();
        if (cartListModels.size() > 0) {
            CartListAdapter cartListAdapter = new CartListAdapter(cartListModels, CartActivity.this);
            cartListRec.setAdapter(cartListAdapter);
            cartListRec.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        } else {
            CartListAdapter cartListAdapter = new CartListAdapter(cartListModels, CartActivity.this);
            cartListRec.setAdapter(cartListAdapter);
            cartListRec.setLayoutManager(new LinearLayoutManager(CartActivity.this));
            Share.showToast(CartActivity.this, "Cart Is Empty");
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
        pdLay.setVisibility(View.GONE);
    }

    public void getCartData() {
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();
        List<CartListModel> cartListModels = myDatabase.getCartList();
        for (int k = 0; k < cartListModels.size(); k++) {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("pId", cartListModels.get(k).getpId());
                jsonObject.put("pName", cartListModels.get(k).getpName());
                jsonObject.put("pAmount", cartListModels.get(k).getpAmount());
                jsonObject.put("pQty", cartListModels.get(k).getpQty());
                jsonObject.put("pImage", cartListModels.get(k).getpImage());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getFinalAmount() {
        myDatabase.getFinalAmount();
        finalAmount.setText(String.valueOf(myDatabase.finalAmount));
    }

    @SuppressLint("SetTextI18n")
    private void showOrderPopUp(String sId, String fAmount, String pDetail) {
        TextView finalAmount;
        AppCompatButton cancelBtn, orderBtn;

        dialog.setContentView(R.layout.dialog_order_confirm);
        finalAmount = dialog.findViewById(R.id.finalAmount);
        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        orderBtn = dialog.findViewById(R.id.orderBtn);

        finalAmount.setText("₹" + fAmount);

        orderBtn.setOnClickListener(v -> {
            System.out.println("uId === " + share.getUserId());
            System.out.println("sId === " + sId);
            System.out.println("fAmount === " + fAmount);
            System.out.println("pDetail === " + jsonArray.toString());

            orderNow(sId, fAmount, pDetail);
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void orderNow(String sId, String fAmount, String pDetail) {
        pdLay2.setVisibility(View.VISIBLE);
        placeOrder.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<UserOrderModel> responseModelCall = apiMaster.userOrder(share.getUserId(), sId, fAmount, pDetail);
        responseModelCall.enqueue(new Callback<UserOrderModel>() {
            @Override
            public void onResponse(@NotNull Call<UserOrderModel> call, @NotNull Response<UserOrderModel> response) {
                UserOrderModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    Intent i = new Intent(CartActivity.this, ThankYouActivity.class);
                    i.putExtra("orderId", responseModel.getOrderId());
                    i.putExtra("finalAmount", responseModel.getFinalAmount());
                    i.putExtra("orderDate", responseModel.getOrderDate());
                    i.putExtra("orderItem", responseModel.getTotalItem());
                    i.putExtra("orderStatus", responseModel.getOrderStatus());
                    i.putExtra("orderQty", responseModel.getTotalQty());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Share.showToast(CartActivity.this, getResources().getString(R.string.server_msg2));
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }

                pdLay2.setVisibility(View.GONE);
                placeOrder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<UserOrderModel> call, @NotNull Throwable t) {
                Share.showToast(CartActivity.this, getResources().getString(R.string.server_msg));
                pdLay2.setVisibility(View.GONE);
                placeOrder.setVisibility(View.VISIBLE);
            }
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
