package com.yummoidmkschinky.customerapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.activity.AddressListActivity;
import com.yummoidmkschinky.customerapp.activity.FaqActivity;
import com.yummoidmkschinky.customerapp.activity.FavritsActivity;
import com.yummoidmkschinky.customerapp.activity.HelpActivity;
import com.yummoidmkschinky.customerapp.activity.LoginActivity;
import com.yummoidmkschinky.customerapp.activity.MywalletActivity;
import com.yummoidmkschinky.customerapp.activity.OffersActivity;
import com.yummoidmkschinky.customerapp.activity.OrderActivity;
import com.yummoidmkschinky.customerapp.activity.ReferlActivity;
import com.yummoidmkschinky.customerapp.activity.WalletActivatActivity;
import com.yummoidmkschinky.customerapp.adepter.OrderAdp;
import com.yummoidmkschinky.customerapp.model.Login;
import com.yummoidmkschinky.customerapp.model.Order;
import com.yummoidmkschinky.customerapp.model.OrderHistoryItem;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
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

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.walletname;
import static com.yummoidmkschinky.customerapp.utiles.Utility.newAddress;
import static com.yummoidmkschinky.customerapp.utiles.Utility.ratesupdat;


public class AccountFragment extends Fragment implements OrderAdp.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_noandemail)
    TextView txtNoandemail;
    @BindView(R.id.lvl_myaccount)
    LinearLayout lvlMyaccount;
    @BindView(R.id.lvl_address)
    LinearLayout lvlAddress;
    @BindView(R.id.lvl_Faq)
    LinearLayout lvlFaq;
    @BindView(R.id.lvl_subacount)
    LinearLayout lvlSubacount;
    @BindView(R.id.my_order)
    RecyclerView rvMyOrder;
    @BindView(R.id.img_arror)
    ImageView imgArror;
    @BindView(R.id.txt_wallettital)
    TextView txtWallettital;
    @BindView(R.id.txt_loadmore)
    TextView txtLoadmore;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    int count = 1;
    OrderAdp orderAdp;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyOrder.setLayoutManager(mLayoutManager);
        txtName.setText("" + user.getName());
        txtNoandemail.setText("" + user.getMobile());
        txtWallettital.setText("" + sessionManager.getStringData(walletname));
        getOrders();


        return view;
    }

    private void getOrders() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("page", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderHistry(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    List<OrderHistoryItem> orderHistoryItems = new ArrayList<>();

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Order order = gson.fromJson(result.toString(), Order.class);
                if (order.getResult().equalsIgnoreCase("true")) {
                    if (orderHistoryItems.size() == 0) {

                        orderHistoryItems = order.getOrderHistory();
                        if(order.getOrderHistory().size()<4){
                            txtLoadmore.setVisibility(View.GONE);

                        }
                        orderAdp = new OrderAdp(getActivity(), orderHistoryItems, this);
                        rvMyOrder.setAdapter(orderAdp);
                    } else if (order.getOrderHistory().size() != 0) {
                        orderHistoryItems.addAll(order.getOrderHistory());
                        orderAdp.notifyDataSetChanged();

                    } else {
                        txtLoadmore.setVisibility(View.GONE);
                    }

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();

                Login loginUser = gson.fromJson(result.toString(), Login.class);

                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUserLogin());


                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    @OnClick({R.id.lvl_refer, R.id.lvl_address, R.id.txt_loadmore, R.id.lvl_myaccount, R.id.lvl_favrits, R.id.lvl_offers, R.id.lvl_Faq, R.id.txt_edit, R.id.lvl_logout, R.id.lvl_help, R.id.lvl_munny})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lvl_refer:
                startActivity(new Intent(getActivity(), ReferlActivity.class));
                break;
            case R.id.lvl_address:
                newAddress = 1;
                startActivity(new Intent(getActivity(), AddressListActivity.class));
                break;
            case R.id.txt_loadmore:
                count++;
                getOrders();

                break;
            case R.id.lvl_favrits:
                startActivity(new Intent(getActivity(), FavritsActivity.class));
                break;
            case R.id.lvl_offers:
                startActivity(new Intent(getActivity(), OffersActivity.class));
                break;
            case R.id.lvl_Faq:
                startActivity(new Intent(getActivity(), FaqActivity.class));
                break;
            case R.id.txt_edit:
                editProfile(getActivity());
                break;
            case R.id.lvl_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.lvl_munny:
                if (user.getIsVerify() == 0) {
                    startActivity(new Intent(getActivity(), WalletActivatActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), MywalletActivity.class));
                }

                break;
            case R.id.lvl_logout:
                sessionManager.logoutUser();
                User user = new User();
                user.setId("0");
                sessionManager.setUserDetails("", user);
                startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                break;

            case R.id.lvl_myaccount:
                if (lvlSubacount.getVisibility() == View.VISIBLE) {
                    // Its visible
                    lvlSubacount.setVisibility(View.GONE);
                    imgArror.setImageResource(R.drawable.ic_profile_arrow_down);
                } else {
                    // Either gone or invisible
                    lvlSubacount.setVisibility(View.VISIBLE);
                    imgArror.setImageResource(R.drawable.ic_profile_arrow_right);

                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void onOrderItem(String oid) {

        startActivity(new Intent(getActivity(), OrderActivity.class).putExtra("oid", oid));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ratesupdat) {
            ratesupdat = false;
            getOrders();

        }
    }

    public void editProfile(Context context) {


        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.editeacount_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        EditText edName = rootView.findViewById(R.id.ed_name);
        TextView txtClick = rootView.findViewById(R.id.btn_create);
        TextInputEditText edPassword = rootView.findViewById(R.id.ed_password);

        edName.setText("" + user.getName());
        edPassword.setText("" + user.getPassword());
        txtClick.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edName.getText().toString())) {
                edName.setError("Enter Mobile");

            } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                edPassword.setError("Enter Password");

            } else {
                mBottomSheetDialog.cancel();
                orderCancle(edName.getText().toString(), edPassword.getText().toString());

            }


        });
        mBottomSheetDialog.show();


    }

    private void orderCancle(String name, String password) {

        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("name", name);
            jsonObject.put("password", password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().updateProfile(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }
}