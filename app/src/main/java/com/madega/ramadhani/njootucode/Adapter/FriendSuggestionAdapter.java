package com.madega.ramadhani.njootucode.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madega.ramadhani.njootucode.Models.FriendListModel;
import com.madega.ramadhani.njootucode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/15/18.
 */

public class FriendSuggestionAdapter extends RecyclerView.Adapter<FriendSuggestionAdapter.ItemholderClass> {

    private List<FriendListModel>  friendListModels=new ArrayList<>();
    private Context context;

    public FriendSuggestionAdapter( Context context,List<FriendListModel> friendListModels) {
        this.friendListModels = friendListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemholderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_follow_layout,parent,false);
        return new ItemholderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemholderClass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friendListModels.size();
    }

    public class ItemholderClass extends ViewHolder{

        public ItemholderClass(View itemView) {
            super(itemView);
        }
    }
}
