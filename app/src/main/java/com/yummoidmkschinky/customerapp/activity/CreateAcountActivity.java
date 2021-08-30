package com.yummoidmkschinky.customerapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Contry;
import com.yummoidmkschinky.customerapp.model.CountryCodeItem;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.yummoidmkschinky.customerapp.utiles.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class CreateAcountActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.ed_mobilenumber)
    EditText edMobilenumber;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_refercode)
    EditText edRefercode;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.spinner)
    Spinner spinner;
    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CreateAcountActivity.this);
        getCode();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }




    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getCountryCode();
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        arealist.add(cCodes.get(i).getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(edName.getText().toString())) {
            edName.setError("Enter Name");
            return false;
        }
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

    @OnClick({R.id.btn_create})
    public void onClick(View view) {

        if (view.getId() == R.id.btn_create && validationCreate()) {

            Utility.isvarification = 1;
            User user = new User();
            user.setCcode(codeSelect);
            user.setName("" + edName.getText().toString());
            user.setMobile("" + edMobilenumber.getText().toString());
            user.setPassword("" + edPassword.getText().toString());
            user.setRefercode("" + edRefercode.getText().toString());
            sessionManager.setUserDetails("", user);
            Utility.isvarification = 1;
            startActivity(new Intent(this, VerifyPhoneActivity.class));

        }
    }
}