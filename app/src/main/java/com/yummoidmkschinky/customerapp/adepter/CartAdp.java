package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.MenuitemDataItem;
import com.yummoidmkschinky.customerapp.utiles.Product;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdp extends RecyclerView.Adapter<CartAdp.MyViewHolder> {
    private Context mContext;
    private List<MenuitemDataItem> categoryList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onCartItem(String titel, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_pextra)
        TextView txtPextra;
        @BindView(R.id.lvl_cart)
        LinearLayout lvlCart;
        @BindView(R.id.txt_price)
        TextView txtPrice;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public CartAdp(Context mContext, List<MenuitemDataItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        MenuitemDataItem dataItem = categoryList.get(position);
        holder.txtTitle.setText("" + dataItem.getTitle());

        new Product().setJoinPlayrList(holder.lvlCart, dataItem, mContext);

        if (dataItem.getAddonTitel() != null) {
            holder.txtPextra.setText("" + dataItem.getAddonTitel());
            String[] separated = dataItem.getAddonPrice().split(",");
            int aprice = 0;
            for (String price : separated) {
                aprice = aprice + Integer.parseInt(price);
            }
            double tdata = dataItem.getPrice() + aprice;
            if (tdata == (long) tdata)
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + String.format("%d", (long) tdata));
            else
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + String.format("%s", tdata));

        } else {
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getPrice());

        }


        switch (dataItem.getIsVeg()) {
            case 0:
                holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nonveg, 0, 0, 0);
                break;
            case 1:
                holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                break;
            case 2:
                holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }
}