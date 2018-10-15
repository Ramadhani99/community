package com.madega.ramadhani.njootucode.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Helper.EndlessRecyclerViewScrollListener;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.PostPublishModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.ApplicationDatabase;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by root on 9/22/18.
 */

public class HomeFragment extends Fragment implements View.OnClickListener,PostAdapter.PostAdapterCallback {


    private static String TAG = "HomeFragment";
    private PostPublishModel mPostPublishModel;

    private EndlessRecyclerViewScrollListener scrollListener;

    public static User logiuser;


    private static  Parcelable mlistState;

    private PostModel mPostModel;
    private List<PostModel> list_of_postModel = new ArrayList<>();
    private PostAdapter mPostAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View mTryagain, mTryagainView, mProgressBar, mCreatePostBtn;

    private SwipeRefreshLayout mRefresher;

    private ApplicationDatabase Db;


    private SmoothProgressBar mSmoothProgressBar,mPublishPostProgress;





    @Override
    public void onStart() {
        Db = ApplicationDatabase.getApplicationDatabase(getContext());
        super.onStart();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                for (PostModel getmodel : Db.postdao().getAllpost()) {
                    if (list_of_postModel.isEmpty() || list_of_postModel.size()<25) {
                        list_of_postModel.add(getmodel);
                    }

                }


                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mPostAdapter.notifyDataSetChanged();
            }
        }.execute(null, null);







    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerview);
        mCreatePostBtn = v.findViewById(R.id.create_postbtn);
        mCreatePostBtn.setOnClickListener(this);

        mSmoothProgressBar = v.findViewById(R.id.progress);
        mPublishPostProgress = v.findViewById(R.id.progressup);


        linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostAdapter = new PostAdapter(getContext(), list_of_postModel,HomeFragment.this);
        mRecyclerView.setAdapter(mPostAdapter);

        getData(0);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                getData(page+1);




            }
        };
       mRefresher= v.findViewById(R.id.swiperefresh);
       mRefresher.setOnRefreshListener(() -> getData(1));



        mRecyclerView.addOnScrollListener(scrollListener);


        mTryagain = v.findViewById(R.id.tryagain);
        mTryagain.setOnClickListener(this);

        mTryagainView = v.findViewById(R.id.no_connection_view);
        mProgressBar = v.findViewById(R.id.tyr_again_progressbar);

        return v;
    }




    private void getData(int page) {

       // Toasty.info(getContext(),"current page "+page, Toast.LENGTH_SHORT).show();


        AsyncHttpClient client = new AsyncHttpClient();
        logiuser = SharedPreferenceHelper.getUSer(getContext());
        String mytoken = logiuser.getToken();
        RequestParams params = new RequestParams();
        params.put("token",mytoken);
        params.put("page",page);
        client.get(StaticInformation.POST, params, new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                mSmoothProgressBar.setVisibility(View.GONE);
                mRefresher.setRefreshing(false);

                if (page==0||page==1){
                    list_of_postModel.clear();
                }



                Log.e(TAG,"success "+statusCode);

                try {
                    //Log.e(TAG, mytoken);
                    JSONArray questionArray = new JSONArray(responseBody);
                    for (int i = 0; i < questionArray.length(); i++) {
                        JSONObject object = questionArray.getJSONObject(i);
                        PostModel postModel = new PostModel();
                        postModel.setId(object.optInt("id"));
                        if (object.optString("content").equals(null)) {
                            postModel.setPost("No massages");
                        } else {
                            postModel.setPost(object.optString("content"));
                        }

                        postModel.setDate(object.getString("published_at"));
                        postModel.setLikes(object.getInt("total_likes"));
                        postModel.setComments(object.getInt("total_commenets"));
                        postModel.setLike(object.optBoolean("is_liked"));


                        JSONObject publisher_details = new JSONObject(object.optString("publisher"));

                        postModel.setPublisherName(publisher_details.optString("display_name"));
                        postModel.setPosterImage(publisher_details.optString("dp"));
                        postModel.setPublisherId(publisher_details.optInt("id"));

                        if (object.has("images")) {

                            if (!object.isNull("images")) {
                                JSONArray Array = new JSONArray(object.optString("images"));
                                if (Array.length() > 0) {
                                    postModel.ATTACHMENT_TYPE=1;
                                    JSONObject image = Array.getJSONObject(0);
                                    postModel.setPostImage(image.optString("photo"));
                                } else {
                                    if (!object.isNull("docs")){
                                        JSONArray Array2=new JSONArray(object.optString("docs"));
                                        if (Array2.length()>0) {
                                            postModel.ATTACHMENT_TYPE=2;
                                            JSONObject doc = Array2.getJSONObject(0);
                                            postModel.setPostImage(doc.optString("doc"));
                                            Log.e(TAG,doc.optString("doc"));
                                        }else{
                                            postModel.setPostImage("1");
                                        }
                                    }

                                }


                            } else {
                                postModel.setPostImage("2");
                            }
                        }
                        else {
                            postModel.setPostImage("2");
                        }


                        list_of_postModel.add(postModel);

                        mPostAdapter.notifyDataSetChanged();

                    }

             if (page==0 || page==1) {

                 new AsyncTask<Void, Void, Void>() {
                     @Override
                     protected Void doInBackground(Void... voids) {

                         Db.postdao().DeleteAllfromPost();
                         for (PostModel model : list_of_postModel) {
                             Db.postdao().InsertPost(model);
                         }
                         for (PostModel getmodel : Db.postdao().getAllpost()) {
                             Log.e(TAG, "From Database" + getmodel.getPublisherName());

                         }


                         return null;

                     }
                 }.execute(null, null);
             }


                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

                mSmoothProgressBar.setVisibility(View.GONE);
                Log.e(TAG,"Failure "+responseBody+statusCode);

            }

            @Override
            public void onStart() {
                super.onStart();
                if (page>1){
                    mSmoothProgressBar.setVisibility(View.VISIBLE);
                }


               //

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_postbtn:
                Intent myintent=new Intent(getActivity(), PostPublishLayoutActivity.class);
              // mlistState= linearLayoutManager.onSaveInstanceState();

                getActivity(). startActivityForResult(myintent,1);

                break;
            case R.id.tryagain:
                list_of_postModel.clear();
                break;
            default:
                break;
        }
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



    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toasty.success(getContext(),"inafika hapa",Toast.LENGTH_SHORT).show();
        getActivity();

        if (requestCode == 1) {
            String result=data.getStringExtra("result");
            mPostPublishModel=new Gson().fromJson(result,PostPublishModel.class);
            if(resultCode == Activity.RESULT_OK){
                createPost();

                Toasty.success(getContext(),"Fragment"+mPostPublishModel.getUser_token(), Toast.LENGTH_SHORT).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        else {
            Toasty.error(getContext(),"hiooo"+mPostPublishModel.getUser_token(), Toast.LENGTH_SHORT).show();
        }
    }
    private void createPost(){

        AsyncHttpClient createpost=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("token",mPostPublishModel.getUser_token());
        params.put("content",mPostPublishModel.getTextPost());

            //Log.e(TAG,ImgUrl);
            try {

                if(mPostPublishModel.getImgPost() != null){
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
                Toasty.error(getContext(),""+statusCode+""+responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toasty.success(getContext(),responseString, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onStart() {
                super.onStart();
                mPublishPostProgress.setVisibility(View.VISIBLE);

            }
        });
    }


}

