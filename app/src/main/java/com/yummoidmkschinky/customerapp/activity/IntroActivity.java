package com.yummoidmkschinky.customerapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yummoidmkschinky.customerapp.R;
import com.yummoidmkschinky.customerapp.fragment.Info1Fragment;
import com.yummoidmkschinky.customerapp.fragment.Info2Fragment;
import com.yummoidmkschinky.customerapp.fragment.Info3Fragment;
import com.yummoidmkschinky.customerapp.model.User;
import com.yummoidmkschinky.customerapp.utiles.AutoScrollViewPager;
import com.yummoidmkschinky.customerapp.utiles.SessionManager;
import com.yummoidmkschinky.customerapp.utiles.Utility;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.flexibleIndicator)
    ExtensiblePageIndicator flexibleIndicator;

    SessionManager sessionManager;
    public static AutoScrollViewPager vpPager;
    MyPagerAdapter adapterViewPager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        vpPager = findViewById(R.id.vpPager);
        sessionManager = new SessionManager(IntroActivity.this);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        if (sessionManager.getBooleanData(SessionManager.login) || sessionManager.getBooleanData(SessionManager.intro)) {
            startActivity(new Intent(IntroActivity.this, HomeActivity.class));
            finish();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(IntroActivity.this)) {
                Toast.makeText(IntroActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
                Utility.enableLoc(IntroActivity.this);
            }
        }
        vpPager.startAutoScroll();
        vpPager.setInterval(3000);

        vpPager.setStopScrollWhenTouch(true);
        vpPager.setAdapter(adapterViewPager);
        ExtensiblePageIndicator extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(vpPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("sjlkj", "sjahdal");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.btn_location, R.id.lvl_login})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_location:
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(this)) {
                    Utility.enableLoc(IntroActivity.this);

                } else {
                    User user = new User();
                    user.setId("0");
                    sessionManager.setUserDetails("", user);
                    startActivity(new Intent(IntroActivity.this, MapActivity.class));
                    finish();

                }

                break;
            case R.id.lvl_login:
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int numItems = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return numItems;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Info1Fragment.newInstance();
                case 1:
                    return Info2Fragment.newInstance();
                case 2:
                    return Info3Fragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("page", "" + position);
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

    }


}
