package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemReselectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Fragments.CommunityFragment;
import com.madega.ramadhani.njootucode.Fragments.HomeFragment;
import com.madega.ramadhani.njootucode.Fragments.MyPostFragment;
import com.madega.ramadhani.njootucode.Fragments.ProfileFragment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class TestActivity extends AppCompatActivity  {
    private Fragment fragment;


    private static String TAG = "TestActivity";


    private BottomNavigationView mBtnNav;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

       fragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pages,fragment)
                .commit();


        mTitle = findViewById(R.id.title);
        Typeface prateface = Typeface.createFromAsset(getAssets(), "font/Monoton-Regular.ttf");
        mTitle.setTypeface(prateface);


        mBtnNav = findViewById(R.id.buttomView);


        mBtnNav.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.home:
                    fragment=new HomeFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();
                    return true;

                case R.id.profile:
                    fragment=new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();
                    return true;

                case R.id.chart:
                    fragment=new MyPostFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();

                    return true;


                case R.id.setting:
                    return true;

                case R.id.community:
                    fragment=new CommunityFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();
                    return true;


                default:
                    return true;



            }

        });


//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.buttomView);
//       BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


}
