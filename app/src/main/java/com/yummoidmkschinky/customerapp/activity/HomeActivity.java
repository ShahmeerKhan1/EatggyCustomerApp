package com.yummoidmkschinky.customerapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.fragment.AccountFragment;
import com.yummoidmkschinky.customerapp.fragment.HomeFragment;
import com.yummoidmkschinky.customerapp.model.Address;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.MyHelper;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.yummoidmkschinky.customerapp.utiles.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.login;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GetResult.MyListener {

    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    @BindView(R.id.lvl_dmode)
    LinearLayout lvlDmode;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    SessionManager sessionManager;
    MyHelper myHelper;
    public static HomeActivity homeActivity;

    public static HomeActivity getInstance() {
        return homeActivity;
    }

    View cartBadge;
    CustPrograssbar custPrograssbar;
    User user;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        homeActivity = this;

        cartBadge = LayoutInflater.from(this)
                .inflate(R.layout.custom_cart_item_layout,
                        bottomNavigation, false);

        myHelper = new MyHelper(HomeActivity.this);
        sessionManager = new SessionManager(HomeActivity.this);
        user = sessionManager.getUserDetails("");
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(this)) {
            if (sessionManager.getBooleanData(login)) {
                getAddress();

            } else {
                selectLocation(this, new ArrayList<>());
            }

        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            bottomNavigation.setSelectedItemId(R.id.menu_home);
        }

        cartCounter();
    }
    public void isDevlopermode(){
        lvlDmode.setVisibility(View.VISIBLE);
        fragmentFrame.setVisibility(View.GONE);
        bottomNavigation.setVisibility(View.GONE);
    }

    public void cartCounter() {

        int total = myHelper.getAllData().getCount();


        if (total == 0) {

            BottomNavigationMenuView mbottomNavigationMenuView =
                    (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
            View view = mbottomNavigationMenuView.getChildAt(2);
            BottomNavigationItemView itemView = (BottomNavigationItemView) view;


            itemView.removeView(cartBadge);

        } else {
            BottomNavigationMenuView mbottomNavigationMenuView =
                    (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
            View view = mbottomNavigationMenuView.getChildAt(2);
            BottomNavigationItemView itemView = (BottomNavigationItemView) view;

            itemView.removeView(cartBadge);
            TextView txtItemt = cartBadge.findViewById(R.id.txt_itemt);
            txtItemt.setText("" + total);
            itemView.addView(cartBadge);

        }


    }

    public void callFragment(Fragment fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentClass).addToBackStack("adds").commit();

    }

    BottomSheetDialog mBottomSheetDialog;

    public void selectLocation(Context context, List<MyAddress> list) {
        Activity activity = (Activity) context;
        mBottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.cust_location_layout, null);
        Button button = sheetView.findViewById(R.id.btn_location);
        TextView txtTitle = sheetView.findViewById(R.id.txt_title);
        RecyclerView rvList = sheetView.findViewById(R.id.rv_list);
        if (sessionManager.getBooleanData(login)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvList.setLayoutManager(layoutManager);
            rvList.setAdapter(new AdepterAddress(this, list));
        } else {
            rvList.setVisibility(View.GONE);
            txtTitle.setVisibility(View.GONE);
        }

        button.setOnClickListener(v -> {

            final LocationManager manager = (LocationManager) HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(HomeActivity.this)) {
                Utility.enableLoc(HomeActivity.this);
                mBottomSheetDialog.cancel();
            }
        });
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();


    }


    private void getAddress() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().addressList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                callFragment(new HomeFragment());
                return true;
            case R.id.menu_serch:
                startActivity(new Intent(this, SearchRestorentActivity.class));

                return true;

            case R.id.menu_cart:
                startActivity(new Intent(this, CartActivity.class));
                return true;

            case R.id.menu_acount:
                if (sessionManager.getBooleanData(login)) {
                    callFragment(new AccountFragment());
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }

                return true;


            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("error", "-->" + resultCode);
        Log.e("error data", "-->" + data);
        if (resultCode == -1) {
            callFragment(new HomeFragment());

        }
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    selectLocation(HomeActivity.this, address.getAddressList());

                }
            }
        } catch (Exception e) {
            e.toString();

        }
    }

    public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
        private Context context;
        private List<MyAddress> listItems;

        public AdepterAddress(Context context, List<MyAddress> mBanner) {
            this.context = context;
            this.listItems = mBanner;
        }

        @NonNull
        @Override
        public AdepterAddress.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.addresss_item1, parent, false);
            return new AdepterAddress.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdepterAddress.BannerHolder holder, int position) {
            holder.txtType.setText("" + listItems.get(position).getType());
            holder.txtFulladdress.setText("" + listItems.get(position).getAddress());
            Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);
            holder.lvlHome.setOnClickListener(v -> {
                MyAddress myAddress = listItems.get(position);
                sessionManager.setAddress(myAddress);
                if (mBottomSheetDialog != null)
                    mBottomSheetDialog.cancel();
                bottomNavigation.setSelectedItemId(R.id.menu_home);

            });

        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;

            @BindView(R.id.txt_type)
            TextView txtType;
            @BindView(R.id.txt_fulladdress)
            TextView txtFulladdress;

            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment test = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        if (test == null && !test.isVisible()) {
            finish();
        }
    }
}