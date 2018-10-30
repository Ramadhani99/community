package com.madega.ramadhani.njootucode.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.madega.ramadhani.njootucode.Models.PostPublishModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class TestActivity extends AppCompatActivity implements PostAdapter.PostAdapterCallback  {
    private Fragment fragment,mHomeFragment;
    public static User logiuser;


    private static String TAG = "TestActivity";
    private PostPublishModel mPostPublishModel;



    private BottomNavigationView mBtnNav;
    private TextView mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        logiuser = SharedPreferenceHelper.getUSer(getBaseContext());


        if (savedInstanceState==null){
            mHomeFragment=new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pages,mHomeFragment)
                    .commit();
        }



        mTitle = findViewById(R.id.title);
        Typeface prateface = Typeface.createFromAsset(getAssets(), "font/Monoton-Regular.ttf");
        mTitle.setTypeface(prateface);




        mBtnNav = findViewById(R.id.buttomView);


        mBtnNav.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.home:
                    findViewById(R.id.navheader).setVisibility(View.VISIBLE);
                    if (mHomeFragment==null) {
                        mHomeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.pages, mHomeFragment)
                                .commit();
                        return true;
                    }else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.pages, mHomeFragment)
                                .commit();
                        return true;
                    }

                case R.id.profile:
                    findViewById(R.id.navheader).setVisibility(View.GONE);
                    fragment=new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();
                    return true;

                case R.id.chart:
                    findViewById(R.id.navheader).setVisibility(View.VISIBLE);
                    fragment=new MyPostFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pages,fragment)
                            .commit();

                    return true;


                case R.id.setting:
                    return true;

                case R.id.community:
                    findViewById(R.id.navheader).setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                mPostPublishModel = new Gson().fromJson(result, PostPublishModel.class);
                createPost();


            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else {

        }
    }

    private  void createPost(){

        AsyncHttpClient createpost=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("token",mPostPublishModel.getUser_token());
        params.put("content",mPostPublishModel.getTextPost());

        //Log.e(TAG,ImgUrl);
        try {

            if(mPostPublishModel.getImgPost().length()>5){
                params.put("photo",new File(mPostPublishModel.getImgPost()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG,"ERROR upload file "+ e.getMessage());

        }

        createpost.post(StaticInformation.PUBLISH_POST, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG,""+statusCode+""+responseString);
                Toasty.error(getBaseContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"ERROR "+statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toasty.success(getBaseContext(),"post published", Toast.LENGTH_SHORT).show();

                Log.e(TAG,"The result are \n"+responseString);


            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    @Override
    public void sendObject(PostModel postModel) {
        //Toasty.success(getContext(),postModel.getPost(),Toast.LENGTH_SHORT).show();





        String url_api;
        Log.e(TAG,postModel.isLike()+"");

        AsyncHttpClient likepost=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("token",logiuser.getToken());
        params.put("post_id",postModel.getId());
        if (postModel.isLike()){
            url_api=StaticInformation.UNLIKE_POST;
        }
        else {
            url_api=StaticInformation.LIKE_POST;
        }
        likepost.get(url_api,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Toasty.error(getContext(),responseString,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray responze  = new JSONArray(responseString);

                    JSONObject object = responze.getJSONObject(0);
                    if (object.getBoolean("status") ){



                    }
                    else if (object.getBoolean("status") ){

                    }
                    else {
                        // Toasty.error(getContext(),responseString,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });



    }

    private void createNotification(int nId, int iconRes, String title, String body) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder.build());
    }
}
