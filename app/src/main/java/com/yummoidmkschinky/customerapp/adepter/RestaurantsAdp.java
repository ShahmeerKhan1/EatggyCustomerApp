package com.yummoidmkschinky.customerapp.adepter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.RestuarantDataItem;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;

public class RestaurantsAdp extends RecyclerView.Adapter<RestaurantsAdp.MyViewHolder> {
    private Context mContext;
    private List<RestuarantDataItem> categoryList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onRestaurantsItem(String rid, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
      public  ImageView imageView;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_sdesc)
        TextView txtSdesc;
        @BindView(R.id.txt_location)
        TextView txtLocation;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.txt_offers)
        TextView txtOffers;
        @BindView(R.id.txt_star)
        TextView txtStar;
        @BindView(R.id.lvl_offerdata)
        LinearLayout lvlOfferdata;
        @BindView(R.id.txt_offertitle)
        TextView txtOffertitle;
        @BindView(R.id.lvl_itmeclik)
        LinearLayout lvlItmeclik;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public RestaurantsAdp(Context mContext, List<RestuarantDataItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restorent, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        RestuarantDataItem item = categoryList.get(position);
        holder.txtTitle.setText(item.getRestTitle());
        holder.txtStar.setText(item.getRestRating());
        holder.txtSdesc.setText(item.getRestSdesc());

        holder.txtLocation.setText(item.getRestFullAddress() + " | " + item.getRestDistance());
        holder.txtTime.setText(item.getRestDeliverytime() + " mins");
        holder.txtPrice.setText(sessionManager.getStringData(currency) + " " + item.getRestCostfortwo() + " for two");


        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getRestImg()).thumbnail(Glide.with(mContext).load(R.drawable.animationbg)).into(holder.imageView);



        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onRestaurantsItem(item.getRestId(), position);
            }
        });
        if (item.getCouTitle() != null && !item.getCouTitle().isEmpty()) {
            holder.lvlOfferdata.setVisibility(View.VISIBLE);
            holder.txtOffertitle.setVisibility(View.VISIBLE);
            holder.txtOffers.setText("" + item.getCouSubtitle());
            holder.txtOffertitle.setText("" + item.getCouTitle());
        } else {
            holder.lvlOfferdata.setVisibility(View.GONE);
            holder.txtOffertitle.setVisibility(View.GONE);

        }
        if(item.getRestIsopen()==0){
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        createContrast(bitmap, 5,holder.imageView);

                    }catch (Exception e){

                    }

                }
            }, 800);

        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }
    public Bitmap createContrast(Bitmap src, double value,ImageView imageView) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.red(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.red(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        imageView.setImageBitmap(bmOut);
        return bmOut;
    }


}