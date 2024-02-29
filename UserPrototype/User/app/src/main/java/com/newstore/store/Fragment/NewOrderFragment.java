package com.newstore.store.Fragment;

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

import com.newstore.store.Activity.MapsActivity;
import com.newstore.store.Adapter.StoreListAdapter;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.StoreListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Share;
import com.newstore.store.Utils.Url;

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

public class NewOrderFragment extends Fragment {

    private ProgressBar pdLay;
    private RecyclerView storeListRec;

    private StoreListModel responseModel;
    private StoreListAdapter storeListAdapter;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order, container, false);

        pdLay = view.findViewById(R.id.pdLay);
        storeListRec = view.findViewById(R.id.storeListRec);
        ImageView location = view.findViewById(R.id.location);
        EditText search = view.findViewById(R.id.search);

        getStoreList();

        location.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MapsActivity.class);
            requireActivity().startActivity(i);
        });

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

        return view;
    }

    private void filter(String text) {
        List<StoreListModel.StoreList> temp = new ArrayList<>();
        for (StoreListModel.StoreList d : responseModel.getStoreLists()) {
            if (d.getsName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        storeListAdapter.updateList(temp);
    }

    private void getStoreList() {
        pdLay.setVisibility(View.VISIBLE);
        storeListRec.setVisibility(View.GONE);

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
                    storeListAdapter = new StoreListAdapter(responseModel.getStoreLists(), requireActivity());
                    storeListRec.setAdapter(storeListAdapter);
                    storeListRec.setLayoutManager(new LinearLayoutManager(requireActivity()));
                } else {
                    Share.showToast(requireActivity(), getResources().getString(R.string.server_msg2));
                }
                pdLay.setVisibility(View.GONE);
                storeListRec.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<StoreListModel> call, @NotNull Throwable t) {
                Share.showToast(requireActivity(), getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                storeListRec.setVisibility(View.VISIBLE);
            }
        });
    }
}
