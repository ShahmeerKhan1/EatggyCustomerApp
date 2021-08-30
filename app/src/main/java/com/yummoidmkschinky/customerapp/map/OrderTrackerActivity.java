package com.yummoidmkschinky.customerapp.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.activity.OrderActivity;
import com.yummoidmkschinky.customerapp.model.Mapinfo;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.OrderMap;
import com.yummoidmkschinky.customerapp.model.RestResponse;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class OrderTrackerActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GetResult.MyListener {

    @BindView(R.id.layoutMap)
    LinearLayout layoutMap;


    @BindView(R.id.img_restorent)
    ImageView imgRestorent;
    @BindView(R.id.txt_rtitle)
    TextView txtRtitle;
    @BindView(R.id.txt_rstatus)
    TextView txtRstatus;
    @BindView(R.id.txt_rmobile)
    TextView txtRmobile;
    @BindView(R.id.txt_canceloreder)
    TextView txtCanceloreder;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.lvl_deleveryboy)
    LinearLayout lvlDeleveryboy;
    @BindView(R.id.lvl_restorent)
    LinearLayout lvlRestorent;
    @BindView(R.id.img_delivery)
    ImageView imgDelivery;
    @BindView(R.id.txt_deliveryboy)
    TextView txtDeliveryboy;
    @BindView(R.id.txt_dmobile)
    TextView txtDmobile;
    @BindView(R.id.txt_dstatus)
    TextView txtDstatus;
    @BindView(R.id.txt_ordertime)
    TextView txtOrdertime;
    @BindView(R.id.txt_orderid)
    TextView txtOrderid;
    @BindView(R.id.img_more)
    ImageView imgMore;


    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;

    FusedLocationProviderClient fusedLocationProviderClient;


    private int progressStatus = 50;
    private Handler handler = new Handler();


    GoogleMap mMap;
    MarkerOptions place1;
    MarkerOptions place2;
    MarkerOptions placePath;
    Marker marker;
    MyAddress myAddress;
    private Polyline currentPolyline;
    String oID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordertrack);
        custPrograssbar = new CustPrograssbar();
        ButterKnife.bind(this);
        oID = getIntent().getStringExtra("oid");
        sessionManager = new SessionManager(OrderTrackerActivity.this);
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        fusedLocationProviderClient = getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getOrdersTrack();


    }

    private void getOrdersTrack() {
        custPrograssbar.prograssCreate(OrderTrackerActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderMaps(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void orderCancle() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().orderCancel(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                OrderMap orderMap = gson.fromJson(result.toString(), OrderMap.class);
                if (orderMap.getResult().equalsIgnoreCase("true")) {
                    Mapinfo mapinfo = orderMap.getMapinfo();

                    txtDmobile.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + mapinfo.getRiderMobile()));
                        startActivity(intent);
                    });

                    txtRmobile.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + mapinfo.getRestMobile()));
                        startActivity(intent);
                    });
                    if (30 > mapinfo.getOrderArriveSeconds()) {

                        new Thread(() -> {
                            while (progressStatus < 100) {
                                progressStatus += 1;

                                //Try to sleep the thread for 20 milliseconds
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                //Update the progress bar
                                handler.post(() -> {
                                    Log.e("ss", "-->" + progressStatus);
                                    pb.setProgress(progressStatus);
                                    if (progressStatus >= 100) {
                                        txtCanceloreder.setVisibility(View.GONE);
                                        pb.setVisibility(View.GONE);
                                        lvlDeleveryboy.setVisibility(View.VISIBLE);
                                        txtDeliveryboy.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).start();
                    } else {
                        txtCanceloreder.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        lvlDeleveryboy.setVisibility(View.VISIBLE);
                        txtDeliveryboy.setVisibility(View.GONE);

                    }

                    txtOrderid.setText("" + mapinfo.getOrderid());
                    txtRtitle.setText("" + mapinfo.getRestName());
                    txtRstatus.setText("" + mapinfo.getRestMsg());
                    txtOrdertime.setText("" + mapinfo.getArriveTime());
                    Glide.with(this).load(APIClient.baseUrl + "/" + mapinfo.getRestImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgRestorent);

                    switch (orderMap.getMapinfo().getOrderStep()) {
                        case 1:
                            if (mMap != null && marker != null) {

                                animateMarkerToGB(marker, new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs()), new LatLngInterpolator.Spherical());
                            } else {
                                place1 = new MarkerOptions().position(new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs())).title(mapinfo.getRestName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_restro));
                                LatLng coordinate = new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs());
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
                                mMap.animateCamera(yourLocation);
                                marker = mMap.addMarker(place1);

                                marker.showInfoWindow();
                            }

                            break;
                        case 2:
                            if (mMap != null && marker != null) {

                                animateMarkerToGB(marker, new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs()), new LatLngInterpolator.Spherical());

                            } else {

                                placePath = new MarkerOptions().position(new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs())).title("Path");
                                place1 = new MarkerOptions().position(new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs())).title(mapinfo.getRestName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_restro));
                                place2 = new MarkerOptions().position(new LatLng(mapinfo.getRiderLats(), mapinfo.getRiderLongs())).title(mapinfo.getRiderName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_deliveryboy));
                                LatLng coordinate = new LatLng(mapinfo.getRestLats(), mapinfo.getRestLongs());
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
                                mMap.animateCamera(yourLocation);
                                marker = mMap.addMarker(place1);
                                mMap.addMarker(place2);
                                marker.showInfoWindow();
                            }
                            txtDeliveryboy.setVisibility(View.VISIBLE);
                            txtDeliveryboy.setText("" + mapinfo.getRiderName());
                            txtDstatus.setText("" + mapinfo.getRiderMsg());
                            txtDmobile.setVisibility(View.VISIBLE);
                            Glide.with(this).load(APIClient.baseUrl + "/" + mapinfo.getRiderImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgDelivery);

                            break;
                        case 3:
                            lvlRestorent.setVisibility(View.GONE);
                            txtDeliveryboy.setVisibility(View.VISIBLE);
                            txtDmobile.setVisibility(View.VISIBLE);
                            txtDeliveryboy.setText("" + mapinfo.getRiderName());
                            txtDstatus.setText("" + mapinfo.getRiderMsg());
                            txtDmobile.setVisibility(View.VISIBLE);
                            Glide.with(this).load(APIClient.baseUrl + "/" + mapinfo.getRiderImg()).thumbnail(Glide.with(this).load(R.drawable.animationbg)).into(imgDelivery);
                            if (mMap != null && marker != null) {

                                animateMarkerToGB(marker, new LatLng(mapinfo.getRiderLats(), mapinfo.getRiderLongs()), new LatLngInterpolator.Spherical());

                            } else {

                                placePath = new MarkerOptions().position(new LatLng(mapinfo.getRiderLats(), mapinfo.getRiderLongs())).title("Path");
                                place1 = new MarkerOptions().position(new LatLng(mapinfo.getRiderLats(), mapinfo.getRiderLongs())).title(mapinfo.getRiderName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_deliveryboy));
                                place2 = new MarkerOptions().position(new LatLng(mapinfo.getCustAddressLat(), mapinfo.getCustAddressLong())).title(mapinfo.getCustAddressType()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_current));

                                new FetchURL(OrderTrackerActivity.this).execute(getUrl(placePath.getPosition(), place2.getPosition(), "driving"), "driving");
                                LatLng coordinate = new LatLng(mapinfo.getRiderLats(), mapinfo.getRiderLongs());
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
                                mMap.animateCamera(yourLocation);
                                marker = mMap.addMarker(place1);
                                mMap.addMarker(place2);
                                marker.showInfoWindow();
                            }

                            break;

                        default:
                            break;
                    }


                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    finish();

                }
            }

        } catch (Exception e) {
            Log.e("Error", "--" + e.toString());
        }
    }

    @OnClick({R.id.img_back, R.id.img_more, R.id.txt_canceloreder})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_canceloreder:
                orderCancle();
                break;

            case R.id.img_more:
                PopupMenu popup = new PopupMenu(this, imgMore);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menupopup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {
                    startActivity(new Intent(OrderTrackerActivity.this, OrderActivity.class).putExtra("oid", oID));
                    return true;
                });

                popup.show();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;



    }


    public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {

        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);

    }

    public interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class Spherical implements LatLngInterpolator {
            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to) {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);
                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6) {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;
                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);
                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }

            private double computeAngleBetween(double format, double fromLng, double toLat, double toLng) {
                // Haversine's formula
                double dLat = format - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(format) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.api_key);
        return url;
    }
}