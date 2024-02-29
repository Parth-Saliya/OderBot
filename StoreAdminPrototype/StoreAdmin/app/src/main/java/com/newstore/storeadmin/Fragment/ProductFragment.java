package com.newstore.storeadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.newstore.storeadmin.Activity.ProductAddActivity;
import com.newstore.storeadmin.Adapter.ProductListAdapter;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Model.ProductListModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductFragment extends Fragment {

    private Share share;
    private RecyclerView productListRec;
    private ProgressBar pdLay;

    private ProductListModel responseModel;
    private ProductListAdapter productListAdapter;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        share = new Share(requireActivity());
        ImageView productAdd = view.findViewById(R.id.productAdd);
        productListRec = view.findViewById(R.id.productListRec);
        pdLay = view.findViewById(R.id.pdLay);
        EditText search = view.findViewById(R.id.search);

        getProductList();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        productAdd.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), ProductAddActivity.class);
            startActivity(i);
        });

        return view;
    }

    private void filter(String text) {
        List<ProductListModel.ProductList> temp = new ArrayList<>();
        for (ProductListModel.ProductList d : responseModel.getProductLists()) {
            if (d.getProductName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        productListAdapter.updateList(temp);
    }

    public void getProductList() {
        pdLay.setVisibility(View.VISIBLE);
        productListRec.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<ProductListModel> responseModelCall = apiMaster.storeProductList(share.getStoreId());
        responseModelCall.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(@NotNull Call<ProductListModel> call, @NotNull Response<ProductListModel> response) {
                responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    productListAdapter = new ProductListAdapter(responseModel.getProductLists(),
                            requireActivity(), ProductFragment.this);
                    productListRec.setAdapter(productListAdapter);
                    productListRec.setLayoutManager(new LinearLayoutManager(requireActivity()));
                } else {
                    Share.showToast(requireActivity(), getResources().getString(R.string.server_msg2));
                }
                pdLay.setVisibility(View.GONE);
                productListRec.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<ProductListModel> call, @NotNull Throwable t) {
                Share.showToast(requireActivity(), getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                productListRec.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductList();
    }
}
