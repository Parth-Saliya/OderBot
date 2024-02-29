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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.SignUpModel;
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

public class SignUpActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    CircleImageView storeImage;
    FloatingActionButton storeImageSelect;
    TextInputEditText storeName, storeAddress, storeMobile, storeEmail, storePassword;
    ProgressBar pdLay;
    AppCompatButton signUpBtn;

    private int REQUEST_CODE = 1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String sImage = "";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        share = new Share(this);

        back = findViewById(R.id.back);
        storeImage = findViewById(R.id.storeImage);
        storeImageSelect = findViewById(R.id.storeImageSelect);
        storeName = findViewById(R.id.storeName);
        storeAddress = findViewById(R.id.storeAddress);
        storeMobile = findViewById(R.id.storeMobile);
        storeEmail = findViewById(R.id.storeEmail);
        storePassword = findViewById(R.id.storePassword);
        pdLay = findViewById(R.id.pdLay);
        signUpBtn = findViewById(R.id.signUpBtn);

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
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
            String sEmail = Objects.requireNonNull(storeEmail.getText()).toString().trim();
            String sPassword = Objects.requireNonNull(storePassword.getText()).toString().trim();

            if (sName.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Store name");
            } else if (sAddress.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Store Address");
            } else if (sMobile.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Store Mobile");
            } else if (sEmail.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Store Email");
            } else if (sPassword.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Password");
            } else if (!sEmail.matches(emailPattern)) {
                Share.showToast(SignUpActivity.this, "Enter Valid Email");
            } else {

                System.out.println("SignUp === " + sName);
                System.out.println("SignUp === " + sAddress);
                System.out.println("SignUp === " + sMobile);
                System.out.println("SignUp === " + sEmail);
                System.out.println("SignUp === " + sPassword);
                System.out.println("SignUp === " + sImage);

                signUp(sName, sAddress, sMobile, sEmail, sPassword, sImage);
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

    private void signUp(String sName, String sAddress, String sMobile,
                        String sEmail, String sPassword, String sImage) {
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
        Call<SignUpModel> responseModelCall = apiMaster.signUp(sName, sAddress, sMobile, sEmail, sPassword, sImage);
        responseModelCall.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(@NotNull Call<SignUpModel> call, @NotNull Response<SignUpModel> response) {
                SignUpModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {
                    share.setStoreLoginValue(responseModel.getsId(), responseModel.getsName(),
                            responseModel.getsAddress(), responseModel.getsMobile(),
                            responseModel.getsEmail(), responseModel.getsImage());

                    if (responseModel.getsMapFlag().equals("0")) {
                        Intent i = new Intent(SignUpActivity.this, MapsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        share.setStoreLatLog(responseModel.getsLat(), responseModel.getsLog());
                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                } else {
                    Share.showToast(SignUpActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
                signUpBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpModel> call, @NotNull Throwable t) {
                Share.showToast(SignUpActivity.this, getResources().getString(R.string.server_msg));
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
