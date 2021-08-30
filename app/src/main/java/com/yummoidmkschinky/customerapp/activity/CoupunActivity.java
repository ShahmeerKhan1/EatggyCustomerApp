package com.yummoidmkschinky.customerapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.adepter.CouponAdpOne;
import com.yummoidmkschinky.customerapp.model.Coupon;
import com.yummoidmkschinky.customerapp.model.CouponItem;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.coupon;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.couponid;


public class CoupunActivity extends AppCompatActivity implements GetResult.MyListener, CouponAdpOne.RecyclerTouchListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    User user;
    SessionManager sessionManager;
    int amount = 0;
    CustPrograssbar custPrograssbar;
    String rid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupun);
        ButterKnife.bind(this);
        amount = getIntent().getIntExtra("amount", 0);
        rid=getIntent().getStringExtra("rid");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(CoupunActivity.this);
        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getCoupuns();

    }

    private void getCoupuns() {
        custPrograssbar.prograssCreate(CoupunActivity.this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rid",rid);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getCouponList(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void chalkCoupons(String cid) {
        try {
            custPrograssbar.prograssCreate(CoupunActivity.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            jsonObject.put("cid", cid);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().checkCoupon(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Coupon coupon = gson.fromJson(result.toString(), Coupon.class);
                if (coupon.getResult().equalsIgnoreCase("true")) {
                    CouponAdpOne couponAdp = new CouponAdpOne(this, coupon.getCouponlist(), this, amount);
                    recyclerView.setAdapter(couponAdp);
                }else {
                    Toast.makeText(CoupunActivity.this,coupon.getResponseMsg(),Toast.LENGTH_LONG).show();
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(CoupunActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equalsIgnoreCase("true")) {

                    finish();
                } else {
                    sessionManager.setIntData(coupon, 0);
                }
            }
        } catch (Exception e) {
            sessionManager.setIntData(coupon, 0);
        }

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }



    @Override
    public void onClickItem(View v, CouponItem coupons) {
        try {
            if (coupons.getMinAmt() < amount) {
                Log.e("vvvv",""+ Integer.parseInt(coupons.getCValue()));
                sessionManager.setIntData(coupon, Integer.parseInt(coupons.getCValue()));
                sessionManager.setIntData(couponid, Integer.parseInt(coupons.getId()));
                chalkCoupons(coupons.getId());
            } else {
                Toast.makeText(CoupunActivity.this, "Sorry this coupon code is not applied", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Error ","-->"+e.toString());
            e.printStackTrace();

        }
    }
}