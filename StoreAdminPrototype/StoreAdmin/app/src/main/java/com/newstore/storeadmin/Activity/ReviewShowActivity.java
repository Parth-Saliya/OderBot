package com.newstore.storeadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.UserReviewShowModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewShowActivity extends AppCompatActivity {

    Share share;

    RelativeLayout back;
    RatingBar qOne, qTwo;
    TextView Review;
    ProgressBar pdLay;
    NestedScrollView mainLay;

    String order_id, uId, pId;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_show);

        order_id = getIntent().getStringExtra("order_id");
        uId = getIntent().getStringExtra("u_id");
        pId = getIntent().getStringExtra("p_id");

        share = new Share(this);
        back = findViewById(R.id.back);
        qOne = findViewById(R.id.qOne);
        qTwo = findViewById(R.id.qTwo);
        Review = findViewById(R.id.Review);
        pdLay = findViewById(R.id.pdLay);
        mainLay = findViewById(R.id.mainLay);

        userReviewShow(order_id, uId, pId);

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });
    }

    private void userReviewShow(String o_id, String u_id, String p_id) {
        pdLay.setVisibility(View.VISIBLE);
        mainLay.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<UserReviewShowModel> responseModelCall = apiMaster.userReviewShow(o_id, share.getStoreId(), u_id, p_id);
        responseModelCall.enqueue(new Callback<UserReviewShowModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<UserReviewShowModel> call, @NotNull Response<UserReviewShowModel> response) {
                UserReviewShowModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.VISIBLE);

                    qOne.setRating(Float.parseFloat(responseModel.getReviewQ1()));
                    qTwo.setRating(Float.parseFloat(responseModel.getReviewQ2()));
                    Review.setText(responseModel.getReviewText());

                } else {
                    Share.showToast(ReviewShowActivity.this, "No Review Yet!");
                    pdLay.setVisibility(View.GONE);
                    mainLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserReviewShowModel> call, @NotNull Throwable t) {
                Share.showToast(ReviewShowActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                mainLay.setVisibility(View.GONE);
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
