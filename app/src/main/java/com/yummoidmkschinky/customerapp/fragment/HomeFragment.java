package com.yummoidmkschinky.customerapp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.activity.DeliveryLocationActivity;
import com.yummoidmkschinky.customerapp.activity.HomeActivity;
import com.yummoidmkschinky.customerapp.activity.OffersActivity;
import com.yummoidmkschinky.customerapp.activity.RestaurantsActivity;
import com.yummoidmkschinky.customerapp.activity.RestorentListActivity;
import com.yummoidmkschinky.customerapp.adepter.BannerAdp;
import com.yummoidmkschinky.customerapp.adepter.CategoryAdp;
import com.yummoidmkschinky.customerapp.adepter.RestaurantsAdp;
import com.yummoidmkschinky.customerapp.model.Home;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.Utility.changeAddress;


public class HomeFragment extends Fragment implements CategoryAdp.RecyclerTouchListener, BannerAdp.RecyclerTouchListener, RestaurantsAdp.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;

    @BindView(R.id.recycler_banner)
    RecyclerView recyclerBanner;

    @BindView(R.id.recycler_restorent)
    RecyclerView recyclerRestorent;

    @BindView(R.id.txt_address)
    TextView txtAddress;

    @BindView(R.id.txt_type)
    TextView txtType;

    @BindView(R.id.img_footer)
    ImageView imgFooter;

    SessionManager sessionManager;
    MyAddress myAddress;
    CustPrograssbar custPrograssbar;
    User user;
    RestaurantsAdp restaurantsAdp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerBanner.setLayoutManager(mLayoutManager1);
        recyclerBanner.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager12 = new LinearLayoutManager(getActivity());
        mLayoutManager12.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCategory.setLayoutManager(mLayoutManager12);
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(getActivity());
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerRestorent.setLayoutManager(mLayoutManager13);

        recyclerRestorent.setItemAnimator(new DefaultItemAnimator());

        myAddress = sessionManager.getAddress();
        if (myAddress.getType() != null) {
            txtType.setVisibility(View.VISIBLE);
            txtType.setText("" + myAddress.getType());
        } else {
            txtType.setVisibility(View.GONE);
        }
        txtAddress.setText("" + myAddress.getAddress());
        getHome();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerRestorent.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    RestaurantsAdp.MyViewHolder holder = null;
                    int starPos = ((LinearLayoutManager) recyclerRestorent.getLayoutManager()).findFirstVisibleItemPosition();
                    int lastPos = ((LinearLayoutManager) recyclerRestorent.getLayoutManager()).findLastVisibleItemPosition();
                    if (starPos <= lastPos) {
                        while (true) {
                            holder = (RestaurantsAdp.MyViewHolder) recyclerRestorent.findViewHolderForLayoutPosition(starPos);
                            if (holder != null) {
                                holder.imageView.setRotation(holder.imageView.getRotation() + (float) dy);
                            }
                            if (starPos == lastPos) {
                                break;
                            }
                            ++starPos;
                        }
                    }
                }
            });


        }

        return view;

    }

    private void getHome() {

        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getHome(bodyRequest);
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
                Home home = gson.fromJson(result.toString(), Home.class);
                if (home.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setIntData(SessionManager.istip, home.getResultData().getMainData().getIsTip());
                    sessionManager.setStringData(SessionManager.tips, home.getResultData().getMainData().getTip());

                    sessionManager.setIntData(SessionManager.istax, home.getResultData().getMainData().getIsTax());
                    sessionManager.setStringData(SessionManager.taxs, home.getResultData().getMainData().getTax());
                    sessionManager.setStringData(SessionManager.walletname, home.getResultData().getMainData().getWname());

                    sessionManager.setStringData(SessionManager.currency, home.getResultData().getMainData().getCurrency());
                    BannerAdp adapter = new BannerAdp(getActivity(), home.getResultData().getBanner(), this);
                    recyclerBanner.setAdapter(adapter);

                    CategoryAdp adapter2 = new CategoryAdp(getActivity(), home.getResultData().getCatlist(), this);
                    recyclerCategory.setAdapter(adapter2);

                    restaurantsAdp = new RestaurantsAdp(getActivity(), home.getResultData().getRestuarantData(), this);
                    recyclerRestorent.setAdapter(restaurantsAdp);
                    imgFooter.setVisibility(View.VISIBLE);
                    if(home.getResultData().getMainData().getIsDmode().equalsIgnoreCase("1")){
                        HomeActivity.getInstance().isDevlopermode();
                    }

                }
            }
        } catch (Exception e) {
            e.toString();
        }

    }


    @Override
    public void onCategoryItem(String cid, String title, String image) {

        startActivity(new Intent(getActivity(), RestorentListActivity.class)
                .putExtra("cid", cid)
                .putExtra("title", title)
                .putExtra("image", image));
    }

    @Override
    public void onBannerItem(String titel, int position) {


    }

    @Override
    public void onRestaurantsItem(String rid, int position) {

        startActivity(new Intent(getActivity(), RestaurantsActivity.class).putExtra("rid", rid));
    }

    @OnClick({R.id.txt_address, R.id.txt_offers})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_address:
                startActivity(new Intent(getActivity(), DeliveryLocationActivity.class));
                break;
            case R.id.txt_offers:
                startActivity(new Intent(getActivity(), OffersActivity.class));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (changeAddress && custPrograssbar != null && sessionManager != null) {
            changeAddress = false;
            myAddress = sessionManager.getAddress();
            if (myAddress.getType() != null) {
                txtType.setVisibility(View.VISIBLE);

                txtType.setText("" + myAddress.getType());
            } else {
                txtType.setVisibility(View.GONE);
            }
            txtAddress.setText("" + myAddress.getAddress());
            getHome();
        }
    }
}