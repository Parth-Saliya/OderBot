package com.newstore.store.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.StoreListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.GPSTracker;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ImageView myLocation;
    RelativeLayout back;
    ProgressBar pdLay;
    SupportMapFragment mapFragment;
    FloatingActionButton storeLocation;

    private StoreListModel responseModel;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    double sLat = 0, sLog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        myLocation = findViewById(R.id.myLocation);
        back = findViewById(R.id.back);
        pdLay = findViewById(R.id.pdLay);
        storeLocation = findViewById(R.id.storeLocation);

        myLocation.setOnClickListener(v -> getMyLocation());

        storeLocation.setOnClickListener(v -> {

            if (sLat == 0) {
                Share.showToast(MapsActivity.this, "Select Store Marker First");
            } else {
                Intent i = new Intent(MapsActivity.this, ProductActivity.class);
                i.putExtra("sLat", String.valueOf(sLat));
                i.putExtra("sLog", String.valueOf(sLog));
                startActivity(i);
            }

        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getStoreList();

        mMap.setOnMarkerClickListener(marker -> {
            System.out.println("Marker Id === " + marker.getId());
            System.out.println("Marker Id === " + marker.getPosition().latitude);
            System.out.println("Marker Id === " + marker.getPosition().longitude);

            marker.showInfoWindow();

            sLat = marker.getPosition().latitude;
            sLog = marker.getPosition().longitude;

            return false;
        });

        getMyLocation();


    }


    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        GPSTracker gpsTracker = new GPSTracker();
        assert location != null;
        gpsTracker.onLocationChanged(location);

        LatLng myLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14.0f));
    }

    private void getStoreList() {
        pdLay.setVisibility(View.VISIBLE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<StoreListModel> responseModelCall = apiMaster.storeList();
        responseModelCall.enqueue(new Callback<StoreListModel>() {
            @Override
            public void onResponse(@NotNull Call<StoreListModel> call, @NotNull Response<StoreListModel> response) {
                responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    for (int i = 0; i < responseModel.getStoreLists().size(); i++) {
                        LatLng storeOne = new LatLng(Double.parseDouble(responseModel.getStoreLists().get(i).getsLat()),
                                Double.parseDouble(responseModel.getStoreLists().get(i).getsLog()));
                        mMap.addMarker(new MarkerOptions()
                                .position(storeOne)
                                .title(responseModel.getStoreLists().get(i).getsName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                } else {
                    Share.showToast(MapsActivity.this, getResources().getString(R.string.server_msg2));
                }
                pdLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<StoreListModel> call, @NotNull Throwable t) {
                Share.showToast(MapsActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
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
