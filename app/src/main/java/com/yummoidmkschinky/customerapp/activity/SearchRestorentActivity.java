package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class SearchRestorentActivity extends AppCompatActivity implements GetResult.MyListener, RestaurantsAdp.RecyclerTouchListener {


    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    MyAddress myAddress;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restorent);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(SearchRestorentActivity.this);
        custPrograssbar=new CustPrograssbar();
        myAddress=sessionManager.getAddress();
        user=sessionManager.getUserDetails("");
        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(mLayoutManager13);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchRestorent(edSearch.getText().toString());
                }
                return true;
            }
            return false;
        });

    }

    private void getSearchRestorent(String keyword) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid",user.getId());
            jsonObject.put("keyword", keyword);
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSearchRestorent(bodyRequest);
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
                if (home.getRestuarantData().size() != 0) {
                    lvlNotfound.setVisibility(View.GONE);
                    myRecyclerView.setVisibility(View.VISIBLE);
                    RestaurantsAdp adapter3 = new RestaurantsAdp(this, home.getRestuarantData(), this);
                    myRecyclerView.setAdapter(adapter3);

                } else {
                    lvlNotfound.setVisibility(View.VISIBLE);
                    myRecyclerView.setVisibility(View.GONE);
                }



            }
        } catch (Exception e) {
            Log.e("error", "" + e.toString());
        }
    }

    @Override
    public void onRestaurantsItem(String rid, int position) {

        startActivity(new Intent(this, RestaurantsActivity.class).putExtra("rid", rid));

    }
    @OnClick({R.id.img_back, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchRestorent(edSearch.getText().toString());

                }
                break;

            default:
                break;
        }
    }
}