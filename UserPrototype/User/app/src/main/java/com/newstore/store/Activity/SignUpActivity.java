package com.newstore.store.Activity;

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
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.SignUpModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;
import com.newstore.store.Utils.Utils;

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
    CircleImageView userImage;
    FloatingActionButton userImageSelect;
    TextInputEditText firstName, lastName, userMobile, userEmail, userPassword;
    ProgressBar pdLay;
    AppCompatButton signUpBtn;

    private int REQUEST_CODE = 1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String uImage = "";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        share = new Share(this);

        back = findViewById(R.id.back);
        userImage = findViewById(R.id.userImage);
        userImageSelect = findViewById(R.id.userImageSelect);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userMobile = findViewById(R.id.userMobile);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        pdLay = findViewById(R.id.pdLay);
        signUpBtn = findViewById(R.id.signUpBtn);

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

        userImageSelect.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Store Image"), REQUEST_CODE);
        });

        signUpBtn.setOnClickListener(v -> {
            String uFName = Objects.requireNonNull(firstName.getText()).toString().trim();
            String uLName = Objects.requireNonNull(lastName.getText()).toString().trim();
            String uMobile = Objects.requireNonNull(userMobile.getText()).toString().trim();
            String uEmail = Objects.requireNonNull(userEmail.getText()).toString().trim();
            String uPassword = Objects.requireNonNull(userPassword.getText()).toString().trim();

            if (uFName.equals("")) {
                Share.showToast(SignUpActivity.this, "Required User name");
            } else if (uLName.equals("")) {
                Share.showToast(SignUpActivity.this, "Required User Address");
            } else if (uMobile.equals("")) {
                Share.showToast(SignUpActivity.this, "Required User Mobile");
            } else if (uEmail.equals("")) {
                Share.showToast(SignUpActivity.this, "Required User Email");
            } else if (uPassword.equals("")) {
                Share.showToast(SignUpActivity.this, "Required Password");
            } else if (!uEmail.matches(emailPattern)) {
                Share.showToast(SignUpActivity.this, "Enter Valid Email");
            } else {

                System.out.println("SignUp === " + uFName);
                System.out.println("SignUp === " + uLName);
                System.out.println("SignUp === " + uMobile);
                System.out.println("SignUp === " + uEmail);
                System.out.println("SignUp === " + uPassword);
                System.out.println("SignUp === " + uImage);

                signUp(uFName, uLName, uMobile, uEmail, uPassword, uImage);
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

                userImage.setImageBitmap(scaled);
                uImage = Utils.getBase64ImageStringFromBitmap(scaled);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void signUp(String uFName, String uLName, String uMobile,
                        String uEmail, String uPassword, String uImage) {
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
        Call<SignUpModel> responseModelCall = apiMaster.signUp(uFName, uLName, uMobile, uEmail, uPassword, uImage);
        responseModelCall.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(@NotNull Call<SignUpModel> call, @NotNull Response<SignUpModel> response) {
                SignUpModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    share.setStoreLoginValue(responseModel.getuId(), responseModel.getuFName(),
                            responseModel.getuLName(), responseModel.getuMobile(),
                            responseModel.getuEmail(), responseModel.getuImage());

                    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

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
