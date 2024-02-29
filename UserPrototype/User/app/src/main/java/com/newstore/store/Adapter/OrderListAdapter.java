package com.newstore.store.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newstore.store.Activity.OrderDetailActivity;
import com.newstore.store.Model.UserOrderListModel;
import com.newstore.store.R;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<UserOrderListModel.UserOrderList> userOrderLists;
    private Activity context;

    public OrderListAdapter(List<UserOrderListModel.UserOrderList> userOrderLists, Activity context) {
        this.userOrderLists = userOrderLists;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_order_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        UserOrderListModel.UserOrderList mList = userOrderLists.get(position);

        holder.orderNo.setText(mList.getOrderId());
        holder.orderAmount.setText("₹" + mList.getFinalAmount());
        holder.orderDate.setText(mList.getOrderDate());

        String oStatus = mList.getOrderStatus();
        switch (oStatus) {
            case "0":
                holder.orderStatus.setText("Order Pending");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case "1":
                holder.orderStatus.setText("Order Complete");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "2":
                holder.orderStatus.setText("Order Cancel");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }

        holder.orderDetails.setOnClickListener(v -> {
            Intent i = new Intent(context, OrderDetailActivity.class);
            i.putExtra("oId", mList.getoId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return userOrderLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderNo, orderAmount, orderDate, orderStatus, orderDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.orderNo);
            orderAmount = itemView.findViewById(R.id.orderAmount);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }

    public void updateList(List<UserOrderListModel.UserOrderList> list) {
        userOrderLists = list;
        notifyDataSetChanged();
    }
}
