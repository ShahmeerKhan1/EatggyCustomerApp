package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;

import java.util.List;

public class CustmazationAdp extends RecyclerView.Adapter<CustmazationAdp.MyViewHolder> {
    private Context mContext;
    private List<String> categoryList;
    private RecyclerTouchListener listener;
    RadioGroup rg;
    public interface RecyclerTouchListener {
        public void onCustmazationItem(String titel, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);

        }
    }

    public CustmazationAdp(Context mContext, List<String> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
        rg = new RadioGroup(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custmazation, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final RadioButton[] rb = new RadioButton[10];
        rb[position]  = new RadioButton(mContext);
        rb[position].setText(" Test");
        rb[position].setId(position + 100);
        rg.addView(rb[position]);
//        itemView.addView(rg);
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
//        return categoryList.size();
        return 10;
    }
}