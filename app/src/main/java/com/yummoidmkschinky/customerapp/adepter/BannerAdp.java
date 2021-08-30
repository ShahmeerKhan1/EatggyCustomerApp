package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.BannerItem;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;

import java.util.List;

public class BannerAdp extends RecyclerView.Adapter<BannerAdp.MyViewHolder> {
    private Context mContext;
    private List<BannerItem> itemList;
    private RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        public void onBannerItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.imageView);
        }
    }

    public BannerAdp(Context mContext, List<BannerItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.itemList = categoryList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        BannerItem item = itemList.get(position);
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getImg()).thumbnail(Glide.with(mContext).load(R.drawable.animationbg)).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();

    }
}