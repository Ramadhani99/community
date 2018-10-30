package com.madega.ramadhani.njootucode.Fragments;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Helper.ConnectionAvailable;
import com.madega.ramadhani.njootucode.Helper.EndlessRecyclerViewScrollListener;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.PostPublishModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.ApplicationDatabase;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by root on 9/22/18.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{


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

    if (!ConnectionAvailable.hasConnection(getContext())){
        Toasty.info(getContext(),"No internet connection",Toast.LENGTH_SHORT).show();

    }
    else {
        ReloadData(0);

    }








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
        mPostAdapter = new PostAdapter(getContext(), list_of_postModel);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());




        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                ReloadData(page+1);




            }
        };
       mRefresher= v.findViewById(R.id.swiperefresh);
       mRefresher.setOnRefreshListener(() -> ReloadData(1));



        mRecyclerView.addOnScrollListener(scrollListener);


        mTryagain = v.findViewById(R.id.tryagain);
        mTryagain.setOnClickListener(this);

        mTryagainView = v.findViewById(R.id.no_connection_view);
        mProgressBar = v.findViewById(R.id.tyr_again_progressbar);

        return v;
    }




    private void ReloadData(int page) {

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
                mPublishPostProgress.setVisibility(View.GONE);
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
                                    postModel.setPostAttachment(image.optString("photo"));
                                } else {
                                    if (!object.isNull("docs")){
                                        JSONArray Array2=new JSONArray(object.optString("docs"));
                                        if (Array2.length()>0) {
                                            postModel.ATTACHMENT_TYPE=2;
                                            JSONObject doc = Array2.getJSONObject(0);
                                            postModel.setPostAttachment(doc.optString("doc"));
                                            Log.e(TAG,doc.optString("doc"));
                                        }else{
                                            postModel.setPostAttachment("1");
                                        }
                                    }else if(!object.isNull("videos")) {

                                      JSONArray listOfVideo=new JSONArray(object.optString("video"));
                                      if (listOfVideo.length()>0){
                                          postModel.ATTACHMENT_TYPE=3;
                                          JSONObject mVideo=listOfVideo.getJSONObject(0);
                                          postModel.setPostAttachment(mVideo.optString("video"));

                                      }


                                    }

                                }


                            } else {
                                postModel.setPostAttachment("2");
                            }
                        }
                        else {
                            postModel.setPostAttachment("2");
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
                mPublishPostProgress.setVisibility(View.GONE);

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
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }

    @Override
    public void onResume() {
        super.onResume();

    }



}

