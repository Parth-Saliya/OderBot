package com.newstore.store.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newstore.store.Activity.OrderDetailActivity;
import com.newstore.store.Activity.ReviewActivity;
import com.newstore.store.Activity.ReviewShowActivity;
import com.newstore.store.Model.UserOrderDetailModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Url;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<UserOrderDetailModel.UserOrderDetail> userOrderDetails;
    private Activity context;

    public OrderDetailAdapter(List<UserOrderDetailModel.UserOrderDetail> userOrderDetails, Activity context) {
        this.userOrderDetails = userOrderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_order_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        UserOrderDetailModel.UserOrderDetail mList = userOrderDetails.get(position);

        String path = Url.IMAGE_URL + mList.getpImage();
        Glide.with(context).load(path).into(holder.productImage);

        holder.productName.setText(mList.getpName());
        holder.qtyPrice.setText(mList.getpQty() + "/" + mList.getpAmount());

        String amount = String.valueOf((Double.parseDouble(mList.getpQty()) * Double.parseDouble(mList.getpAmount())));
        holder.productTotalAmount.setText("₹" + amount);

        if (((OrderDetailActivity) context).o_status.equals("1")) {
            holder.orderReview.setVisibility(View.VISIBLE);
        } else {
            holder.orderReview.setVisibility(View.GONE);
        }

        holder.orderReview.setOnClickListener(v -> {
            if (mList.getpReview().equals("1")) {
                Intent i = new Intent(context, ReviewShowActivity.class);
                i.putExtra("order_id", ((OrderDetailActivity) context).order_id);
                i.putExtra("s_id", ((OrderDetailActivity) context).sID);
                i.putExtra("p_id", mList.getpId());
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, ReviewActivity.class);
                i.putExtra("order_id", ((OrderDetailActivity) context).order_id);
                i.putExtra("s_id", ((OrderDetailActivity) context).sID);
                i.putExtra("p_id", mList.getpId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userOrderDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, qtyPrice, productTotalAmount, orderReview;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            qtyPrice = itemView.findViewById(R.id.qtyPrice);
            productTotalAmount = itemView.findViewById(R.id.productTotalAmount);
            orderReview = itemView.findViewById(R.id.orderReview);
        }
    }
}
