package com.yummoidmkschinky.customerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Address;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.Utility.newAddress;

public class AddressListActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.recycle_address)
    androidx.recyclerview.widget.RecyclerView recycleAddress;
    @BindView(R.id.lvl_addaadress)
    LinearLayout lvlAddaadress;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(AddressListActivity.this);
        user = sessionManager.getUserDetails("");
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddressListActivity.this, LinearLayoutManager.VERTICAL, false);
        recycleAddress.setLayoutManager(layoutManager);
        getAddress();
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

    public void removeAddress(String aid) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("aid", aid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().removeAddress(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

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
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.addresss_item, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            holder.txtType.setText("" + listItems.get(position).getType());
            holder.txtFulladdress.setText("" + listItems.get(position).getHno() + "," + listItems.get(position).getLandmark() + "," + listItems.get(position).getAddress());


            Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);
            holder.txtEdit.setOnClickListener(v -> {
                newAddress = 1;
                startActivity(new Intent(AddressListActivity.this, MapActivity.class)
                        .putExtra("lat", Double.parseDouble(listItems.get(position).getLatMap()))
                        .putExtra("long", Double.parseDouble(listItems.get(position).getLongMap()))
                        .putExtra("landmark", listItems.get(position).getLandmark())
                        .putExtra("hno", listItems.get(position).getHno())
                        .putExtra("atype", listItems.get(position).getType())
                        .putExtra("newuser", "old")
                        .putExtra("userid", user.getId())
                        .putExtra("aid", listItems.get(position).getId()));


            });

            holder.txtDelete.setOnClickListener(v -> {
                removeAddress(listItems.get(position).getId());
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

            @BindView(R.id.txt_edit)
            TextView txtEdit;

            @BindView(R.id.txt_delete)
            TextView txtDelete;

            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    @OnClick({R.id.img_back, R.id.recycle_address, R.id.lvl_addaadress})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.recycle_address:

                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.lvl_addaadress:
                newAddress = 1;
                startActivity(new Intent(this, MapActivity.class));

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
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
                    recycleAddress.setVisibility(View.VISIBLE);
                    recycleAddress.setAdapter(new AdepterAddress(AddressListActivity.this, address.getAddressList()));

                }else {
                    recycleAddress.setVisibility(View.GONE);
                    sessionManager.setAddress(new MyAddress());

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    getAddress();

                }
            }
        } catch (Exception e) {
            e.toString();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null && newAddress == 1) {
            newAddress=0;
            getAddress();
        }
    }
}