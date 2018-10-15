package com.madega.ramadhani.njootucode.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Adapter.FriendSuggestionAdapter;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.FriendListModel;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.ApplicationDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.fotoapparat.hardware.orientation.Orientation;

/**
 * Created by root on 9/22/18.
 */

public class ProfileFragment extends Fragment  {

    private static String TAG = "ProfileFragment";

    private ImageView mCoverPhoto,mprofilePhoto;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private List<FriendListModel> mListFiend=new ArrayList<>();
    private FriendListModel friendListModel;
    private FriendSuggestionAdapter mFriendSuggestionAdapter;








    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        mRecyclerView=v.findViewById(R.id.fiendrecyclerview);

        layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mFriendSuggestionAdapter=new FriendSuggestionAdapter(getContext(),mListFiend);

        mRecyclerView.setAdapter(mFriendSuggestionAdapter);
        setData();


        mCoverPhoto=v.findViewById(R.id.cover_photo);



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


}
