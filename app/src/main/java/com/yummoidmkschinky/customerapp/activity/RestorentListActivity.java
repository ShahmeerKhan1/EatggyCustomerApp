package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class RestorentListActivity extends AppCompatActivity implements RestaurantsAdp.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.main_content)
    androidx.coordinatorlayout.widget.CoordinatorLayout mainContent;
    @BindView(R.id.appbar)
    com.google.android.material.appbar.AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorent_list);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();


        txtTitle.setText("" + getIntent().getExtras().get("title"));


        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(mLayoutManager13);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getRetorantList();
    }

    private void getRetorantList() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("cid", getIntent().getExtras().get("cid"));
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCarData(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void onRestaurantsItem(String rid, int position) {

        startActivity(new Intent(this, RestaurantsActivity.class).putExtra("rid", rid));

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
                        RestaurantsAdp adapter3 = new RestaurantsAdp(this, home.getRestuarantData(), this);
                        myRecyclerView.setAdapter(adapter3);
                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        myRecyclerView.setVisibility(View.GONE);

                    }
                }


            }
        } catch (Exception e) {
            Log.e("error", "" + e.toString());
        }
    }

    @OnClick({R.id.img_back, R.id.txt_brestorent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_brestorent:
                finish();
                break;

            default:

                break;
        }
    }
}