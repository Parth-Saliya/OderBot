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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.UserEditModel;
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

public class EditProfileActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    CircleImageView userImage;
    FloatingActionButton userImageSelect;
    TextInputEditText firstName, lastName, userMobile, userEmail;
    ProgressBar pdLay;
    AppCompatButton signUpBtn;

    private int REQUEST_CODE = 1;
    String uImage = "";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        share = new Share(this);

        back = findViewById(R.id.back);
        userImage = findViewById(R.id.userImage);
        userImageSelect = findViewById(R.id.userImageSelect);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userMobile = findViewById(R.id.userMobile);
        userEmail = findViewById(R.id.userEmail);
        pdLay = findViewById(R.id.pdLay);
        signUpBtn = findViewById(R.id.signUpBtn);

        firstName.setText(share.getUserFName());
        lastName.setText(share.getUserLName());
        userMobile.setText(share.getUserMobile());
        userMobile.setText(share.getUserMobile());
        userEmail.setText(share.getUserEmail());

        String path = Url.IMAGE_URL + share.getUserImage();
        Glide.with(this).load(path).into(userImage);

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

            if (uFName.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required User name");
            } else if (uLName.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required User Address");
            } else if (uMobile.equals("")) {
                Share.showToast(EditProfileActivity.this, "Required User Mobile");
            } else {

                System.out.println("Edit === " + uFName);
                System.out.println("Edit === " + uLName);
                System.out.println("Edit === " + uMobile);
                System.out.println("Edit === " + uImage);

                editProfile(uFName, uLName, uMobile, uImage);
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

    private void editProfile(String uFName, String uLName, String uMobile, String uImage) {
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
        Call<UserEditModel> responseModelCall = apiMaster.userEditProfile(uFName, uLName, uMobile, uImage, share.getUserId());
        responseModelCall.enqueue(new Callback<UserEditModel>() {
            @Override
            public void onResponse(@NotNull Call<UserEditModel> call, @NotNull Response<UserEditModel> response) {
                UserEditModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    share.setStoreLoginValue(responseModel.getuId(), responseModel.getuFName(),
                            responseModel.getuLName(), responseModel.getuMobile(),
                            responseModel.getuEmail(), responseModel.getuImage());

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
            public void onFailure(@NotNull Call<UserEditModel> call, @NotNull Throwable t) {
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
