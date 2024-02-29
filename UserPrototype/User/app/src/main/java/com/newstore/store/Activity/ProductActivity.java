package com.newstore.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newstore.store.Adapter.ProductListAdapter;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Database.MyDatabase;
import com.newstore.store.Model.ProductListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {

    MyDatabase myDatabase;

    EditText search;
    RelativeLayout back;
    AppCompatButton viewCart;
    ProgressBar pdLay;
    RecyclerView productListRec;
    RelativeLayout lay2, lay3;
    TextView finalAmount;

    TextView StoreName;
    public String sId, sName, storeAddress, storeEmail, storeMobile, storeImage;
    String sLat = "", sLog = "";

    private ProductListModel responseModel;
    private ProductListAdapter productListAdapter;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sLat = getIntent().getStringExtra("sLat");
        sLog = getIntent().getStringExtra("sLog");

        System.out.println("Lat === " + getIntent().getStringExtra("sLat"));
        System.out.println("Log === " + getIntent().getStringExtra("sLog"));

        myDatabase = new MyDatabase(this);
        StoreName = findViewById(R.id.StoreName);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
        viewCart = findViewById(R.id.viewCart);
        pdLay = findViewById(R.id.pdLay);
        productListRec = findViewById(R.id.productListRec);
        lay2 = findViewById(R.id.lay2);
        lay3 = findViewById(R.id.lay3);
        finalAmount = findViewById(R.id.finalAmount);

        myDatabase.cartDelete();
        getFinalAmount();
        getProductList(sLat, sLog);

        viewCart.setOnClickListener(v -> {
            Intent i = new Intent(ProductActivity.this, CartActivity.class);
            i.putExtra("sId",sId);
            i.putExtra("sName",sName);
            i.putExtra("storeAddress",storeAddress);
            i.putExtra("storeEmail",storeEmail);
            i.putExtra("storeMobile",storeMobile);
            i.putExtra("storeImage",storeImage);
            startActivity(i);
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

    }

    private void filter(String text) {
        List<ProductListModel.ProductList> temp = new ArrayList<>();
        for (ProductListModel.ProductList d : responseModel.getProductLists()) {
            if (d.getProductName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        productListAdapter.updateList(temp);
    }

    private void getProductList(String s_lat, String s_log) {
        pdLay.setVisibility(View.VISIBLE);
        productListRec.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<ProductListModel> responseModelCall = apiMaster.storeProductList(s_lat, s_log);
        responseModelCall.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(@NotNull Call<ProductListModel> call, @NotNull Response<ProductListModel> response) {
                responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {

                    StoreName.setText(responseModel.getsName());
                    sId = responseModel.getsId();
                    sName = responseModel.getsName();
                    storeAddress = responseModel.getsAddress();
                    storeEmail = responseModel.getsEmail();
                    storeMobile = responseModel.getsMobile();
                    storeImage = responseModel.getsImage();

                    productListAdapter = new ProductListAdapter(responseModel.getProductLists(), ProductActivity.this);
                    productListRec.setAdapter(productListAdapter);
                    productListRec.setLayoutManager(new LinearLayoutManager(ProductActivity.this));

                    lay2.setVisibility(View.VISIBLE);
                    lay3.setVisibility(View.VISIBLE);
                } else {
                    Share.showToast(ProductActivity.this, getResources().getString(R.string.server_msg2));
                    lay2.setVisibility(View.GONE);
                    lay3.setVisibility(View.GONE);
                }
                pdLay.setVisibility(View.GONE);
                productListRec.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<ProductListModel> call, @NotNull Throwable t) {
                Share.showToast(ProductActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                productListRec.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getFinalAmount() {
        myDatabase.getFinalAmount();
        finalAmount.setText(String.valueOf(myDatabase.finalAmount));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        myDatabase.cartDelete();
        getFinalAmount();
        getProductList(sLat, sLog);
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
