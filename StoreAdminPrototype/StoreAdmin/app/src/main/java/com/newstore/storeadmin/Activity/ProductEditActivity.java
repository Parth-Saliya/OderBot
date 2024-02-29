package com.newstore.storeadmin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.ProductEditModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;
import com.newstore.storeadmin.Utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductEditActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    CircleImageView productImage;
    FloatingActionButton productImageSelect;
    TextInputEditText productName, productDetail, productQty, productAmount;
    ProgressBar pdLay;
    Button productAddBtn;

    private int REQUEST_CODE = 1;
    String pId = "";
    String pImage = "";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        share = new Share(this);
        back = findViewById(R.id.back);
        productImage = findViewById(R.id.productImage);
        productImageSelect = findViewById(R.id.productImageSelect);
        productName = findViewById(R.id.productName);
        productDetail = findViewById(R.id.productDetail);
        productQty = findViewById(R.id.productQty);
        productAmount = findViewById(R.id.productAmount);
        pdLay = findViewById(R.id.pdLay);
        productAddBtn = findViewById(R.id.productAddBtn);

        productName.setText(getIntent().getStringExtra("pName"));
        productDetail.setText(getIntent().getStringExtra("pDetail"));
        productQty.setText(getIntent().getStringExtra("pQty"));
        productAmount.setText(getIntent().getStringExtra("pAmount"));

        pId = getIntent().getStringExtra("pId");
        String path = Url.IMAGE_URL + getIntent().getStringExtra("pImage");
        Glide.with(this).load(path).into(productImage);

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

        productImageSelect.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Store Image"), REQUEST_CODE);
        });

        productAddBtn.setOnClickListener(v -> {
            String pName = Objects.requireNonNull(productName.getText()).toString().trim();
            String pDetail = Objects.requireNonNull(productDetail.getText()).toString().trim();
            String pQty = Objects.requireNonNull(productQty.getText()).toString().trim();
            String pAmount = Objects.requireNonNull(productAmount.getText()).toString().trim();

            if (pName.equals("")) {
                Share.showToast(ProductEditActivity.this, "Required Product name");
            } else if (pDetail.equals("")) {
                Share.showToast(ProductEditActivity.this, "Required Product Detail");
            } else if (pQty.equals("")) {
                Share.showToast(ProductEditActivity.this, "Required Product Quantity");
            } else if (pAmount.equals("")) {
                Share.showToast(ProductEditActivity.this, "Required Product Amount");
            } else {

                System.out.println("Product === " + pName);
                System.out.println("Product === " + pDetail);
                System.out.println("Product === " + pQty);
                System.out.println("Product === " + pAmount);
                System.out.println("Product === " + pImage);

                productEdit(pName, pDetail, pQty, pAmount, pImage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));

                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                productImage.setImageBitmap(scaled);
                pImage = Utils.getBase64ImageStringFromBitmap(scaled);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void productEdit(String pName, String pDetail, String pQty, String pAmount, String pImage) {
        pdLay.setVisibility(View.VISIBLE);
        productAddBtn.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<ProductEditModel> responseModelCall = apiMaster.storeProductEdit(pName, pDetail, pQty,
                pAmount, pImage, pId, share.getStoreId());
        responseModelCall.enqueue(new Callback<ProductEditModel>() {
            @Override
            public void onResponse(@NotNull Call<ProductEditModel> call, @NotNull Response<ProductEditModel> response) {
                ProductEditModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                } else {
                    Share.showToast(ProductEditActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
                productAddBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<ProductEditModel> call, @NotNull Throwable t) {
                Share.showToast(ProductEditActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                productAddBtn.setVisibility(View.VISIBLE);
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
