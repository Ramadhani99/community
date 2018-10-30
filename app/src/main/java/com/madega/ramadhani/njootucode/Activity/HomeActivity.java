package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="HomeActivity";


    private User logiuser;
    private PostModel mPostModel;
    private List<PostModel> list_of_postModel =new ArrayList<>();
    private PostAdapter mPostAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View mTryagain,mTryagainView,mProgressBar,mCreatePostBtn;
    private SmoothProgressBar mSmoothProgressBar;
    private BottomNavigationView mBtnNav;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mRecyclerView=findViewById(R.id.recyclerview);
        mCreatePostBtn=findViewById(R.id.create_postbtn);
        mCreatePostBtn.setOnClickListener(this);

        mSmoothProgressBar=findViewById(R.id.progress);


        linearLayoutManager=new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mPostAdapter=new PostAdapter(this, list_of_postModel);
        mRecyclerView.setAdapter(mPostAdapter);

        ReloadData();

        mTryagain=findViewById(R.id.tryagain);
        mTryagain.setOnClickListener(this);

        mTitle=findViewById(R.id.title);
        Typeface prateface=Typeface.createFromAsset(getAssets(),"font/Monoton-Regular.ttf");
        mTitle.setTypeface(prateface);


        mTryagainView=findViewById(R.id.no_connection_view);
        mProgressBar=findViewById(R.id.tyr_again_progressbar);

        mBtnNav=findViewById(R.id.buttomView);

        mBtnNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        //startActivity(new Intent(HomeActivity.this,PostPublishLayoutActivity.class));
                        break;
                    case R.id.profile:
                        break;
                    case R.id.chart:
                        break;

                    case R.id.setting:
                        break;
                    case R.id.share:
                        default:
                            break;
                }
            }
        });




//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.buttomView);
//       BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }
    @Override
    protected void onStart() {
        super.onStart();



    }


    private void ReloadData(){
        AsyncHttpClient client=new AsyncHttpClient();
        Intent myintent=getIntent();
        logiuser= SharedPreferenceHelper.getUSer(getBaseContext());
        String mytoken=logiuser.getToken();
        RequestParams params=new RequestParams("token",mytoken);
        client.get(StaticInformation.POST,params,new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                mTryagainView.setVisibility(View.GONE);
               mProgressBar.setVisibility(View.GONE);
                mSmoothProgressBar.setVisibility(View.GONE);
                try {
                    Log.e(TAG,mytoken);
                    JSONArray questionArray=new JSONArray(responseBody);
                    for (int i=0;i<questionArray.length();i++){
                        JSONObject object=questionArray.getJSONObject(i);
                        PostModel postModel =new PostModel();
                        if (object.optString("content").equals(null)){
                            postModel.setPost("No massages");
                        }
                        else{
                        postModel.setPost(object.optString("content"));
                        }

                        postModel.setDate(object.getString("published_at"));


                        JSONObject postername=new JSONObject(object.optString("publisher"));

                        postModel.setPublisherName(postername.optString("display_name"));
                        postModel.setPosterImage(postername.optString("dp"));
                        if (object.has("images")){

                            if (!object.isNull("images")){
                                JSONArray Array=new JSONArray(object.optString("images"));
                               if (Array.length()>0) {
                                   JSONObject image = Array.getJSONObject(0);
                                   postModel.setPostAttachment(image.optString("photo"));
                               }else{
                                   postModel.setPostAttachment("1");
                               }







                            }
                            else{
                                if (!object.isNull("docs")){
                                    JSONArray Array=new JSONArray(object.optString("docs"));
                                    if (Array.length()>0) {
                                        JSONObject doc = Array.getJSONObject(0);

                                        Log.e(TAG,doc.optString("doc"));
                                    }else{
                                        postModel.setPostAttachment("1");
                                    }
                                }
                            }
                        }

                       // JSONObject imageObject=object.getJSONObject("images");
 //                       JSONArray arrayImages=object.getJSONArray("images");
//                        for(int j=0;i<arrayImages.length();i++){
//                            JSONObject image=arrayImages.getJSONObject(i);

//                            Log.e(TAG,postModel.getPostAttachment());
//                        }









                        list_of_postModel.add(postModel);
                        Log.e(TAG, postModel.getPost()+ "" + postModel.getDate()+" "+ postModel.getPublisherName() );
                       //Log.e(TAG,  postModel.getPostAttachment());
                        mPostAdapter.notifyDataSetChanged();

                    }


                }
                catch (Exception e){
                    Log.e(TAG,e.getMessage());

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

                //Toasty.success(getBaseContext(),"request time out").show();
                mTryagainView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mSmoothProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onStart() {
                super.onStart();
                mSmoothProgressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
          case R.id.create_postbtn:
               startActivity(new Intent(HomeActivity.this,PostPublishLayoutActivity.class));
               break;
          case R.id.tryagain:
               // mProgressBar.setVisibility(View.VISIBLE);
                list_of_postModel.clear();
                ReloadData();
                break;
                default:
                    break;
        }
    }

}
