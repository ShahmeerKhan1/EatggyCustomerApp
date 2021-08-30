package com.yummoidmkschinky.customerapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.adepter.CartAdp;
import com.yummoidmkschinky.customerapp.map.OrderTrackerActivity;
import com.yummoidmkschinky.customerapp.model.Address;
import com.yummoidmkschinky.customerapp.model.Cart;
import com.yummoidmkschinky.customerapp.model.MenuitemDataItem;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.PaymentItem;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.Tips;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.MyHelper;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.coupon;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.couponid;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.istax;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.istip;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.login;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.taxs;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.tips;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.wallet;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.walletname;
import static com.yummoidmkschinky.customerapp.utiles.Utility.newAddress;
import static com.yummoidmkschinky.customerapp.utiles.Utility.paymentId;
import static com.yummoidmkschinky.customerapp.utiles.Utility.paymentsucsses;
import static com.yummoidmkschinky.customerapp.utiles.Utility.tragectionID;

public class CartActivity extends AppCompatActivity implements CartAdp.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.appbar)
    com.google.android.material.appbar.AppBarLayout appbar;
    @BindView(R.id.toolbar)
    android.widget.Toolbar toolbar;
    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.recycler_tips)
    RecyclerView recyclerTips;
    @BindView(R.id.ed_note)
    EditText edNote;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_rest)
    ImageView imgRest;
    @BindView(R.id.img_coopncode)
    ImageView imgCoopncode;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDcharge;
    @BindView(R.id.lvl_discount)
    LinearLayout lvlDiscount;
    @BindView(R.id.txt_discount)
    TextView txtDiscount;
  ///  @BindView(R.id.txt_applycode)
    TextView txtApplycode;
    @BindView(R.id.txt_scharge)
    TextView txtScharge;
    @BindView(R.id.txt_topay)
    TextView txtTopay;
    @BindView(R.id.txt_deliverytip)
    TextView txtDeliverytip;
    @BindView(R.id.txt_tax)
    TextView txtTax;
    @BindView(R.id.lvl_main)
    LinearLayout lvlMain;
    @BindView(R.id.lvl_bottom)
    LinearLayout lvlBottom;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    @BindView(R.id.lvl_dtips)
    LinearLayout lvlDtips;
    @BindView(R.id.lvl_taxs)
    LinearLayout lvlTaxs;
    @BindView(R.id.lvl_notlogin)
    LinearLayout lvlNotlogin;
    @BindView(R.id.lvl_resprent)
    LinearLayout lvlResprent;
    @BindView(R.id.lvl_storeclose)
    LinearLayout lvlStoreclose;
    @BindView(R.id.lvl_addaadress)
    LinearLayout lvlAddaadress;
    @BindView(R.id.lvl_wallet)
    LinearLayout lvlWallet;
    @BindView(R.id.txt_walletname)
    TextView txtWalletname;
    @BindView(R.id.ch_wallet)
    CheckBox chWallet;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.lvl_twal)
    LinearLayout lvlTwal;
    @BindView(R.id.lvl_scharge)
    LinearLayout lvlScharge;
    @BindView(R.id.txt_waltea)
    TextView txtWaltea;
  //  @BindView(R.id.srcollview)
    NestedScrollView srcollview;

    MyHelper myHelper;
    public CartAdp adapter;
    MyAddress myAddress;
    User user;
    SessionManager sessionManager;
    public static CartActivity activity;
    CustPrograssbar custPrograssbar;
    List<PaymentItem> paymentList = new ArrayList<>();

    public static CartActivity getInstance() {
        return activity;
    }

    List<MenuitemDataItem> itemList;
    String rid;
    Cart cart;
    double total = 0;
    double itemtotal = 0;
    List<Tips> tipsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        activity = this;
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        sessionManager.setIntData(coupon, 0);

        myAddress = sessionManager.getAddress();
        user = sessionManager.getUserDetails("");


        myHelper = new MyHelper(CartActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCart.setLayoutManager(mLayoutManager);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerTips.setLayoutManager(mLayoutManager1);
        itemList = new ArrayList<>();
        itemList = myHelper.getCData();
        adapter = new CartAdp(this, itemList, this);
        recyclerCart.setItemAnimator(new DefaultItemAnimator());
        recyclerCart.setAdapter(adapter);
        if (itemList.size() != 0) {
            lvlBottom.setVisibility(View.VISIBLE);
            lvlMain.setVisibility(View.VISIBLE);
            lvlNotfound.setVisibility(View.GONE);
            rid = itemList.get(0).getRid();

            if (sessionManager.getIntData(istip) == 1) {
                String[] separated = sessionManager.getStringData(tips).split(",");
                tipsList = new ArrayList<>();
                for (String price : separated) {
                    Tips tips = new Tips();
                    tips.setPrice(Integer.parseInt(price));
                    tips.setSelect(false);
                    tipsList.add(tips);
                }
        //        recyclerTips.setAdapter(new TipsAdp(this));

            } else {
                lvlDtips.setVisibility(View.GONE);
            }
            if (myAddress.getType() != null) {
                txtType.setText("" + myAddress.getType());
                txtAddress.setText("" + myAddress.getHno() + "," + myAddress.getLandmark() + "," + myAddress.getAddress());

            } else {
                if (sessionManager.getBooleanData(login)) {
                    getAddress();
                } else {
                    lvlBottom.setVisibility(View.GONE);
                    lvlNotlogin.setVisibility(View.VISIBLE);
                }
            }
            getHome(rid);
        } else {
            lvlBottom.setVisibility(View.GONE);
            lvlMain.setVisibility(View.GONE);
            lvlNotfound.setVisibility(View.VISIBLE);
        }

        if (sessionManager.getIntData(wallet) != 0) {
            txtWalletname.setText(sessionManager.getStringData(walletname));
            chWallet.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(wallet));
            lvlWallet.setVisibility(View.VISIBLE);
        }

        chWallet.setOnCheckedChangeListener((buttonView, isChecked) -> CartActivity.this.updateCartData());
    }

    private void getHome(String rid) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", rid);
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getcartData(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

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
        getResult.callForLogin(call, "3");

    }

    public void updateCount() {
        adapter = new CartAdp(this, myHelper.getCData(), this);
        recyclerCart.setAdapter(adapter);
        updateCartData();
    }

    BottomSheetDialog mBottomSheetDialog;

    public void selectLocation(Context context, List<MyAddress> list) {
        Activity activity = (Activity) context;
        mBottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.cust_address_select, null);

        RecyclerView rvList = sheetView.findViewById(R.id.rv_list);
        TextView txtAddaddress = sheetView.findViewById(R.id.txt_addaddress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
      //  rvList.setAdapter(new AdepterAddress(this, list));
        txtAddaddress.setOnClickListener(v -> {
            newAddress = 1;
            mBottomSheetDialog.cancel();
            startActivity(new Intent(CartActivity.this, MapActivity.class));
        });
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();


    }

    double tempWallet = 0;

    public void updateCartData() {
        total = 0;
        itemtotal = 0;
        double aprice = 0;
        List<MenuitemDataItem> itemList = myHelper.getCData();
        if (itemList.size() != 0) {
            lvlBottom.setVisibility(View.VISIBLE);
            lvlMain.setVisibility(View.VISIBLE);
            lvlNotfound.setVisibility(View.GONE);
            for (int i = 0; i < itemList.size(); i++) {
                MenuitemDataItem item = myHelper.getCData().get(i);
                aprice = 0;
                if (item.getAddonPrice() != null) {
                    String[] separated = item.getAddonPrice().split(",");

                    for (String price : separated) {
                        aprice = aprice + Integer.parseInt(price);
                    }
                }
                itemtotal = itemtotal + aprice + item.getPrice();
                itemtotal = itemtotal * item.getQty();
            }
            txtItemtotal.setText(sessionManager.getStringData(currency) + itemtotal);
            total = itemtotal + Double.parseDouble(cart.getRestuarantData().get(0).getRestDcharge());
            if (sessionManager.getIntData(coupon) != 0) {
                imgCoopncode.setImageResource(R.drawable.ic_coupon_close);
            } else {
                imgCoopncode.setImageResource(R.drawable.ic_coupon_arrow);

            }

            txtDcharge.setText(sessionManager.getStringData(currency) + Double.parseDouble(cart.getRestuarantData().get(0).getRestDcharge()));


            if (cart.getRestuarantData().get(0).getRestCharge().equalsIgnoreCase("0")) {
                lvlScharge.setVisibility(View.GONE);
            } else {
                txtScharge.setText(sessionManager.getStringData(currency) + cart.getRestuarantData().get(0).getRestCharge());
            }

            if (sessionManager.getIntData(coupon) == 0) {
                lvlDiscount.setVisibility(View.GONE);
                txtApplycode.setText("Apply Coupon");
                txtApplycode.setTextColor(getResources().getColor(R.color.black));

            } else {
                txtApplycode.setText("Coupon code has been applied ! ");
                txtApplycode.setTextColor(getResources().getColor(R.color.colorgreen));
                lvlDiscount.setVisibility(View.VISIBLE);
                txtDiscount.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(coupon));
            }

            total = total + Double.parseDouble(cart.getRestuarantData().get(0).getRestCharge());
            total = total - sessionManager.getIntData(coupon);
            if (tipsList.get(pposition).isSelect()) {
                lvlDtips.setVisibility(View.VISIBLE);
                total = total + tipsList.get(pposition).getPrice();
                txtDeliverytip.setText(sessionManager.getStringData(currency) + tipsList.get(pposition).getPrice());
            } else {
                lvlDtips.setVisibility(View.GONE);

            }
            if (sessionManager.getIntData(istax) == 1) {
                lvlTaxs.setVisibility(View.VISIBLE);
                txtTax.setText(sessionManager.getStringData(currency) + sessionManager.getStringData(taxs));
                total = total + Integer.parseInt(sessionManager.getStringData(taxs));

            } else {
                lvlTaxs.setVisibility(View.GONE);
            }
            if (user.getIsVerify() == 1) {
                if (chWallet.isChecked()) {

                    if (sessionManager.getIntData(SessionManager.wallet) <= total) {

                        total = total - sessionManager.getIntData(SessionManager.wallet);
                        chWallet.setText(sessionManager.getStringData(SessionManager.currency) + "0");
                        tempWallet = sessionManager.getIntData(SessionManager.wallet);
                    } else {
                        tempWallet = sessionManager.getIntData(SessionManager.wallet) - total;
                        chWallet.setText(sessionManager.getStringData(SessionManager.currency) + tempWallet);
                        tempWallet = total;
                        total = 0;

                    }
                    lvlTwal.setVisibility(View.VISIBLE);
                    txtWaltea.setText(sessionManager.getStringData(SessionManager.currency) + tempWallet);
                } else {
                    lvlTwal.setVisibility(View.GONE);
                    tempWallet = 0;
                    chWallet.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(wallet));
                }
            }


            txtTopay.setText(sessionManager.getStringData(currency) + total);
            txtTotal.setText(sessionManager.getStringData(currency) + total);

        } else {
            lvlBottom.setVisibility(View.GONE);
            lvlMain.setVisibility(View.GONE);
            lvlNotfound.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onCartItem(String titel, int position) {
    }

    @OnClick()
    public void onClickback(View view) {
        finish();
    }

//    @OnClick({R.id.txt_viewll, R.id.btn_addaddres, R.id.txt_change, R.id.txt_process, R.id.img_coopncode, R.id.btn_login, R.id.lvl_storeclose})
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.btn_addaddres:
//                newAddress = 1;
 //               startActivity(new Intent(this, MapActivity.class));
  //              break;
            case R.id.txt_change:
                getAddress();
                break;
            case R.id.txt_process:
                if (Integer.parseInt(cart.getRestuarantData().get(0).getRestMorder()) <= itemtotal) {
                    if (total == 0) {
                        paymentId = "5";
                        new AsyncTaskRunner().execute("0");
                    } else {
                        bottonPaymentList();

                    }
                } else {
                    Toast.makeText(CartActivity.this, "Minimum order amount " + cart.getRestuarantData().get(0).getRestMorder(), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.lvl_storeclose:
                finish();
                break;
  //          case R.id.txt_viewll:
  //              srcollview.fullScroll(View.FOCUS_DOWN);
    //        break;
            case R.id.btn_login:
                startActivity(new Intent(CartActivity.this, LoginActivity.class));
                break;
            case R.id.img_coopncode:
                if (sessionManager.getIntData(coupon) != 0) {
                    imgCoopncode.setImageResource(R.drawable.ic_cancel_coupon);
                    sessionManager.setIntData(coupon, 0);
                    updateCartData();
                } else {
                    int temp = (int) Math.round(total);
                    startActivity(new Intent(CartActivity.this, CoupunActivity.class).putExtra("rid", rid).putExtra("amount", temp));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public void bottonPaymentList() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(currency) + total);
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(CartActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_paymentitem, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(CartActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(CartActivity.this).load(R.drawable.animationbg)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                paymentId = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(total);
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;
                        case "Cash On Delivery":
                            new AsyncTaskRunner().execute("0");
                            mBottomSheetDialog.cancel();
                            break;
                        case "Paypal":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, PaypalActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, StripPaymentActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;

                        case "FlutterWave":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, FlutterwaveActivity.class).putExtra("amount", total));
                            break;
                        case "Paytm":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, PaytmActivity.class).putExtra("amount", total));
                            break;
                        case "SenangPay":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, SenangpayActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listView.addView(view);
        }
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {


        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            Cursor res = myHelper.getAllData();
            while (res.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    int aprice = 0;
                    String temp = res.getString(13);
                    if (temp != null && !temp.isEmpty()) {
                        String[] separated = temp.split(",");
                        for (String price : separated) {
                            aprice = aprice + Integer.parseInt(price);
                        }
                    }
                    aprice = aprice + Integer.parseInt(res.getString(6));
                    jsonObject.put("pid", res.getString(2));
                    jsonObject.put("title", res.getString(3));
                    jsonObject.put("cost", aprice);
                    jsonObject.put("qty", res.getString(7));
                    jsonObject.put("addon", res.getString(12));
                    jsonObject.put("type", res.getString(10));
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonArray;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            // execution of result of Long time consuming operation
            orderPlace(jsonArray);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    public void orderPlace(JSONArray jsonArray) {
        custPrograssbar.prograssCreate(CartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rest_id", rid);
            jsonObject.put("uid", user.getId());
            jsonObject.put("p_method_id", paymentId);
            jsonObject.put("full_address", myAddress.getHno() + myAddress.getLandmark() + myAddress.getAddress());
            jsonObject.put("atype", myAddress.getType());
            jsonObject.put("d_charge", cart.getRestuarantData().get(0).getRestDcharge());
            jsonObject.put("cou_id", sessionManager.getIntData(couponid));
            jsonObject.put("cou_amt", sessionManager.getIntData(coupon));
            jsonObject.put("transaction_id", tragectionID);
            jsonObject.put("product_total", total);
            jsonObject.put("product_subtotal", itemtotal);
            jsonObject.put("a_note", edNote.getText().toString());
            jsonObject.put("wall_amt", tempWallet);
            jsonObject.put("tax", sessionManager.getStringData(taxs));
            if (tipsList.get(pposition).isSelect()) {
                jsonObject.put("tip", tipsList.get(pposition).getPrice());
            } else {
                jsonObject.put("tip", "0");
            }
            jsonObject.put("rest_charge", cart.getRestuarantData().get(0).getRestCharge());
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());
            jsonObject.put("ProductData", jsonArray);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getOrderNow(bodyRequest);
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
                cart = gson.fromJson(result.toString(), Cart.class);
                if (cart.getResult().equalsIgnoreCase("true")) {
                    Glide.with(this).load(APIClient.baseUrl + "/" + cart.getRestuarantData().get(0).getRestImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgRest);
                    txtTitle.setText("" + cart.getRestuarantData().get(0).getRestTitle());
                    txtLocation.setText("" + cart.getRestuarantData().get(0).getRestFullAddress());
                    paymentList = new ArrayList<>();
                    paymentList = cart.getPaymentItems();
                    if (cart.getRestuarantData().get(0).getRestIsopen() == 0) {
                        lvlStoreclose.setVisibility(View.VISIBLE);
                        lvlBottom.setVisibility(View.GONE);
                    }
                    updateCartData();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                sessionManager.setIntData(wallet, Integer.parseInt(restResponse.getWallet()));
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    myHelper.deleteCard();
                    sessionManager.setIntData(wallet, Integer.parseInt(restResponse.getWallet()));
                    Intent intent = new Intent(this, OrderTrackerActivity.class).putExtra("oid", restResponse.getOrderId());
                    startActivity(intent);
                    finish();

                } else {
                    finish();
                    Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            } else if (callNo.equalsIgnoreCase("3")) {

                Gson gson = new Gson();
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    selectLocation(this, address.getAddressList());
                } else {
                    lvlAddaadress.setVisibility(View.VISIBLE);
                    lvlBottom.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.toString();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cart != null) {
            updateCartData();
        }
        if(newAddress==1){
            newAddress=0;
             if (myAddress.getType() != null) {
                txtType.setText("" + myAddress.getType());
                txtAddress.setText("" + myAddress.getHno() + "," + myAddress.getLandmark() + "," + myAddress.getAddress());

            } else {
                if (sessionManager.getBooleanData(login)) {
                    getAddress();
                } else {
                    lvlBottom.setVisibility(View.GONE);
                    lvlNotlogin.setVisibility(View.VISIBLE);
                }
            }
        }


        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            new AsyncTaskRunner().execute("0");

        }
    }

    int pposition = 0;

 //   public class TipsAdp extends RecyclerView.Adapter<TipsAdp.MyViewHolder> {
   //     private Context mContext;


     //   public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

        //    public MyViewHolder(View view) {
          //      super(view);
           //     title = (TextView) view.findViewById(R.id.txt_title);
         //   }
       // }

       // public TipsAdp(Context mContext) {
        //    this.mContext = mContext;


       // }

     //   @Override
      //  public TipsAdp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       //     View itemView = LayoutInflater.from(parent.getContext())
       //             .inflate(R.layout.item_tips, parent, false);
       //     return new TipsAdp.MyViewHolder(itemView);
      //  }

     //   @Override
     //   public void onBindViewHolder(final TipsAdp.MyViewHolder holder, int position) {

       //     Tips item = tipsList.get(position);
       //     holder.title.setText("" + item.getPrice());
        //    if (item.isSelect) {
         //       holder.title.setBackground(mContext.getDrawable(R.drawable.tip1));
          //      holder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        //        holder.title.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.ic_tip_close), null);
        //    } else {
          //      holder.title.setBackground(mContext.getDrawable(R.drawable.tip0));
           //     holder.title.setTextColor(mContext.getResources().getColor(R.color.black));
           //     holder.title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            }
         //   holder.title.setOnClickListener(v -> {
          //      if (item.isSelect()) {

           //         pposition = position;
           //         item.setSelect(false);
           //         tipsList.set(position, item);
            //        notifyDataSetChanged();
             //   } else {
             //       Tips tips = tipsList.get(pposition);
         //           tips.setSelect(false);
           //         tipsList.set(pposition, tips);
               //     pposition = position;
             //       item.setSelect(true);
                //    tipsList.set(position, item);
            //        notifyDataSetChanged();
             //   }
               // updateCartData();

            //});
        //}

 //       @Override
   //     public int getItemCount() {
     //       return tipsList.size();

       // }
    //}

  //  public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
    //    private Context context;
    //    private List<MyAddress> listItems;

  //      public AdepterAddress(Context context, List<MyAddress> mBanner) {
  //          this.context = context;
  //          this.listItems = mBanner;
  //      }

  //      @NonNull
   //     @Override
   //     public AdepterAddress.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   //         View view = LayoutInflater.from(context).inflate(R.layout.addresss_item1, parent, false);
    //        return new AdepterAddress.BannerHolder(view);
     //   }

    //    @Override
    //    public void onBindViewHolder(@NonNull AdepterAddress.BannerHolder holder, int position) {
     //       holder.txtType.setText("" + listItems.get(position).getType());
     //       holder.txtFulladdress.setText("" + listItems.get(position).getHno() + "," + listItems.get(position).getLandmark() + "," + listItems.get(position).getAddress());
      //      Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);
       //     holder.lvlHome.setOnClickListener(v -> {
      //          MyAddress myAddress = listItems.get(position);
   //             sessionManager.setAddress(myAddress);
     //           if (mBottomSheetDialog != null)
       //             mBottomSheetDialog.cancel();
         //       recreate();
     //       });

      //  }

   //     @Override
     //   public int getItemCount() {
       //     return listItems.size();
      //  }

   //     public class BannerHolder extends RecyclerView.ViewHolder {
     //       @BindView(R.id.img_banner)
      //      ImageView imgBanner;

       //     @BindView(R.id.txt_type)
        //    TextView txtType;
        //    @BindView(R.id.txt_fulladdress)
        //    TextView txtFulladdress;

     //       @BindView(R.id.lvl_home)
       //     LinearLayout lvlHome;

         //   public BannerHolder(@NonNull View itemView) {
           //     super(itemView);
         //       ButterKnife.bind(this, itemView);
        //    }
      //  }
   // }
//}