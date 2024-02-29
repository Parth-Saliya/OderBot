package com.newstore.store.Fragment;

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
import android.widget.ProgressBar;

import com.newstore.store.Adapter.OrderListAdapter;
import com.newstore.store.ApiMaster.ApiMaster;
import com.newstore.store.Model.UserOrderListModel;
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

public class OrderFragment extends Fragment {

    private Share share;

    private ProgressBar pdLay;
    private RecyclerView orderListRec;

    private UserOrderListModel responseModel;
    private OrderListAdapter orderListAdapter;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        share = new Share(requireActivity());
        EditText search = view.findViewById(R.id.search);
        pdLay = view.findViewById(R.id.pdLay);
        orderListRec = view.findViewById(R.id.orderListRec);

        getOrderList();

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
        List<UserOrderListModel.UserOrderList> temp = new ArrayList<>();
        for (UserOrderListModel.UserOrderList d : responseModel.getUserOrderLists()) {
            if (d.getOrderId().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getOrderDate().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        orderListAdapter.updateList(temp);
    }

    private void getOrderList() {
        pdLay.setVisibility(View.VISIBLE);
        orderListRec.setVisibility(View.GONE);

        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiMaster apiMaster = retrofit.create(ApiMaster.class);
        Call<UserOrderListModel> responseModelCall = apiMaster.userOrderList(share.getUserId());
        responseModelCall.enqueue(new Callback<UserOrderListModel>() {
            @Override
            public void onResponse(@NotNull Call<UserOrderListModel> call, @NotNull Response<UserOrderListModel> response) {
                responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    orderListAdapter = new OrderListAdapter(responseModel.getUserOrderLists(), requireActivity());
                    orderListRec.setAdapter(orderListAdapter);
                    orderListRec.setLayoutManager(new LinearLayoutManager(requireActivity()));
                } else {
                    Share.showToast(requireActivity(), getResources().getString(R.string.server_msg2));
                }
                pdLay.setVisibility(View.GONE);
                orderListRec.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<UserOrderListModel> call, @NotNull Throwable t) {
                Share.showToast(requireActivity(), getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
                orderListRec.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }
}
