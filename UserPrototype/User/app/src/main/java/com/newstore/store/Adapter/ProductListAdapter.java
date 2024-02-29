package com.newstore.store.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.newstore.store.Activity.ProductActivity;
import com.newstore.store.Database.MyDatabase;
import com.newstore.store.Model.ProductListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Url;

import java.util.List;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<ProductListModel.ProductList> productLists;
    private Activity context;
    private MyDatabase myDatabase;
    private Dialog dialog, dialog2;

    public ProductListAdapter(List<ProductListModel.ProductList> productLists, Activity context) {
        this.productLists = productLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_product_list, parent, false);
        myDatabase = new MyDatabase(context);
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
        holder.qtyPrice.setText(mList.getProductQty() + "/" + mList.getProductAmount());

        holder.viewDetail.setOnClickListener(v -> viewDetailPopUp(mList.getProductDetail()));

        holder.productImage.setOnClickListener(v -> viewImagePopUp(path));

        holder.plusBtn.setOnClickListener(view -> {
            int a = Integer.parseInt(mList.getProductQty());
            int r = Integer.parseInt(holder.itemQty.getText().toString());

            if (r == a) {
                holder.plusBtn.setEnabled(false);
                holder.minusBtn.setEnabled(true);
            } else {
                holder.plusBtn.setEnabled(true);
                holder.minusBtn.setEnabled(true);
                r++;
                holder.itemQty.setText(String.valueOf(r));

                myDatabase.cartInsertOrUpdate(mList.getProductId(), mList.getProductName(),
                        mList.getProductAmount(), String.valueOf(r), mList.getProductQty(), mList.getProductImage());
                ((ProductActivity) context).getFinalAmount();
            }

        });

        holder.minusBtn.setOnClickListener(view -> {
            int r = Integer.parseInt(holder.itemQty.getText().toString());

            if (r > 0) {
                holder.minusBtn.setEnabled(true);
                r--;
                holder.itemQty.setText(Integer.toString(r));

                myDatabase.cartInsertOrUpdate(mList.getProductId(), mList.getProductName(),
                        mList.getProductAmount(), String.valueOf(r), mList.getProductQty(), mList.getProductImage());
                ((ProductActivity) context).getFinalAmount();

                if (r == 0) {
                    holder.minusBtn.setEnabled(false);
                    holder.plusBtn.setEnabled(true);

                    myDatabase.carItemRemove(mList.getProductId());
                    ((ProductActivity) context).getFinalAmount();
                }

            } else {
                holder.minusBtn.setEnabled(false);
                holder.plusBtn.setEnabled(true);

                myDatabase.carItemRemove(mList.getProductId());
                ((ProductActivity) context).getFinalAmount();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, qtyPrice,viewDetail;
        ImageView minusBtn, plusBtn;
        TextView itemQty;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            qtyPrice = itemView.findViewById(R.id.qtyPrice);
            viewDetail = itemView.findViewById(R.id.viewDetail);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            itemQty = itemView.findViewById(R.id.itemQty);
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
}
