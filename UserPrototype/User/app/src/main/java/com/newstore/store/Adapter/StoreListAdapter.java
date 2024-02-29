package com.newstore.store.Adapter;

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
import com.newstore.store.Activity.ProductActivity;
import com.newstore.store.Model.StoreListModel;
import com.newstore.store.R;
import com.newstore.store.Utils.Url;

import java.util.List;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {

    private List<StoreListModel.StoreList> storeLists;
    private Activity context;

    public StoreListAdapter(List<StoreListModel.StoreList> storeLists, Activity context) {
        this.storeLists = storeLists;
        this.context = context;
    }

    @NonNull
    @Override
    public StoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desk_store_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListAdapter.ViewHolder holder, int position) {
        StoreListModel.StoreList mList = storeLists.get(position);

        String path = Url.IMAGE_URL + mList.getsImage();
        Glide.with(context).load(path).into(holder.storeImage);

        holder.storeName.setText(mList.getsName());
        holder.storeAddress.setText(mList.getsAddress());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductActivity.class);
            i.putExtra("sLat", mList.getsLat());
            i.putExtra("sLog", mList.getsLog());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return storeLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView storeImage;
        TextView storeName, storeAddress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeImage = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);
        }
    }

    public void updateList(List<StoreListModel.StoreList> list) {
        storeLists = list;
        notifyDataSetChanged();
    }
}
