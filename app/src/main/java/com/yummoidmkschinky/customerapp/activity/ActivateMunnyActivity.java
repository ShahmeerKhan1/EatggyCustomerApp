package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Login;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.MyDatePickerDialog;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.Utility.isvarification;
import static com.yummoidmkschinky.customerapp.utiles.Utility.walletActivat;

public class ActivateMunnyActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_mobile)
    TextView txtMobile;
    @BindView(R.id.ed_fname)
    com.google.android.material.textfield.TextInputEditText edFname;
    @BindView(R.id.ed_lname)
    com.google.android.material.textfield.TextInputEditText edLname;
    @BindView(R.id.ed_birdate)
    com.google.android.material.textfield.TextInputEditText edBirdate;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ed_govid)
    com.google.android.material.textfield.TextInputEditText edGovid;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_munny);
        ButterKnife.bind(this);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ActivateMunnyActivity.this);
        user = sessionManager.getUserDetails("");
        String[] codeSelect = {"PAN", "DRIVING LICENSE", "PASSPORT", "VOTER ID", "AADHAR"};
        txtMobile.setText("" + user.getCcode() + user.getMobile());
        edFname.setText("" + user.getName());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, codeSelect);
        dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edGovid.setHint("" + spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void activateWallet() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("fname", edFname.getText().toString());
            jsonObject.put("lname", edLname.getText().toString());
            jsonObject.put("mobile", user.getMobile());
            jsonObject.put("dob", edBirdate.getText().toString());
            jsonObject.put("title", spinner.getSelectedItem().toString());
            jsonObject.put("gnum", edGovid.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().walletActivate(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @OnClick({R.id.img_back, R.id.btn_confirm, R.id.ed_birdate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (validationCreate()) {
                    isvarification = 3;
                    startActivity(new Intent(ActivateMunnyActivity.this, VerifyPhoneActivity.class));
                }
                break;
            case R.id.ed_birdate:
                MyDatePickerDialog dialog = new MyDatePickerDialog(this);
                dialog.setTitle("Select your Date of Birth");
                dialog.showDatePicker((view1, year, month, dayOfMonth) -> {
                    //Date select callback
                    edBirdate.setText("" + dayOfMonth + "/" + (month+1) + "/" + (year));
                }, Calendar.getInstance());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public boolean validationCreate() {

        if (TextUtils.isEmpty(edFname.getText().toString())) {
            edFname.setError("Enter Name");
            return false;
        }

        if (TextUtils.isEmpty(edLname.getText().toString())) {
            edLname.setError("Enter Last Name");
            return false;
        }
        if (TextUtils.isEmpty(edBirdate.getText().toString())) {
            edBirdate.setError("Enter Birth Day");
            return false;
        }
        if (TextUtils.isEmpty(edGovid.getText().toString())) {
            edGovid.setError("Enter Government ID");
            return false;
        }


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (walletActivat) {
            walletActivat = false;
            activateWallet();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            Gson gson = new Gson();

            Login loginUser = gson.fromJson(result.toString(), Login.class);
            Toast.makeText(this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
            if (loginUser.getResult().equalsIgnoreCase("true")) {
                sessionManager.setUserDetails("", loginUser.getUserLogin());
                startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        } catch (Exception e) {
        e.toString();
        }
    }
}