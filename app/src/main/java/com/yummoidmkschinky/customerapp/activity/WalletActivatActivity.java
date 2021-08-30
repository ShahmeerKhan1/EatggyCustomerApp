package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivatActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_walletname)
    TextView txtWalletname;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_activat);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(WalletActivatActivity.this);
        txtWalletname.setText(""+sessionManager.getStringData(SessionManager.walletname));

    }


    @OnClick({R.id.btn_confirm, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                startActivity(new Intent(WalletActivatActivity.this, ActivateMunnyActivity.class));
                break;
            case R.id.img_back:
                finish();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}