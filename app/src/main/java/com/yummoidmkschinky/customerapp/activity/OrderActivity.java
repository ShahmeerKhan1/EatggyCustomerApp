package com.yummoidmkschinky.customerapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.OrderDetail;
import com.yummoidmkschinky.customerapp.model.OrderItemsItem;
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

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;

public class OrderActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_rtitle)
    TextView txtRtitle;
    @BindView(R.id.txt_rlocation)
    TextView txtRlocation;
    @BindView(R.id.txt_customer)
    TextView txtCustomer;
    @BindView(R.id.txt_caddress)
    TextView txtCaddress;
    @BindView(R.id.lvl_completdate)
    LinearLayout lvlCompletdate;
    @BindView(R.id.txt_deliveryboy)
    TextView txtDeliveryboy;
    @BindView(R.id.txt_completdate)
    TextView txtCompletdate;
    @BindView(R.id.lvl_itmelist)
    LinearLayout lvlItmelist;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDcharge;
    @BindView(R.id.lvl_discount)
    LinearLayout lvlDiscount;
    @BindView(R.id.txt_discount)
    TextView txtDiscount;
    @BindView(R.id.txt_pmethod)
    TextView txtPmethod;
    @BindView(R.id.txt_topay)
    TextView txtTopay;
    @BindView(R.id.txt_orderid)
    TextView txtOrderid;

    @BindView(R.id.lvl_deliverytips)
    LinearLayout lvlDeliverytips;
    @BindView(R.id.txt_dtips)
    TextView txtDtips;
    @BindView(R.id.lvl_storecharge)
    LinearLayout lvlStorecharge;
    @BindView(R.id.txt_storecharge)
    TextView txtStorecharge;
    @BindView(R.id.lvl_texandcharge)
    LinearLayout lvlTexandcharge;
    @BindView(R.id.txt_taxcharge)
    TextView txtTaxcharge;
    @BindView(R.id.lvl_wallet)
    LinearLayout lvlWallet;

    @BindView(R.id.txt_wallet)
    TextView txtWallet;


    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    String OID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        OID = getIntent().getStringExtra("oid");

        getOrdersHistry();
    }

    private void getOrdersHistry() {
        custPrograssbar.prograssCreate(OrderActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", OID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderDetalis(bodyRequest);
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
                OrderDetail orderDetail = gson.fromJson(result.toString(), OrderDetail.class);
                if (orderDetail.getResult().equalsIgnoreCase("true")) {
                    txtRtitle.setText("" + orderDetail.getOrderInfo().getRestName());
                    txtRlocation.setText("" + orderDetail.getOrderInfo().getRestAddress());
                    txtCustomer.setText("" + orderDetail.getOrderInfo().getAddressType());
                    txtCaddress.setText("" + orderDetail.getOrderInfo().getCustAddress());

                    txtItemtotal.setText(sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getSubtotal());
                    txtDcharge.setText("+" + sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getDeliveryCharge());


                    if (orderDetail.getOrderInfo().getCouAmt() != null && orderDetail.getOrderInfo().getCouAmt().equalsIgnoreCase("0")) {
                        lvlDiscount.setVisibility(View.GONE);
                    } else {
                        txtDiscount.setText("-" + sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getCouAmt());
                    }

                    if (orderDetail.getOrderInfo().getTax() != null && orderDetail.getOrderInfo().getTax().equalsIgnoreCase("0")) {
                        lvlTexandcharge.setVisibility(View.GONE);
                    } else {
                        txtTaxcharge.setText("+" + sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getTax());
                    }

                    if (orderDetail.getOrderInfo().getRestCharge() != null && orderDetail.getOrderInfo().getRestCharge().equalsIgnoreCase("0")) {
                        lvlStorecharge.setVisibility(View.GONE);
                    } else {
                        txtStorecharge.setText("+" + sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getRestCharge());
                    }
                    if (orderDetail.getOrderInfo().getRiderTip() != null && orderDetail.getOrderInfo().getRiderTip().equalsIgnoreCase("0")) {
                        lvlDeliverytips.setVisibility(View.GONE);
                    } else {
                        txtDtips.setText(sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getRiderTip());
                    }

                    if (orderDetail.getOrderInfo().getWallAmt() != null && orderDetail.getOrderInfo().getWallAmt().equalsIgnoreCase("0")) {
                        lvlWallet.setVisibility(View.GONE);
                    } else {
                        txtWallet.setText("-" + sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getWallAmt());
                    }


                    txtTopay.setText(sessionManager.getStringData(currency) + orderDetail.getOrderInfo().getOrderTotal());
                    txtPmethod.setText("" + orderDetail.getOrderInfo().getPMethodName());
                    txtOrderid.setText("ORDERID #" + orderDetail.getOrderInfo().getOrderId());
                    if (orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Completed")) {
                        lvlCompletdate.setVisibility(View.VISIBLE);
                        txtDeliveryboy.setText("" + orderDetail.getOrderInfo().getRiderName());
                        txtCompletdate.setText("" + orderDetail.getOrderInfo().getOrderCompleteDate());
                    }
                    setNotiList(lvlItmelist, orderDetail.getOrderInfo().getOrderItems());
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    private void setNotiList(LinearLayout lnrView, List<OrderItemsItem> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(OrderActivity.this);

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
            }
            txtTitel.setText("" + list.get(i).getItemName());
            txtPextra.setText("" + list.get(i).getItemAddon());
            txtPrice.setText("" + sessionManager.getStringData(currency) + list.get(i).getItemTotal());
            lnrView.addView(view);

        }

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

        }
    }


}