package com.madega.ramadhani.njootucode.Fragments;

import android.content.Intent;
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

    private PostModel mPostModel;
    private List<PostModel> list_of_postModel = new ArrayList<>();
    private PostAdapter mPostAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View mTryagain, mTryagainView, mProgressBar, mCreatePostBtn;
    private SmoothProgressBar mSmoothProgressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerview);
        mCreatePostBtn = v.findViewById(R.id.create_postbtn);
        mCreatePostBtn.setOnClickListener(this);

        mSmoothProgressBar = v.findViewById(R.id.progress);


        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostAdapter = new PostAdapter(getContext(), list_of_postModel);
        mRecyclerView.setAdapter(mPostAdapter);
        getData();

        mTryagain = v.findViewById(R.id.tryagain);
        mTryagain.setOnClickListener(this);

        mTryagainView = v.findViewById(R.id.no_connection_view);
        mProgressBar = v.findViewById(R.id.tyr_again_progressbar);

        return v;
    }

    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();

        String mytoken = "4zt-37f40346d7b470d5d298:@olb:dnZ5";
        RequestParams params = new RequestParams("token", mytoken);
        client.get(StaticInformation.POST, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                mTryagainView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
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

                        postModel.setUser(postername.optString("display_name"));
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

                        // JSONObject imageObject=object.getJSONObject("images");
                        //                       JSONArray arrayImages=object.getJSONArray("images");
//                        for(int j=0;i<arrayImages.length();i++){
//                            JSONObject image=arrayImages.getJSONObject(i);

//                            Log.e(TAG,postModel.getPostImage());
//                        }


                        list_of_postModel.add(postModel);
                        Log.e(TAG, postModel.getPost() + "" + postModel.getDate() + " " + postModel.getUser());
                        //Log.e(TAG,  postModel.getPostImage());
                        mPostAdapter.notifyDataSetChanged();

                    }


                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());

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
        switch (v.getId()) {
            case R.id.create_postbtn:
                startActivity(new Intent(getActivity(), PostPublishLayoutActivity.class));
                break;
            case R.id.tryagain:
                // mProgressBar.setVisibility(View.VISIBLE);
                list_of_postModel.clear();
                getData();
                break;
            default:
                break;
        }
    }
}
