package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.CouponItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponAdp extends RecyclerView.Adapter<CouponAdp.MyViewHolder> {
    private Context mContext;
    private List<CouponItem> categoryList;
    private RecyclerTouchListener listener;
    private int amount;

    public interface RecyclerTouchListener {
        public void onCouponItem(String titel, CouponItem couponItem);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.txt_desc)
        TextView txtDesc;

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txt_title)
        TextView txtTitle;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CouponAdp(Context mContext, List<CouponItem> categoryList, final RecyclerTouchListener listener, int amount) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
        this.amount = amount;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CouponItem item = categoryList.get(position);
        holder.txtTitle.setText(item.getCouponTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtDesc.setText(Html.fromHtml(item.getCDesc(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.txtDesc.setText(Html.fromHtml(item.getCDesc()));
        }
//        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getCatimg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                listener.onClickItem(category.getCatname(), Integer.parseInt(category.getId()));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }
}