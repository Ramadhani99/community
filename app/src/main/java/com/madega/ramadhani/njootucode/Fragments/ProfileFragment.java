package com.madega.ramadhani.njootucode.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Adapter.FriendSuggestionAdapter;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.FriendListModel;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by root on 9/22/18.
 */

public class ProfileFragment extends Fragment  {

    private static String TAG = "ProfileFragment";

    private ImageView mCoverPhoto,mprofilePhoto;
    private RecyclerView mFriendRecyclerView,mPostRecyclerView;
    private LinearLayoutManager layoutManager,mPostLayout;
    private List<FriendListModel> mListFiend=new ArrayList<>();
    private FriendListModel friendListModel;
    private FriendSuggestionAdapter mFriendSuggestionAdapter;
    private PostAdapter mPostAdapter;
    private List<PostModel> list_of_postModel = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    private static User logiuser;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        mFriendRecyclerView=v.findViewById(R.id.fiendrecyclerview);
        mPostRecyclerView=v.findViewById(R.id.postrecyclerview);

        SnapHelper snapHelper=new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mFriendRecyclerView);

        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mPostLayout=new LinearLayoutManager(getContext());
       
        mFriendRecyclerView.setLayoutManager(layoutManager);


        mPostLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mPostRecyclerView.setLayoutManager(mPostLayout);
         //initilze adapter
        mPostAdapter=new PostAdapter(getContext(),list_of_postModel);

        mPostRecyclerView.setAdapter(mPostAdapter);

        getData();

        mFriendSuggestionAdapter=new FriendSuggestionAdapter(getContext(),mListFiend);


        mFriendRecyclerView.setAdapter(mFriendSuggestionAdapter);
        setData();
        getUserProfileInfo();


        logiuser=SharedPreferenceHelper.getUSer(getContext());


       // mCoverPhoto=v.findViewById(R.id.cover_photo);
        mprofilePhoto=v.findViewById(R.id.profile_photo);
        //Glide.with(getContext()).load(logiuser.getProfileImgaePath()).into(mCoverPhoto);
        Glide.with(getContext()).load(logiuser.getProfileImgaePath()).into(mprofilePhoto);



        return v;
    }

    private void setData(){
        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        friendListModel=new FriendListModel();
        mListFiend.add(friendListModel);

        mFriendSuggestionAdapter.notifyDataSetChanged();
    }

    private void getData() {

        // Toasty.info(getContext(),"current page "+page, Toast.LENGTH_SHORT).show();


        AsyncHttpClient client = new AsyncHttpClient();
        logiuser = SharedPreferenceHelper.getUSer(getContext());
        String mytoken = logiuser.getToken();
        RequestParams params = new RequestParams();
        params.put("token",mytoken);
        params.put("user_id",logiuser.getId());
        client.get(StaticInformation.USER_WALL, params, new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
              //  mProgressDialog.dismiss();






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



                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

                Log.e(TAG,"Failure "+responseBody+statusCode);

            }

            @Override
            public void onStart() {
                super.onStart();
//
//              mProgressDialog=new ProgressDialog(getActivity());
//              mProgressDialog.show();

            }

        });
    }
 public void getUserProfileInfo() {

     AsyncHttpClient client = new AsyncHttpClient();
     logiuser = SharedPreferenceHelper.getUSer(getContext());
     String mytoken = logiuser.getToken();
     RequestParams params = new RequestParams();
     params.put("token", mytoken);
     params.put("user_id", logiuser.getId());
     client.get(StaticInformation.USER_PROFILE, params, new TextHttpResponseHandler() {


         @Override
         public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            // mProgressDialog.dismiss();


             Log.e(TAG, "success " + statusCode);

             try {
                 JSONArray userProfileArray  = new JSONArray(responseBody);

                 //get object
                 JSONObject objectUser = userProfileArray.getJSONObject(0);

                 //get Arry inside object
                JSONArray userInfoArray= objectUser.getJSONArray("user");

                Log.e(TAG,userInfoArray.toString());

                //












             } catch (Exception e) {
                 Log.e(TAG, e.getMessage());

             }

         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

             Log.e(TAG, "getUserProfileFunction Failure " + responseBody + statusCode + "\n"+logiuser.getId());

         }

         @Override
         public void onStart() {
             super.onStart();

         }

     });
 }

}
