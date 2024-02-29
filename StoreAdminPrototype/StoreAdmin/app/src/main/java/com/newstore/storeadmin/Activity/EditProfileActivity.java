package com.newstore.storeadmin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.StoreEditModel;
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

public class EditProfileActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    CircleImageView storeImage;
    FloatingActionButton storeImageSelect;
    TextInputEditText storeName, storeAddress, storeMobile, storeEmail;
    ProgressBar pdLay;
    AppCompatButton signUpBtn;
    TextView changeLocation;

    private int REQUEST_CODE = 1;
    String sImage = "";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        share = new Share(this);

        back = findViewById(R.id.back);
        storeImage = findViewById(R.id.storeImage);
        storeImageSelect = findViewById(R.id.storeImageSelect);
        storeName = findViewById(R.id.storeName);
        storeAddress = findViewById(R.id.storeAddress);
        storeMobile = findViewById(R.id.storeMobile);
        storeEmail = findViewById(R.id.storeEmail);
        pdLay = findViewById(R.id.pdLay);
        signUpBtn = findViewById(R.id.signUpBtn);
        changeLocation = findViewById(R.id.changeLocation);

        storeName.setText(share.getStoreName());
        storeAddress.setText(share.getStoreAddress());
        storeMobile.setText(share.getStoreMobile());
        storeEmail.setText(share.getStoreEmail());

        String path = Url.IMAGE_URL + share.getStoreImage();
        Glide.with(this).load(path).into(storeImage);

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

        changeLocation.setOnClickListener(v -> {
            Intent i = new Intent(EditProfileActivity.this, MapsActivity.class);
            startActivity(i);
        });

        storeImageSelect.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Store Image"), REQUEST_CODE);
        });

        signUpBtn.setOnClickListener(v -> {
            String sName = Objects.requireNonNull(storeName.getText()).toString().trim();
            String sAddress = Objects.requireNonNull(storeAddress.getText()).toString().trim();
            String sMobile = Objects.requireNonNull(storeMobile.getText()).toString().trim();

            if (sName.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required Store name");
            } else if (sAddress.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required Store Address");
            } else if (sMobile.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required Store Mobile");
            } else {

                System.out.println("SignUp === " + sName);
                System.out.println("SignUp === " + sAddress);
                System.out.println("SignUp === " + sMobile);
                System.out.println("SignUp === " + sImage);

                editProfile(sName, sAddress, sMobile, sImage);
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

                storeImage.setImageBitmap(scaled);
                sImage = Utils.getBase64ImageStringFromBitmap(scaled);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void editProfile(String sName, String sAddress, String sMobile, String sImage) {
        pdLay.setVisibility(View.VISIBLE);
        signUpBtn.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<StoreEditModel> responseModelCall = apiMaster.storeEditProfile(sName, sAddress, sMobile,
                share.getStoreLat(), share.getStoreLog(), sImage, share.getStoreId());
        responseModelCall.enqueue(new Callback<StoreEditModel>() {
            @Override
            public void onResponse(@NotNull Call<StoreEditModel> call, @NotNull Response<StoreEditModel> response) {
                StoreEditModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    share.setStoreLoginValue(responseModel.getsId(), responseModel.getsName(),
                            responseModel.getsAddress(), responseModel.getsMobile(),
                            responseModel.getsEmail(), responseModel.getsImage());

                    share.setStoreLatLog(responseModel.getsLat(), responseModel.getsLog());

                    Intent i = new Intent(EditProfileActivity.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else {
                    Share.showToast(EditProfileActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<StoreEditModel> call, @NotNull Throwable t) {
                Share.showToast(EditProfileActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.VISIBLE);
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
