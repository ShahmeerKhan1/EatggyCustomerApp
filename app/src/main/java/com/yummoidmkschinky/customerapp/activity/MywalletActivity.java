package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.model.Wallet;
import com.yummoidmkschinky.customerapp.model.WalletitemItem;
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
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.wallet;
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.walletname;

public class MywalletActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.txt_walletname)
    TextView txtWalletname;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_addmunny)
    TextView txtAddmunny;
    @BindView(R.id.recy_transaction)
    RecyclerView recyTransaction;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    public static boolean walletUp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        txtWalletname.setText("" + sessionManager.getStringData(walletname));
        txtTotal.setText("" + sessionManager.getStringData(currency) + sessionManager.getIntData(wallet));

        recyTransaction.setLayoutManager(new GridLayoutManager(this, 1));
        recyTransaction.setItemAnimator(new DefaultItemAnimator());
        getHistry();

    }

    private void getHistry() {
        custPrograssbar.prograssCreate(MywalletActivity.this);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().walletReport(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back, R.id.txt_addmunny})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:

                finish();
                break;
            case R.id.txt_addmunny:
                startActivity(new Intent(MywalletActivity.this, AddMoneyActivity.class));
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
                Wallet walletHistry = gson.fromJson(result.toString(), Wallet.class);
                sessionManager.setIntData(wallet,Integer.parseInt(walletHistry.getWallets()));
                txtTotal.setText("" + sessionManager.getStringData(currency) + sessionManager.getIntData(wallet));
                if (walletHistry.getResult().equalsIgnoreCase("true")) {
                    if (walletHistry.getWalletitem().isEmpty()) {
                        recyTransaction.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                    } else {
                        HistryAdp histryAdp = new HistryAdp(walletHistry.getWalletitem());
                        recyTransaction.setAdapter(histryAdp);
                    }

                } else {
                    recyTransaction.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.toString();

        }

    }

    public class HistryAdp extends RecyclerView.Adapter<HistryAdp.MyViewHolder> {
        private List<WalletitemItem> list;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtDate;
            public TextView txtMessage;

            public TextView txtAmount;

            public MyViewHolder(View view) {
                super(view);
                txtDate = (TextView) view.findViewById(R.id.txt_date);
                txtMessage = (TextView) view.findViewById(R.id.txt_message);

                txtAmount = (TextView) view.findViewById(R.id.txt_amount);
            }
        }

        public HistryAdp(List<WalletitemItem> list) {
            this.list = list;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_histry, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            WalletitemItem category = list.get(position);
            holder.txtDate.setText("" + category.getTdate());
            holder.txtMessage.setText(category.getMessage());

            if(category.getStatus().equalsIgnoreCase("Credit")){
                holder.txtAmount.setTextColor(getResources().getColor(R.color.colorgreen));
                holder.txtAmount.setText("+"+sessionManager.getStringData(SessionManager.currency) + category.getAmt());
            }else {
                holder.txtAmount.setText("-"+sessionManager.getStringData(SessionManager.currency) + category.getAmt());
                holder.txtAmount.setTextColor(getResources().getColor(R.color.red));
            }



        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(walletUp){
            walletUp=false;
            getHistry();
        }

    }
}