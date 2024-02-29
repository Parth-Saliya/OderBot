package com.newstore.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.UserLoginModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

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

    TextInputEditText userEmail, userPassword;
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
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        pdLay = findViewById(R.id.pdLay);
        loginBtn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        loginBtn.setOnClickListener(v -> {
            String uEmail = Objects.requireNonNull(userEmail.getText()).toString().trim();
            String uPassword = Objects.requireNonNull(userPassword.getText()).toString().trim();

            if (uEmail.equals("")) {
                Share.showToast(LoginActivity.this, "Required User Email");
            } else if (uPassword.equals("")) {
                Share.showToast(LoginActivity.this, "Required Password");
            } else if (!uEmail.matches(emailPattern)) {
                Share.showToast(LoginActivity.this, "Enter Valid Email");
            } else {

                System.out.println("SignUp === " + uEmail);
                System.out.println("SignUp === " + uPassword);

                login(uEmail, uPassword);
            }
        });

        signUp.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    private void login(String uEmail, String uPassword) {
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
        Call<UserLoginModel> responseModelCall = apiMaster.userLogin(uEmail, uPassword);
        responseModelCall.enqueue(new Callback<UserLoginModel>() {
            @Override
            public void onResponse(@NotNull Call<UserLoginModel> call, @NotNull Response<UserLoginModel> response) {
                UserLoginModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    share.setStoreLoginValue(responseModel.getuId(), responseModel.getuFName(),
                            responseModel.getuLName(), responseModel.getuMobile(),
                            responseModel.getuEmail(), responseModel.getuImage());

                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else {
                    Share.showToast(LoginActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<UserLoginModel> call, @NotNull Throwable t) {
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
