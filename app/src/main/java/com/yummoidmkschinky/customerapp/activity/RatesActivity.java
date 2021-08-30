package com.yummoidmkschinky.customerapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.OrderItemsItem;
import com.yummoidmkschinky.customerapp.model.Rate;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;
import static com.yummoidmkschinky.customerapp.utiles.Utility.ratesupdat;

public class RatesActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_storename)
    TextView txtStorename;
    @BindView(R.id.txt_ridername)
    TextView txtRidername;
    @BindView(R.id.txt_countinu)
    TextView txtCountinu;
    @BindView(R.id.txt_submit)
    TextView txtSubmit;
    @BindView(R.id.recy_rats)
    RecyclerView recyRats;
    @BindView(R.id.lvl_itmelist)
    LinearLayout lvlItmelist;
    @BindView(R.id.img_top2)
    ImageView imgTop2;
    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.txt_ratetitle)
    TextView txtRatetitle;
    @BindView(R.id.txt_ratetitile2)
    TextView txtRatetitile2;
    List<Modeldata> restList = new ArrayList<>();

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    String oid;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(RatesActivity.this);
        user = sessionManager.getUserDetails("");
        oid = getIntent().getStringExtra("oid");
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyRats.setLayoutManager(mLayoutManager1);

        String[] separated = "VERY BAD,BAD,AVERAGE,GOOD,LOVED IT".split(",");
        Integer[] ints = {R.drawable.ic_emo_star_one,
                R.drawable.ic_emo_star_two
                , R.drawable.ic_emo_star_three
                , R.drawable.ic_emo_star_four
                , R.drawable.ic_emo_star_five};
        for (int i = 0; i < 5; i++) {
            Modeldata modeldata = new Modeldata();
            modeldata.setIsselect(false);
            int rt = i + 1;
            modeldata.setRates("" + rt);
            modeldata.setTitle(separated[i]);
            modeldata.setDrawable(ints[i]);
            restList.add(modeldata);
        }
        recyRats.setAdapter(new RatesAdp( restList));
        txtRatetitle.setText("How was the restaurants food");
        txtRatetitile2.setText("Your rating and reviews go a long way in helping people decide where to eat");
        getRestorentWithorder();
    }

    private void getRestorentWithorder() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getRestorentRest(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }


    private void setNotiList(LinearLayout lnrView, List<OrderItemsItem> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(RatesActivity.this);

            View view = inflater.inflate(R.layout.item_orderitem, null);

            TextView txtTitel = view.findViewById(R.id.txt_title);
            TextView txtPextra = view.findViewById(R.id.txt_pextra);
            TextView txtPrice = view.findViewById(R.id.txt_price);

            switch (list.get(i).getIsVeg()) {
                case "0":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nonveg, 0, 0, 0);
                    break;
                case "1":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                    break;
                case "2":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + list.get(i).getIsVeg());
            }
            txtTitel.setText("" + list.get(i).getItemName());
            txtPextra.setText("" + list.get(i).getItemAddon());
            txtPrice.setText("" + sessionManager.getStringData(currency) + list.get(i).getItemTotal());
            lnrView.addView(view);

        }

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Rate rate = gson.fromJson(result.toString(), Rate.class);
                if (rate.getResult().equalsIgnoreCase("true")) {
                    setNotiList(lvlItmelist, rate.getRatedata().getOrderItems());
                    txtTime.setText("" + rate.getRatedata().getOrderCompleteDate());
                    txtStorename.setText("" + rate.getRatedata().getRestName());
                    txtRidername.setText("" + rate.getRatedata().getRiderName());

                    Glide.with(this).load(APIClient.baseUrl + "/" + rate.getRatedata().getRestImage()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgTop);
                    Glide.with(this).load(APIClient.baseUrl + "/" + rate.getRatedata().getRiderImage()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgTop2);


                }

            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    ratesupdat = true;
                    Toast.makeText(RatesActivity.this,"Thank you for your review.",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        } catch (Exception e) {
            e.toString();

        }
    }

    public class Modeldata {
        String title;
        boolean isselect;
        String rates;
        int drawable;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isIsselect() {
            return isselect;
        }

        public void setIsselect(boolean isselect) {
            this.isselect = isselect;
        }

        public String getRates() {
            return rates;
        }

        public void setRates(String rates) {
            this.rates = rates;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }
    }

    int pposition = 0;

    public class RatesAdp extends RecyclerView.Adapter<RatesAdp.MyViewHolder> {

        List<Modeldata> list;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ImageView imgIcon;
            public LinearLayout lvlBg;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.txt_title);
                lvlBg = view.findViewById(R.id.lvl_bg);
                imgIcon = view.findViewById(R.id.img_icon);
            }
        }

        public RatesAdp( List<Modeldata> list) {

            this.list = list;


        }

        @Override
        public RatesAdp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rates, parent, false);
            return new RatesAdp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RatesAdp.MyViewHolder holder, int position) {

            Modeldata item = list.get(position);
            holder.title.setText("" + item.getTitle());
            holder.imgIcon.setImageResource(item.getDrawable());
            if (item.isIsselect()) {
                holder.lvlBg.setBackground(getDrawable(R.drawable.r_circle));
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);

                holder.lvlBg.setBackground(getDrawable(R.drawable.r_circle1));

            }
            holder.lvlBg.setOnClickListener(v -> {
                if (txtStorename.getVisibility() == View.VISIBLE) {
                    txtCountinu.setVisibility(View.VISIBLE);

                } else {
                    txtSubmit.setVisibility(View.VISIBLE);
                }
                if (!item.isIsselect()) {

                    Modeldata tips = list.get(pposition);
                    tips.setIsselect(false);
                    list.set(pposition, tips);
                    pposition = position;
                    item.setIsselect(true);
                    list.set(position, item);
                    notifyDataSetChanged();
                }


            });
        }

        @Override
        public int getItemCount() {
            return list.size();

        }
    }


    Modeldata restorent;

    @OnClick({R.id.txt_submit, R.id.txt_countinu, R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_submit:

                custPrograssbar.prograssCreate(this);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid", user.getId());
                    jsonObject.put("orderid", oid);
                    jsonObject.put("rest_rate", restorent.getRates());
                    jsonObject.put("rest_text", restorent.getTitle());
                    jsonObject.put("rider_rate", restList.get(pposition).getRates());
                    jsonObject.put("rider_text", restList.get(pposition).getTitle());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = APIClient.getInterface().getSendRates(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(this);
                getResult.callForLogin(call, "2");


                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_countinu:
                txtRatetitle.setText("How's the experience with delivery boy");
                txtRatetitile2.setText("Your review helps delivery boy to do more faster service");
                restorent = restList.get(pposition);

                Modeldata item = restorent;
                item.setIsselect(false);
                restList.set(pposition, item);
                recyRats.setAdapter(new RatesAdp( restList));

                txtCountinu.setVisibility(View.GONE);
                imgTop.setVisibility(View.GONE);
                txtStorename.setVisibility(View.GONE);

                txtRidername.setVisibility(View.VISIBLE);
                imgTop2.setVisibility(View.VISIBLE);

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}