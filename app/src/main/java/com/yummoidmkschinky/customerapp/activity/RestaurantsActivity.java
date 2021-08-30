package com.yummoidmkschinky.customerapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.adepter.CouponAdp;
import com.yummoidmkschinky.customerapp.adepter.CustmazationAdp;
import com.yummoidmkschinky.customerapp.adepter.ProductAdp;
import com.yummoidmkschinky.customerapp.adepter.ProductMainAdapter;
import com.yummoidmkschinky.customerapp.model.CouponItem;
import com.yummoidmkschinky.customerapp.model.MenuitemDataItem;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.Restorent;
import com.yummoidmkschinky.customerapp.model.RestuarantDataItem;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.MyHelper;
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
import static com.yummoidmkschinky.customerapp.utiles.SessionManager.restid;

public class RestaurantsActivity extends AppCompatActivity implements ProductAdp.RecyclerTouchListener, CouponAdp.RecyclerTouchListener, CustmazationAdp.RecyclerTouchListener, GetResult.MyListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_favrit)
    ImageView imgFavrit;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.iimg_restorent)
    ImageView iimgRestorent;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_rating)
    TextView txtRating;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_total)
    TextView txtTotal;

    @BindView(R.id.lvl_viewcart)
    LinearLayout lvlViewcart;
    @BindView(R.id.lvl_pureveg)
    LinearLayout lvlPureveg;
    @BindView(R.id.lvl_vegnoveg)
    LinearLayout lvlVegnoveg;
    @BindView(R.id.txt_vegnon)
    TextView txtVegnon;
    @BindView(R.id.lvl1)
    ConstraintLayout lvl1;

    @BindView(R.id.txt_cost)
    TextView txtCost;
    @BindView(R.id.img_fas)
    LinearLayout imgFas;
    @BindView(R.id.txt_fssai)
    TextView txtFssai;
    @BindView(R.id.txt_nameresto)
    TextView txtNameresto;
    @BindView(R.id.txt_fulladdres)
    TextView txtFulladdres;
    @BindView(R.id.txt_dtimesss)
    TextView txtDtimesss;
    @BindView(R.id.recycler_coupon)
    RecyclerView recyclerCoupon;
    @BindView(R.id.recycler_product_main)
    RecyclerView recyclerProductMain;
    SessionManager sessionManager;
    MyAddress myAddress;
    CustPrograssbar custPrograssbar;
    User user;
    MyHelper myHelper;
    public static RestaurantsActivity activity;
    ProductMainAdapter productMainAdapter;
    int swichveg = 0;

    public static RestaurantsActivity getInstance() {
        return activity;
    }

    String rID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorent);
        ButterKnife.bind(this);
        myHelper = new MyHelper(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        activity = this;


        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProductMain.setLayoutManager(mLayoutManager13);
        recyclerProductMain.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCoupon.setLayoutManager(mLayoutManager);
        recyclerCoupon.setItemAnimator(new DefaultItemAnimator());
        rID = getIntent().getExtras().getString("rid");
        sessionManager.setStringData(restid, rID);

        getRestorent(rID);
        if (myHelper.getAllData().getCount() == 0) {
            lvlViewcart.setVisibility(View.GONE);

        } else {
            lvlViewcart.setVisibility(View.VISIBLE);
        }
        update();

    }

    public Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int a;
        int r;
        int g;
        int b;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                a = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                r = Color.red(pixel);
                r = (int) (((((r / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (r < 0) {
                    r = 0;
                } else if (r > 255) {
                    r = 255;
                }

                g = Color.red(pixel);
                g = (int) (((((g / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (g < 0) {
                    g = 0;
                } else if (g > 255) {
                    g = 255;
                }

                b = Color.red(pixel);
                b = (int) (((((b / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (b < 0) {
                    b = 0;
                } else if (b > 255) {
                    b = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(a, r, g, b));
            }
        }
        iimgRestorent.setImageBitmap(bmOut);
        return bmOut;
    }

    private void getRestorent(String rid) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("rid", rid);
            jsonObject.put("fid", swichveg);
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getRestorent(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void getRestorentFevrit(String rid) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("rid", rid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getRestorentFavrit(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    Restorent restorent;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            if (callNo.equalsIgnoreCase("1")) {
                Handler handler = new Handler();
                handler.postDelayed(() -> custPrograssbar.closePrograssBar(), 1000);
                Gson gson = new Gson();
                restorent = gson.fromJson(result.toString(), Restorent.class);
                if (restorent.getResult().equalsIgnoreCase("true")) {
                    RestuarantDataItem restuarantDataItem=restorent.getResultData().getRestuarantData().get(0);
                    txtName.setText("" + restuarantDataItem.getRestTitle());
                    txtType.setText("" + restuarantDataItem.getRestSdesc());
                    txtAddress.setText("" + restuarantDataItem.getRestLandmark() + " | " + restuarantDataItem.getRestDistance());
                    txtRating.setText("" + restuarantDataItem.getRestRating());
                    txtTime.setText(restuarantDataItem.getRestDeliverytime() + " mins");
                    txtCost.setText(sessionManager.getStringData(currency) + restuarantDataItem.getRestCostfortwo());

                    Glide.with(this).load(APIClient.baseUrl + "/" + restuarantDataItem.getRestImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(iimgRestorent);


                    if (restuarantDataItem.getRestIsVeg() == 1) {
                        lvlPureveg.setVisibility(View.VISIBLE);
                        lvlVegnoveg.setVisibility(View.GONE);
                    } else {
                        lvlPureveg.setVisibility(View.GONE);
                        lvlVegnoveg.setVisibility(View.VISIBLE);

                    }
                    if (restuarantDataItem.getRestLicence() != null && !restuarantDataItem.getRestLicence().isEmpty()) {
                        txtFssai.setText("License No. " + restuarantDataItem.getRestLicence());

                    } else {
                        imgFas.setVisibility(View.GONE);

                    }
                    txtNameresto.setText("" + restuarantDataItem.getRestTitle());
                    txtFulladdres.setText("" + restuarantDataItem.getRestFullAddress());
                    productMainAdapter = new ProductMainAdapter(this, restorent.getResultData().getProductData(),restuarantDataItem.getRestIsopen());
                    recyclerProductMain.setAdapter(productMainAdapter);

                    CouponAdp adapter = new CouponAdp(this, restorent.getResultData().getCoupon(), this, 10101010);
                    recyclerCoupon.setAdapter(adapter);
                    if (restuarantDataItem.getISFAVOURITE() == 0) {
                        imgFavrit.setImageResource(R.drawable.like);
                    } else {
                        imgFavrit.setImageResource(R.drawable.ic_favorite);

                    }
                    if(restuarantDataItem.getRestIsopen()==0){
                        txtTime.setText("Not Serviceable");
                        txtTime.setTextColor(getResources().getColor(R.color.red));
                        txtDtimesss.setText("At the moment");
                        Handler handler1 = new Handler();
                        handler1.postDelayed(() -> {
                            BitmapDrawable drawable = (BitmapDrawable) iimgRestorent.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            createContrast(bitmap, 5);

                        }, 800);

                    }

                }

            } else if (callNo.equalsIgnoreCase("2")) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);

                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    if (restorent.getResultData().getRestuarantData().get(0).getISFAVOURITE() == 1) {
                        restorent.getResultData().getRestuarantData().get(0).setISFAVOURITE(0);
                    } else {
                        restorent.getResultData().getRestuarantData().get(0).setISFAVOURITE(1);
                    }

                }
            }
        } catch (Exception e) {
            Log.e("Error-->", e.toString());
        }

    }


    @Override
    public void onProductItem(String titel, int position) {

    }


    @OnClick({R.id.img_back, R.id.img_favrit, R.id.img_search, R.id.txt_viewcart, R.id.txt_vegnon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_favrit:
                if (restorent.getResultData().getRestuarantData().get(0).getISFAVOURITE() == 0) {
                    imgFavrit.setImageResource(R.drawable.ic_favorite);
                } else {
                    imgFavrit.setImageResource(R.drawable.like);
                }

                getRestorentFevrit(rID);
                break;
            case R.id.img_search:
                startActivity(new Intent(RestaurantsActivity.this, SearchProductActivity.class).putExtra("rid", rID));
                break;
            case R.id.txt_viewcart:
                startActivity(new Intent(RestaurantsActivity.this, CartActivity.class));
                break;
            case R.id.txt_vegnon:
                if (swichveg == 1) {
                    swichveg = 0;
                    txtVegnon.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_toggle_inactive_veg), null, null, null);
                } else {
                    swichveg = 1;
                    txtVegnon.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_toggle_active_veg), null, null, null);
                }
                getRestorent(getIntent().getExtras().getString("rid"));
                break;
            default:

                break;
        }
    }


    @Override
    public void onCustmazationItem(String titel, int position) {

    }

    public void update() {
        List<MenuitemDataItem> itemList;
        itemList = myHelper.getCData();
        if (itemList.size() == 0) {
            lvlViewcart.setVisibility(View.GONE);

        } else {
            lvlViewcart.setVisibility(View.VISIBLE);
            double total = 0;
            double aprice = 0;
            for (int i = 0; i < itemList.size(); i++) {
                MenuitemDataItem item = itemList.get(i);
                if (item.getAddonPrice() != null) {
                    String[] separated = item.getAddonPrice().split(",");
                    aprice = 0;
                    for (String price : separated) {
                        aprice = aprice + Integer.parseInt(price);
                    }
                }
                total = total + aprice + item.getPrice();
                total = total * item.getQty();
            }
            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + total);
            txtItem.setText(itemList.size() + " Item ");
        }

    }

    public void adepterUpdate() {
        productMainAdapter.notifyDataSetChanged();

    }


    @Override
    public void onCouponItem(String titel, CouponItem couponItem) {

    }
}