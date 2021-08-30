package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.activity.RatesActivity;
import com.yummoidmkschinky.customerapp.map.OrderTrackerActivity;
import com.yummoidmkschinky.customerapp.model.OrderHistoryItem;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdp extends RecyclerView.Adapter<OrderAdp.MyViewHolder> {
    private Context mContext;
    private List<OrderHistoryItem> itemList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onOrderItem(String oid);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_rtitle)
        TextView txtRtitle;
        @BindView(R.id.txt_delivery)
        TextView txtDelivery;
        @BindView(R.id.txt_location)
        TextView txtLocation;
        @BindView(R.id.txt_totoal)
        TextView txtTotoal;
        @BindView(R.id.txt_ptitle)
        TextView txtPtitle;
        @BindView(R.id.txt_dates)
        TextView txtDates;
        @BindView(R.id.txt_trackorder)
        TextView txtTrackorder;
        @BindView(R.id.txt_orderrates)
        TextView txtOrderrates;
        @BindView(R.id.lvl_itemclick)
        LinearLayout lvlItemclick;
        @BindView(R.id.txt_dtitle)
        TextView txtDtitle;
        @BindView(R.id.txt_drates)
        TextView txtDrates;
        @BindView(R.id.txt_rrtitle)
        TextView txtRrtitle;
        @BindView(R.id.txt_rrates)
        TextView txtRrates;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public OrderAdp(Context mContext, List<OrderHistoryItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.itemList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        OrderHistoryItem item = itemList.get(position);
        holder.txtRtitle.setText("" + item.getRestName());
        holder.txtLocation.setText("" + item.getRestLandmark());
        holder.txtTotoal.setText(sessionManager.getStringData(SessionManager.currency) + item.getOrderTotal());
        holder.txtPtitle.setText("" + item.getOrderItems());
        holder.txtDates.setText("" + item.getOrderCompleteDate());
        if (item.getOStatus().equalsIgnoreCase("Pending") || item.getOStatus().equalsIgnoreCase("Processing") || item.getOStatus().equalsIgnoreCase("On Route")) {
            holder.txtTrackorder.setVisibility(View.VISIBLE);
            holder.txtDelivery.setVisibility(View.GONE);
            holder.txtOrderrates.setVisibility(View.GONE);
        } else {
            holder.txtTrackorder.setVisibility(View.GONE);
            holder.txtDelivery.setVisibility(View.VISIBLE);


            holder.txtDtitle.setVisibility(View.VISIBLE);
            holder.txtRrtitle.setVisibility(View.VISIBLE);

            if (item.getRiderRate() == 0) {
                holder.txtOrderrates.setVisibility(View.VISIBLE);

                holder.txtDtitle.setText("You haven't rated this delivery yet");

            } else {
                holder.txtDrates.setVisibility(View.VISIBLE);
                holder.txtDrates.setText("" + item.getRiderRate() + "    |    " + item.getRiderText());
                holder.txtDtitle.setText("Your Ratting for Delivery");
            }
            if (item.getRestRate() == 0) {
                holder.txtRrates.setVisibility(View.GONE);
                holder.txtRrtitle.setText("You haven't rated this food yet");

            } else {
                holder.txtRrates.setVisibility(View.VISIBLE);
                holder.txtRrates.setText("" + item.getRestRate() + "    |    " + item.getRestText());
                holder.txtRrtitle.setText("Your Food Ratting");
            }
        }

        holder.lvlItemclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOrderItem(item.getOrderId());

            }
        });
        holder.txtTrackorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, OrderTrackerActivity.class).putExtra("oid", item.getOrderId()));

            }
        });
        holder.txtOrderrates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, RatesActivity.class).putExtra("oid", item.getOrderId()));

            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();

    }

}