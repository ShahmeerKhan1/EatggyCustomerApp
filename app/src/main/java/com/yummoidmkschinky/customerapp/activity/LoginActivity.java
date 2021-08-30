package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Login;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.login;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.wallet;
import static com.yummoidmkschinky.customerapp.utiles.Utility.isvarification;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.ed_mobilenumber)
    EditText edMobilenumber;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.txt_forgot)
    TextView txtForgot;
    @BindView(R.id.lvl_offer)
    LinearLayout lvlOffer;
    @BindView(R.id.lvl_sendfeedback)
    LinearLayout lvlSendfeedback;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);
    }

    private void loginuser() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mobile", edMobilenumber.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.btn_login, R.id.lvl_create, R.id.lvl_offer, R.id.lvl_sendfeedback, R.id.txt_forgot})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                if (validationCreate()) {
                    loginuser();
                }
                break;
            case R.id.lvl_create:
                startActivity(new Intent(LoginActivity.this, CreateAcountActivity.class));
                break;
            case R.id.lvl_offer:
                startActivity(new Intent(LoginActivity.this, OffersActivity.class));
                break;
            case R.id.lvl_sendfeedback:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("support@yummoid.com")));
                break;
            case R.id.txt_forgot:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public boolean validationCreate() {

        if (TextUtils.isEmpty(edMobilenumber.getText().toString())) {
            edMobilenumber.setError("Enter Mobile");
            return false;
        }

        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            edPassword.setError("Enter Password");
            return false;
        }

        return true;
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                isvarification = -1;
                Gson gson = new Gson();

                Login loginUser = gson.fromJson(result.toString(), Login.class);
                Toast.makeText(this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUserLogin());
                    sessionManager.setIntData(wallet, Integer.parseInt(loginUser.getUserLogin().getWallet()));

                    sessionManager.setBooleanData(login, true);
                    OneSignal.sendTag("userid", loginUser.getUserLogin().getId());

                    startActivity(new Intent(this, MapActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        } catch (Exception e) {
            e.toString();

        }
    }
}