package com.newstore.storeadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.StoreLoginModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Share share;

    TextInputEditText storeEmail, storePassword;
    ProgressBar pdLay;
    AppCompatButton loginBtn;
    TextView signUp, forgotPassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        share = new Share(this);
        storeEmail = findViewById(R.id.storeEmail);
        storePassword = findViewById(R.id.storePassword);
        pdLay = findViewById(R.id.pdLay);
        loginBtn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        loginBtn.setOnClickListener(v -> {
            String sEmail = Objects.requireNonNull(storeEmail.getText()).toString().trim();
            String sPassword = Objects.requireNonNull(storePassword.getText()).toString().trim();

            if (sEmail.equals("")) {
                Share.showToast(LoginActivity.this, "Required Store Email");
            } else if (sPassword.equals("")) {
                Share.showToast(LoginActivity.this, "Required Password");
            } else if (!sEmail.matches(emailPattern)) {
                Share.showToast(LoginActivity.this, "Enter Valid Email");
            } else {

                System.out.println("SignUp === " + sEmail);
                System.out.println("SignUp === " + sPassword);

                login(sEmail, sPassword);
            }
        });

        signUp.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    private void login(String sEmail, String sPassword) {
        pdLay.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<StoreLoginModel> responseModelCall = apiMaster.storeLogin(sEmail, sPassword);
        responseModelCall.enqueue(new Callback<StoreLoginModel>() {
            @Override
            public void onResponse(@NotNull Call<StoreLoginModel> call, @NotNull Response<StoreLoginModel> response) {
                StoreLoginModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {
                    share.setStoreLoginValue(responseModel.getsId(), responseModel.getsName(),
                            responseModel.getsAddress(), responseModel.getsMobile(),
                            responseModel.getsEmail(), responseModel.getsImage());

                    if (responseModel.getsMapFlag().equals("0")) {
                        Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        share.setStoreLatLog(responseModel.getsLat(), responseModel.getsLog());
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Share.showToast(LoginActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<StoreLoginModel> call, @NotNull Throwable t) {
                Share.showToast(LoginActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            System.out.println("exit === " + e);
        }
    }
}
