package com.newstore.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.UserReviewModel;
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

public class ReviewActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    RatingBar qOne, qTwo;
    TextInputEditText Review;
    AppCompatButton submitBtn;
    ProgressBar pdLay;

    String order_id, sId, pId;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        order_id = getIntent().getStringExtra("order_id");
        sId = getIntent().getStringExtra("s_id");
        pId = getIntent().getStringExtra("p_id");

        share = new Share(this);
        back = findViewById(R.id.back);
        qOne = findViewById(R.id.qOne);
        qTwo = findViewById(R.id.qTwo);
        Review = findViewById(R.id.Review);
        submitBtn = findViewById(R.id.submitBtn);
        pdLay = findViewById(R.id.pdLay);

        submitBtn.setOnClickListener(v -> {
            float o = qOne.getRating();
            float t = qTwo.getRating();
            String q_one = String.valueOf(Math.round(o));
            String q_two = String.valueOf(Math.round(t));
            String review_txt = Objects.requireNonNull(Review.getText()).toString().trim();

            System.out.println("O One === " + q_one);
            System.out.println("O Two === " + q_two);
            System.out.println("Review === " + review_txt);
            System.out.println("oId === " + order_id);
            System.out.println("sId === " + sId);
            System.out.println("pId === " + pId);
            System.out.println("uId === " + share.getUserId());

            userReview(order_id, sId, pId, q_one, q_two, review_txt);
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

    }

    private void userReview(String o_id, String s_id, String p_id, String q_one, String q_two,
                            String reviewTxt) {
        pdLay.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<UserReviewModel> responseModelCall = apiMaster.userReview(o_id, s_id, share.getUserId(),
                p_id, q_one, q_two, reviewTxt);
        responseModelCall.enqueue(new Callback<UserReviewModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<UserReviewModel> call, @NotNull Response<UserReviewModel> response) {
                UserReviewModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                } else {
                    Share.showToast(ReviewActivity.this, getResources().getString(R.string.server_msg2));
                    pdLay.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserReviewModel> call, @NotNull Throwable t) {
                Share.showToast(ReviewActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
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
