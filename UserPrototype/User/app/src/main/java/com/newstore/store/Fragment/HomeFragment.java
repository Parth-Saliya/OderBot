package com.newstore.store.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newstore.store.Activity.EditProfileActivity;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.UserDetailModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private Share share;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private TextView totalOrder, pendingOrder, completeOrder, cancelOrder;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        share = new Share(requireActivity());
        CircleImageView userImage = view.findViewById(R.id.userImage);
        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);
        TextView userMobile = view.findViewById(R.id.userMobile);
        totalOrder = view.findViewById(R.id.totalOrder);
        pendingOrder = view.findViewById(R.id.pendingOrder);
        completeOrder = view.findViewById(R.id.completeOrder);
        cancelOrder = view.findViewById(R.id.cancelOrder);
        ImageView editProfile = view.findViewById(R.id.editProfile);

        String path = Url.IMAGE_URL + share.getUserImage();
        Glide.with(requireActivity()).load(path).into(userImage);
        userName.setText(share.getUserFName() + " " + share.getUserLName());
        userEmail.setText(share.getUserEmail());
        userMobile.setText(share.getUserMobile());

        storeDetail();

        editProfile.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), EditProfileActivity.class);
            startActivity(i);
        });

        return view;
    }

    private void storeDetail() {

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<UserDetailModel> responseModelCall = apiMaster.userDetail(share.getUserId());
        responseModelCall.enqueue(new Callback<UserDetailModel>() {
            @Override
            public void onResponse(@NotNull Call<UserDetailModel> call, @NotNull Response<UserDetailModel> response) {
                UserDetailModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {
                    totalOrder.setText(responseModel.getOrderTotal());
                    pendingOrder.setText(responseModel.getOrderPending());
                    completeOrder.setText(responseModel.getOrderComplete());
                    cancelOrder.setText(responseModel.getOrderCancel());
                } else {
                    Share.showToast(requireActivity(), getResources().getString(R.string.server_msg2));
                }

            }

            @Override
            public void onFailure(@NotNull Call<UserDetailModel> call, @NotNull Throwable t) {
                Share.showToast(requireActivity(), getResources().getString(R.string.server_msg));
            }
        });
    }
}
