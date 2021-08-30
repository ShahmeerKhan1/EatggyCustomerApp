package com.yummoidmkschinky.customerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
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

import static com.yummoidmkschinky.customerapp.utiles.Utility.changeAddress;
import static com.yummoidmkschinky.customerapp.utiles.Utility.newAddress;

public class DeliveryLocationActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.recycle_address)
    androidx.recyclerview.widget.RecyclerView recycleAddress;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    int placeAutocompleteRequestCode = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(DeliveryLocationActivity.this);
        user = sessionManager.getUserDetails("");


        LinearLayoutManager layoutManager = new LinearLayoutManager(DeliveryLocationActivity.this, LinearLayoutManager.VERTICAL, false);
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
            View view = LayoutInflater.from(context).inflate(R.layout.addresss_item, parent, false);
            return new AdepterAddress.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdepterAddress.BannerHolder holder, int position) {
            holder.txtType.setText("" + listItems.get(position).getType());
            holder.txtFulladdress.setText("" + listItems.get(position).getHno()+","+listItems.get(position).getLandmark()+","+listItems.get(position).getAddress());
            Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);

            holder.txtEdit.setVisibility(View.GONE);
            holder.txtDelete.setVisibility(View.GONE);

            holder.lvlHome.setOnClickListener(v -> {
                sessionManager.setAddress(listItems.get(position));
                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //after a place is searched
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == placeAutocompleteRequestCode) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);


                Log.e("lat", "" + place.getLatLng().latitude);
                Log.e("lat", "" + place.getLatLng().longitude);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @OnClick({R.id.lvl_currentlocation, R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lvl_currentlocation:
                startActivity(new Intent(DeliveryLocationActivity.this, MapActivity.class));
                break;

            case R.id.img_back:
                finish();
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
                    recycleAddress.setAdapter(new AdepterAddress(DeliveryLocationActivity.this, address.getAddressList()));
                    changeAddress=true;

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