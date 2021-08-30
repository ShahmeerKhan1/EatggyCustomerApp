package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.adepter.RestaurantsAdp;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.RetorentList;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OffersActivity extends AppCompatActivity implements GetResult.MyListener, RestaurantsAdp.RecyclerTouchListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        custPrograssbar = new CustPrograssbar();
        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(mLayoutManager13);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());


        getOffersList();
    }

    private void getOffersList() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOffers(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RetorentList home = gson.fromJson(result.toString(), RetorentList.class);
                if (home.getResult().equalsIgnoreCase("true")) {
                    if (home.getRestuarantData().size() != 0) {
                        lvlNotfound.setVisibility(View.GONE);
                        myRecyclerView.setVisibility(View.VISIBLE);
                        RestaurantsAdp adapter3 = new RestaurantsAdp(this, home.getRestuarantData(), this);
                        myRecyclerView.setAdapter(adapter3);
                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        myRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    lvlNotfound.setVisibility(View.VISIBLE);
                    myRecyclerView.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            e.toString();

        }
    }

    @Override
    public void onRestaurantsItem(String rid, int position) {

        startActivity(new Intent(this, RestaurantsActivity.class).putExtra("rid", rid));

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}