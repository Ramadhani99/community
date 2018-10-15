package com.madega.ramadhani.njootucode.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.ApplicationDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by root on 9/22/18.
 */

public class CommunityFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "CommunityFragment";

    private ApplicationDatabase Db;


    private SmoothProgressBar mSmoothProgressBar;

    @Override
    public void onStart() {
        super.onStart();
        Db=ApplicationDatabase
                .getApplicationDatabase(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.comment_layout, container, false);


        return v;
    }

    private void getData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

               for (PostModel post:Db.postdao().getAllpost()){
                   Log.e(TAG,post.getPostname());

               }
                return null;

            }
        } .execute(null,null);
        AsyncHttpClient client = new AsyncHttpClient();

        String mytoken = "4zt-37f40346d7b470d5d298:@olb:dnZ5";
        RequestParams params = new RequestParams("token", mytoken);
        client.get(StaticInformation.POST, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {

                mSmoothProgressBar.setVisibility(View.GONE);
                try {
                    Log.e(TAG, mytoken);
                    JSONArray questionArray = new JSONArray(responseBody);
                    for (int i = 0; i < questionArray.length(); i++) {
                        JSONObject object = questionArray.getJSONObject(i);
                        PostModel postModel = new PostModel();
                        if (object.optString("content").equals(null)) {
                            postModel.setPost("No massages");
                        } else {
                            postModel.setPost(object.optString("content"));
                        }

                        postModel.setDate(object.getString("published_at"));


                        JSONObject postername = new JSONObject(object.optString("publisher"));

                        postModel.setPublisherName(postername.optString("display_name"));
                        postModel.setPosterImage(postername.optString("dp"));
                        if (object.has("images")) {

                            if (!object.isNull("images")) {
                                JSONArray Array = new JSONArray(object.optString("images"));
                                if (Array.length() > 0) {
                                    JSONObject image = Array.getJSONObject(0);
                                    postModel.setPostImage(image.optString("photo"));
                                } else {
                                    postModel.setPostImage("1");
                                }


                            } else {
                                postModel.setPostImage("im null right now");
                            }
                        }




                        Log.e(TAG, postModel.getPost() + "" + postModel.getDate() + " " + postModel.getPublisherName());
                        //Log.e(TAG,  postModel.getPostImage());


                    }


                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

                //Toasty.success(getBaseContext(),"request time out").show();

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
        switch (v.getId()) {

            default:
                break;
        }
    }
}
