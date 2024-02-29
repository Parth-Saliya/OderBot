package com.newstore.storeadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.newstore.storeadmin.Activity.EditProfileActivity;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.StoreDetailModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private Share share;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private TextView totalOrder, pendingOrder, completeOrder, cancelOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        share = new Share(requireActivity());
        CircleImageView storeImage = view.findViewById(R.id.storeImage);
        TextView storeName = view.findViewById(R.id.storeName);
        TextView storeEmail = view.findViewById(R.id.storeEmail);
        TextView storeMobile = view.findViewById(R.id.storeMobile);
        TextView storeAddress = view.findViewById(R.id.storeAddress);
        totalOrder = view.findViewById(R.id.totalOrder);
        pendingOrder = view.findViewById(R.id.pendingOrder);
        completeOrder = view.findViewById(R.id.completeOrder);
        cancelOrder = view.findViewById(R.id.cancelOrder);
        ImageView editProfile = view.findViewById(R.id.editProfile);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        String path = Url.IMAGE_URL + share.getStoreImage();
        Glide.with(requireActivity()).load(path).into(storeImage);
        storeName.setText(share.getStoreName());
        storeEmail.setText(share.getStoreEmail());
        storeMobile.setText(share.getStoreMobile());
        storeAddress.setText(share.getStoreAddress());

        storeDetail();

        editProfile.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), EditProfileActivity.class);
            startActivity(i);
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng myLocation = new LatLng(Double.parseDouble(share.getStoreLat()), Double.parseDouble(share.getStoreLog()));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title(share.getStoreName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
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
        Call<StoreDetailModel> responseModelCall = apiMaster.storeDetail(share.getStoreId());
        responseModelCall.enqueue(new Callback<StoreDetailModel>() {
            @Override
            public void onResponse(@NotNull Call<StoreDetailModel> call, @NotNull Response<StoreDetailModel> response) {
                StoreDetailModel responseModel = response.body();

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
            public void onFailure(@NotNull Call<StoreDetailModel> call, @NotNull Throwable t) {
                Share.showToast(requireActivity(), getResources().getString(R.string.server_msg));
            }
        });
    }
}
