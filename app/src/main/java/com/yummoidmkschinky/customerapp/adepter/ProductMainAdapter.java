package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.ProductDataItem;

import java.util.List;

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProductDataItem> mCatlist;
    int isStore;

    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public LinearLayout lvlItmeclik;
        public ImageView imgAddremov;
        public RecyclerView recyclerView;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.txt_title);
            lvlItmeclik =  view.findViewById(R.id.lvl_itmeclik);
            imgAddremov = view.findViewById(R.id.img_addremov);
            recyclerView = view.findViewById(R.id.recycler_product);

        }
    }

    public ProductMainAdapter(Context mContext, List<ProductDataItem> mCatlist,int isStore) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.isStore = isStore;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rest_product_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        setFadeAnimation(holder.itemView);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(mContext, 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(mLayoutManager1);
        holder.recyclerView.setAdapter(new ProductAdp(mContext, mCatlist.get(position).getMenuitemData(),null,isStore));
        holder.title.setText(""+mCatlist.get(position).getTitle());
        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {

                    holder.imgAddremov.setBackgroundResource(R.drawable.rights);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            0,
                            holder.recyclerView.getHeight());
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    holder.recyclerView.startAnimation(animate);
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            holder.recyclerView.getHeight(),
                            0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    holder.recyclerView.startAnimation(animate);

                    holder.imgAddremov.setBackgroundResource(R.drawable.ic_expand);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

}