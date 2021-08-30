package com.yummoidmkschinky.customerapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.model.Home;
import com.yummoidmkschinky.customerapp.model.MyAddress;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.retrofit.APIClient;
import com.yummoidmkschinky.customerapp.retrofit.GetResult;
import com.yummoidmkschinky.customerapp.utiles.CustPrograssbar;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;

import static com.yummoidmkschinky.customerapp.utiles.SessionManager.login;
import static com.yummoidmkschinky.customerapp.utiles.Utility.newAddress;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GetResult.MyListener {

    @BindView(R.id.layoutMap)
    LinearLayout layoutMap;

    @BindView(R.id.locationMarker)
    LinearLayout locationMarker;
    @BindView(R.id.locationMarkertext)
    LinearLayout locationMarkertext;
    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.imageMarker)
    ImageView imageMarker;
    @BindView(R.id.txt_society)
    TextView txtSociety;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_change)
    TextView txtChange;
    @BindView(R.id.btn_location)
    TextView btnLocation;
    GoogleMap mMap;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;

    String userAddress = "";

    String userid = "0";
    String newuser = "user";
    String aid = "0";
    String atype = "Home";
    String hno = "";
    String landmark = "";


    double mLatitude = 0.0;
    double mLongitude = 0.0;

    double currentLatitude;
    double currentLongitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    Bundle addressBundle;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    private MarkerOptions place1;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        custPrograssbar = new CustPrograssbar();
        ButterKnife.bind(this);
        sessionManager = new SessionManager(MapActivity.this);
        user = sessionManager.getUserDetails("");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pulsator.start();
        custPrograssbar.prograssCreate(MapActivity.this);
        addressBundle = new Bundle();
        fusedLocationProviderClient = getFusedLocationProviderClient(this);
        getLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
            }
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                Log.e("lat", "-->" + locationResult.getLocations().get(0).getLatitude());
                Log.e("Lon", "-->" + locationResult.getLocations().get(0).getLongitude());
                //Location fetched, update listener can be removed
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };


        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {

                hno = getIntent().getStringExtra("hno");
                landmark = getIntent().getStringExtra("landmark");
                userid = getIntent().getStringExtra("userid");
                newuser = getIntent().getStringExtra("newuser");
                aid = getIntent().getStringExtra("aid");
                atype = getIntent().getStringExtra("atype");
                atype = getIntent().getStringExtra("atype");
//
//
                mLatitude = getIntent().getDoubleExtra("lat", 0);
                mLongitude = getIntent().getDoubleExtra("long", 0);

            }
        }
        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude");
            mLongitude = savedInstanceState.getDouble("longitude");
            userAddress = savedInstanceState.getString("userAddress");
            currentLatitude = savedInstanceState.getDouble("currentLatitude");
            currentLongitude = savedInstanceState.getDouble("currentLongitude");
        }
    }

    private void getLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        custPrograssbar.closePrograssBar();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();

            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.e("TAGAAAAAA", mMap.getCameraPosition().target.toString());
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                 
                    LatLng latLng = mMap.getCameraPosition().target;
                    Log.e("cc", "currLong: " + latLng.longitude);
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        if (addresses != null && !addresses.isEmpty()) {
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            String subareay = addresses.get(0).getSubLocality();
                            if (subareay != null && knownName != null) {
                                txtSociety.setText(subareay + "," + knownName);

                            } else if (subareay != null) {
                                txtSociety.setText(subareay);

                            } else if (knownName != null) {
                                txtSociety.setText(knownName);

                            }
                            txtAddress.setText("" + address);

                            btnLocation.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.clear();
                 


            }
        });

        
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            } else {
                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            }
        

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude;
        double longitude;
        if (mLatitude != 0.0 && mLongitude != 0.0) {
            latitude = mLatitude;
            longitude = mLongitude;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        Log.e("test", "-->");
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    @OnClick({R.id.txt_change, R.id.btn_location, R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_change:
                finish();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_location:
                if (sessionManager.getBooleanData(login) && newAddress == 1) {
                    bottonAddressadd();
                } else {
                    LatLng latLng = mMap.getCameraPosition().target;
                    MyAddress myAddress = new MyAddress();
                    myAddress.setLandmark(txtSociety.getText().toString());
                    myAddress.setAddress(txtAddress.getText().toString());
                    myAddress.setLatMap(String.valueOf(latLng.latitude));
                    myAddress.setLongMap(String.valueOf(latLng.longitude));
                    sessionManager.setAddress(myAddress);
                    startActivity(new Intent(MapActivity.this, HomeActivity.class));

                }
                break;

        }
    }


    public void bottonAddressadd() {
        LatLng latLng = mMap.getCameraPosition().target;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.address_add, null);
        mBottomSheetDialog.setContentView(rootView);

        TextView txtSociety = rootView.findViewById(R.id.txt_society);
        TextView txtFullaadress = rootView.findViewById(R.id.txt_fullaadress);
        TextView txtAddress = rootView.findViewById(R.id.txt_address);
        EditText edFlat = rootView.findViewById(R.id.ed_flat);
        EditText edAparment = rootView.findViewById(R.id.ed_aparment);
        TextView txtHome = rootView.findViewById(R.id.txt_home);
        TextView txtOffice = rootView.findViewById(R.id.txt_office);
        TextView txtAther = rootView.findViewById(R.id.txt_ather);
        TextView btnLocation = rootView.findViewById(R.id.btn_location);

        txtSociety.setText(this.txtSociety.getText().toString());
        txtFullaadress.setText(this.txtAddress.getText().toString());

        if (atype.equalsIgnoreCase("Home")) {
            txtHome.performClick();
        } else if (atype.equalsIgnoreCase("Office")) {
            txtOffice.performClick();
        } else if (atype.equalsIgnoreCase("Other")) {
            txtOffice.performClick();
        }

        txtHome.setOnClickListener(v -> {
            txtHome.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.white));
            atype = "Home";
        });
        txtOffice.setOnClickListener(v -> {
            txtOffice.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtAther.setBackground(getResources().getDrawable(R.drawable.box));
            txtHome.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.white));
            atype = "Office";
        });

        txtAther.setOnClickListener(v -> {
            txtAther.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtHome.setBackground(getResources().getDrawable(R.drawable.box));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.box));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtAther.setTextColor(getResources().getColor(R.color.white));
            atype = "Other";
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edFlat.getText().toString())) {
                    edFlat.setError("Enter House");
                    return;
                }

                if (TextUtils.isEmpty(edAparment.getText().toString())) {
                    edAparment.setError("Enter Apartment or Building or Landmark");
                    return;
                }

                custPrograssbar.prograssCreate(MapActivity.this);
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("uid", user.getId());
                    jsonObject.put("address", txtFullaadress.getText().toString());
                    jsonObject.put("houseno", edFlat.getText().toString());
                    jsonObject.put("apartment", edAparment.getText().toString());
                    jsonObject.put("type", atype);
                    jsonObject.put("lat_map", latLng.latitude);
                    jsonObject.put("long_map", latLng.longitude);
                    jsonObject.put("aid", aid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = APIClient.getInterface().addressAdd(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(MapActivity.this::callback);
                getResult.callForLogin(call, "1");

            }
        });

        mBottomSheetDialog.show();


    }


    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Home home = gson.fromJson(result.toString(), Home.class);
                if (home.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            }
        } catch (Exception e) {

        }
    }
}