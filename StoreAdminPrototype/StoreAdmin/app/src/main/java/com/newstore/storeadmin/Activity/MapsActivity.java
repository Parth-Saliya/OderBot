package com.newstore.storeadmin.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.StoreLatLangModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.GPSTracker;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ImageView myLocation;
    FloatingActionButton storeLocation;
    RelativeLayout back;
    ProgressBar pdLay;

    double sLat, sLog;
    Share share;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        myLocation = findViewById(R.id.myLocation);
        storeLocation = findViewById(R.id.storeLocation);
        back = findViewById(R.id.back);
        pdLay = findViewById(R.id.pdLay);
        share = new Share(this);

        myLocation.setOnClickListener(v -> getMyLocation());

        storeLocation.setOnClickListener(v -> storeLatLng(Double.toString(sLat), Double.toString(sLog)));

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

        getMyLocation();

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng));
            sLat = latLng.latitude;
            sLog = latLng.longitude;
        });
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


    private void storeLatLng(String s_Lat, String s_Lng) {
        pdLay.setVisibility(View.VISIBLE);
        System.out.println("Location === " + sLat + " " + sLog + " " + share.getStoreId());

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<StoreLatLangModel> responseModelCall = apiMaster.storeLatLang(s_Lat, s_Lng, share.getStoreId());
        responseModelCall.enqueue(new Callback<StoreLatLangModel>() {
            @Override
            public void onResponse(@NotNull Call<StoreLatLangModel> call, @NotNull Response<StoreLatLangModel> response) {
                StoreLatLangModel responseModel = response.body();

                assert responseModel != null;
                if (responseModel.getStatus().equals("true")) {

                    share.setStoreLatLog(responseModel.getsLat(), responseModel.getsLog());
                    Intent i = new Intent(MapsActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Share.showToast(MapsActivity.this, getResources().getString(R.string.server_msg2));
                }

                pdLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<StoreLatLangModel> call, @NotNull Throwable t) {
                Share.showToast(MapsActivity.this, getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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
