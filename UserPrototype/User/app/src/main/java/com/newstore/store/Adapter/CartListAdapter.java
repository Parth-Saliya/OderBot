package com.newstore.store.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newstore.store.Activity.CartActivity;
import com.newstore.store.Database.MyDatabase;
import com.newstore.store.Model.CartListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Url;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CartListModel> cartListModels;
    private Activity context;
    private MyDatabase myDatabase;

    public CartListAdapter(List<CartListModel> cartListModels, Activity context) {
        this.cartListModels = cartListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_cart_list, parent, false);
        myDatabase = new MyDatabase(context);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartListModel mList = cartListModels.get(position);

        String path = Url.IMAGE_URL + mList.getpImage();
        Glide.with(context).load(path).into(holder.productImage);

        holder.productName.setText(mList.getpName());
        holder.qtyPrice.setText(mList.getpTQty() + "/" + mList.getpAmount());
        holder.itemQty.setText(mList.getpQty());

        double tPrice = Double.parseDouble(mList.getpQty()) * Double.parseDouble(mList.getpAmount());
        holder.productAmount.setText(String.valueOf(tPrice));

        holder.plusBtn.setOnClickListener(view -> {
            int a = Integer.parseInt(mList.getpTQty());
            int r = Integer.parseInt(holder.itemQty.getText().toString());

            if (r == a) {
                holder.plusBtn.setEnabled(false);
                holder.minusBtn.setEnabled(true);
            } else {
                holder.plusBtn.setEnabled(true);
                holder.minusBtn.setEnabled(true);
                r++;
                holder.itemQty.setText(String.valueOf(r));

                myDatabase.cartInsertOrUpdate(mList.getpId(), mList.getpName(),
                        mList.getpAmount(), String.valueOf(r), mList.getpTQty(), mList.getpImage());

                ((CartActivity) context).getCartList();
                ((CartActivity) context).getFinalAmount();
            }

        });

        holder.minusBtn.setOnClickListener(view -> {
            int r = Integer.parseInt(holder.itemQty.getText().toString());

            if (r > 0) {
                holder.minusBtn.setEnabled(true);
                r--;
                holder.itemQty.setText(Integer.toString(r));

                myDatabase.cartInsertOrUpdate(mList.getpId(), mList.getpName(),
                        mList.getpAmount(), String.valueOf(r), mList.getpTQty(), mList.getpImage());
                ((CartActivity) context).getCartList();
                ((CartActivity) context).getFinalAmount();

                if (r == 0) {
                    holder.minusBtn.setEnabled(false);
                    holder.plusBtn.setEnabled(true);

                    myDatabase.carItemRemove(mList.getpId());
                    ((CartActivity) context).getCartList();
                    ((CartActivity) context).getFinalAmount();
                }

            } else {
                holder.minusBtn.setEnabled(false);
                holder.plusBtn.setEnabled(true);

                myDatabase.carItemRemove(mList.getpId());
                ((CartActivity) context).getCartList();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartListModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, qtyPrice, productAmount;
        ImageView minusBtn, plusBtn;
        TextView itemQty;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            qtyPrice = itemView.findViewById(R.id.qtyPrice);
            productAmount = itemView.findViewById(R.id.productAmount);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            itemQty = itemView.findViewById(R.id.itemQty);
        }
    }
}
