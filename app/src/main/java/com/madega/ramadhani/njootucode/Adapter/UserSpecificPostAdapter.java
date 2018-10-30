package com.madega.ramadhani.njootucode.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.madega.ramadhani.njootucode.Models.PostModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/19/18.
 */

public class UserSpecificPostAdapter extends RecyclerView.Adapter<UserSpecificPostAdapter.ItemHolder> {
    List<PostModel> mMyPost=new ArrayList<>();
    Context context;

    public UserSpecificPostAdapter(Context context,List<PostModel> mMyPost ) {
        this.mMyPost = mMyPost;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        mMyPost.size();

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void AttachData(){

        }
    }
}
