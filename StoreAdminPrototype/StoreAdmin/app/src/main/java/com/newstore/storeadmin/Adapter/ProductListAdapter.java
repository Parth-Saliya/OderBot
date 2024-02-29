package com.newstore.storeadmin.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.newstore.storeadmin.Activity.ProductEditActivity;
import com.newstore.storeadmin.ApiMaster.ApiMaster;
import com.newstore.storeadmin.Fragment.ProductFragment;
import com.newstore.storeadmin.Model.ProductDeleteModel;
import com.newstore.storeadmin.Model.ProductListModel;
import com.newstore.storeadmin.R;
import com.newstore.storeadmin.Utils.Share;
import com.newstore.storeadmin.Utils.Url;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<ProductListModel.ProductList> productLists;
    private Activity context;
    private ProductFragment productFragment;

    private Share share;
    private Dialog dialog, dialog2;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public ProductListAdapter(List<ProductListModel.ProductList> productLists, Activity context, ProductFragment productFragment) {
        this.productLists = productLists;
        this.context = context;
        this.productFragment = productFragment;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_product_list, parent, false);
        share = new Share(context);
        dialog = new Dialog(context);
        dialog2 = new Dialog(context, R.style.FullscreenDialog);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        ProductListModel.ProductList mList = productLists.get(position);

        String path = Url.IMAGE_URL + mList.getProductImage();
        Glide.with(context).load(path).into(holder.productImage);

        holder.productName.setText(mList.getProductName());
        holder.productQty.setText(mList.getProductQty());
        holder.productAmount.setText("₹" + mList.getProductAmount());

        holder.viewDetail.setOnClickListener(v -> viewDetailPopUp(mList.getProductDetail()));

        holder.productDelete.setOnClickListener(v -> productDelete(holder.pdLay, mList.getProductId()));

        holder.productImage.setOnClickListener(v -> viewImagePopUp(path));

        holder.productEdit.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductEditActivity.class);
            i.putExtra("pName", mList.getProductName());
            i.putExtra("pDetail", mList.getProductDetail());
            i.putExtra("pQty", mList.getProductQty());
            i.putExtra("pAmount", mList.getProductAmount());
            i.putExtra("pImage", mList.getProductImage());
            i.putExtra("pId", mList.getProductId());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productQty, productAmount;
        TextView productDelete, productEdit, viewDetail;
        ProgressBar pdLay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productQty = itemView.findViewById(R.id.productQty);
            productAmount = itemView.findViewById(R.id.productAmount);
            productDelete = itemView.findViewById(R.id.productDelete);
            productEdit = itemView.findViewById(R.id.productEdit);
            viewDetail = itemView.findViewById(R.id.viewDetail);
            pdLay = itemView.findViewById(R.id.pdLay);
        }
    }

    public void updateList(List<ProductListModel.ProductList> list) {
        productLists = list;
        notifyDataSetChanged();
    }

    private void viewDetailPopUp(String pDetail) {
        TextView productDetail;
        AppCompatButton back;

        dialog.setContentView(R.layout.dialog_product_detail);
        productDetail = dialog.findViewById(R.id.productDetail);
        back = dialog.findViewById(R.id.back);

        productDetail.setText(pDetail);

        back.setOnClickListener(view -> dialog.dismiss());

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void viewImagePopUp(String imgUrl) {
        AppCompatButton back;
        final PhotoView imgShow;

        dialog2.setContentView(R.layout.dialog_product_image);
        back = dialog2.findViewById(R.id.back);
        imgShow = dialog2.findViewById(R.id.imgShow);

        Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imgShow.setImageResource(R.drawable.ic_image);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imgShow);

        back.setOnClickListener(view -> dialog2.dismiss());

        Objects.requireNonNull(dialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.show();
    }

    private void productDelete(ProgressBar pdLay, String pId) {
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
        Call<ProductDeleteModel> responseModelCall = apiMaster.storeProductDelete(share.getStoreId(), pId);
        responseModelCall.enqueue(new Callback<ProductDeleteModel>() {
            @Override
            public void onResponse(@NotNull Call<ProductDeleteModel> call, @NotNull Response<ProductDeleteModel> response) {
                ProductDeleteModel responseModel = response.body();

                assert responseModel != null;
                System.out.println("Status === " + responseModel.getStatus());

                if (responseModel.getStatus().equals("true")) {
                    productFragment.getProductList();
                } else {
                    Share.showToast(context, context.getResources().getString(R.string.server_msg2));
                }
                pdLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<ProductDeleteModel> call, @NotNull Throwable t) {
                Share.showToast(context, context.getResources().getString(R.string.server_msg));
                pdLay.setVisibility(View.GONE);
            }
        });
    }
}
