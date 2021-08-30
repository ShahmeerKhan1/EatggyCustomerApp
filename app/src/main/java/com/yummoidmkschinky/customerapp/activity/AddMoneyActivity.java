package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Payment;
import com.yummoidmkschinky.customerapp.model.PaymentItem;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

import static com.yummoidmkschinky.customerapp.activity.MywalletActivity.walletUp;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.currency;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.wallet;
import static com.yummoidmkschinky.customerapp.utiles.Utility.paymentId;
import static com.yummoidmkschinky.customerapp.utiles.Utility.paymentsucsses;

public class AddMoneyActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.iimg_restorent)
    ImageView iimgRestorent;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.ed_amount)
    TextInputEditText edAmount;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    List<PaymentItem> paymentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(AddMoneyActivity.this);
        user = sessionManager.getUserDetails("");
        txtName.setText("" + sessionManager.getStringData(SessionManager.walletname));
        txtTotal.setText("Available balance: " + sessionManager.getStringData(currency) + sessionManager.getIntData(wallet));

    }

    private void getPaymentList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().paymentlist(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addAmount() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("wallet", edAmount.getText().toString());

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().walletUpdate(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void bottonPaymentList() {
        double total = Double.parseDouble(edAmount.getText().toString());
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(currency) + total);
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_paymentitem, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                paymentId = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(total);
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;

                        case "Paypal":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, PaypalActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, StripPaymentActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;

                        case "FlutterWave":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, FlutterwaveActivity.class).putExtra("amount", total));
                            break;
                        case "Paytm":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, PaytmActivity.class).putExtra("amount", total));
                            break;
                        case "SenangPay":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(this, SenangpayActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
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


    @OnClick({R.id.img_back, R.id.btn_processd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_processd:
                if (TextUtils.isEmpty(edAmount.getText().toString())) {
                    edAmount.setError("Enter Amount");

                } else {
                    getPaymentList();

                }

                break;

            default:

                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                walletUp=true;
                if (response.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                if (payment.getResult().equalsIgnoreCase("true")) {
                    paymentList = new ArrayList<>();
                    for (int i = 0; i < payment.getPaymentdata().size(); i++) {
                        if (payment.getPaymentdata().get(i).getpShow().equalsIgnoreCase("1")) {
                            paymentList.add(payment.getPaymentdata().get(i));
                        }
                    }

                    bottonPaymentList();
                }
            }


        } catch (Exception e) {
            e.toString();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            addAmount();

        }

    }
}