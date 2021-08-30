package com.yummoidmkschinky.customerapp.activity;

import android.os.Bundle;
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
import com.yummoidmkschinky.customerapp.adepter.ProductAdp;
import com.yummoidmkschinky.customerapp.model.SearchP;
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

public class SearchProductActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;


    String rID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);


        rID = getIntent().getStringExtra("rid");
        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(mLayoutManager13);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchList(edSearch.getText().toString());
                }


                return true;
            }
            return false;
        });

    }

    private void getSearchList(String keyword) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("keyword", keyword);
            jsonObject.put("rid", rID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSearchProduct(bodyRequest);
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
                SearchP searchP = gson.fromJson(result.toString(), SearchP.class);
                if (searchP.getResult().equalsIgnoreCase("true")) {
                    if (searchP.getProductSearchData().size() != 0) {
                        lvlNotfound.setVisibility(View.GONE);
                        myRecyclerView.setVisibility(View.VISIBLE);
                        myRecyclerView.setAdapter(new ProductAdp(this, searchP.getProductSearchData(), null, 1));

                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        myRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.toString();

        }
    }

    @OnClick({R.id.img_back, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchList(edSearch.getText().toString());
                }
                break;

            default:
                break;
        }
    }

}