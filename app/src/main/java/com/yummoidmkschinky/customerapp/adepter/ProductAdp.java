package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.MenuitemDataItem;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.utiles.Product;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;

public class ProductAdp extends RecyclerView.Adapter<ProductAdp.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<MenuitemDataItem> categoryList;
    private List<MenuitemDataItem> categoryListFiltered;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;
    int isStore;

    public interface RecyclerTouchListener {
        public void onProductItem(String titel, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_vegnonveg)
        ImageView txtVegnonveg;
        @BindView(R.id.txt_titele)
        TextView txtTitele;
        @BindView(R.id.txt_prize)
        TextView txtPrize;
        @BindView(R.id.txt_desc)
        TextView txtDesc;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.lvl_cart)
        LinearLayout lvlCart;
        @BindView(R.id.lvl_image)
        LinearLayout lvlImage;
        @BindView(R.id.rlt_image)
        RelativeLayout rltImage;
        @BindView(R.id.txt_custamize)
        TextView txtCustamize;
        @BindView(R.id.lvl_itmeclik)
        LinearLayout lvlItmeclik;

        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public ProductAdp(Context mContext, List<MenuitemDataItem> categoryList, final RecyclerTouchListener listener, int isStore) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
        this.isStore = isStore;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        MenuitemDataItem dataItem = categoryList.get(position);
        holder.txtTitele.setText(dataItem.getTitle());
        if (dataItem.getItemImg() != null && !dataItem.getItemImg().isEmpty()) {
            holder.lvlImage.setVisibility(View.VISIBLE);
            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) (120 * scale + 0.5f);
            holder.rltImage.getLayoutParams().height = pixels;
            Glide.with(mContext).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(mContext).load(R.drawable.animationbg)).into(holder.imageView);
        } else {
            holder.lvlImage.setVisibility(View.GONE);
            holder.rltImage.getLayoutParams().height = 120;

        }
        holder.txtPrize.setText(sessionManager.getStringData(currency) + dataItem.getPrice());
        holder.txtDesc.setText("" + dataItem.getCdesc());

        if (dataItem.getIsVeg() == 0) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (dataItem.getIsVeg() == 1) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_veg));

        } else if (dataItem.getIsVeg() == 2) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_egg));
        }

        if (dataItem.getIsCustomize() == 1) {
            holder.txtCustamize.setVisibility(View.VISIBLE);
        } else {
            holder.txtCustamize.setVisibility(View.GONE);

        }
        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataItem.getItemImg() != null && !dataItem.getItemImg().isEmpty() && isStore == 1) {
                    Product.bottonProductDetails(mContext, dataItem);
                }
            }
        });
        if (isStore == 1) {
            Product.setJoinPlayrList(holder.lvlCart, dataItem, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryListFiltered = categoryList;
                } else {
                    List<MenuitemDataItem> filteredList = new ArrayList<>();
                    for (MenuitemDataItem row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getIsVeg() == 1) {
                            filteredList.add(row);
                        }
                    }

                    categoryListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = categoryListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryListFiltered = (List<MenuitemDataItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}