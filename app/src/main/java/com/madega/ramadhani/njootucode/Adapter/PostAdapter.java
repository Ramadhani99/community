package com.madega.ramadhani.njootucode.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Activity.PostWithCommentsActivity;
import com.madega.ramadhani.njootucode.Activity.ViewPhotoLayoutActivity;
import com.madega.ramadhani.njootucode.Fragments.HomeFragment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;


import java.util.ArrayList;
import java.util.List;

import static com.madega.ramadhani.njootucode.Activity.PostWithCommentsActivity.POST;


/**
 * Created by root on 9/14/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Itemholder> {
    private  boolean test=false;




    private static String TAG="PostAdapter";
    private List<PostModel> mPostModels =new ArrayList<>();
    private  Context context;


    public PostAdapter(Context context,List<PostModel> postModels) {
        this.mPostModels = postModels;
        this.context = context;


    }




    @Override
    public Itemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_contents,parent,false) ;
        return new Itemholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Itemholder holder, int position) {

        holder.SaveData(mPostModels.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mPostModels.size();
    }



    public class Itemholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PostModel postModel;

        private TextView mComments,mShare,mPostername,mDate,mPostText,mTotatlike;
        private ImageView mLikes,mProfileImage,mPostImage,mBtnMore,mDefaultImage,mUserImage;
        private ProgressBar mImageProgressBar;
        private View mCard,mControl,mWrite_post,mPostPublish,mPostLayout;



        public Itemholder(View itemView) {
            super(itemView);
            mWrite_post=itemView.findViewById(R.id.write_post);

            mTotatlike=itemView.findViewById(R.id.total_like);
            mLikes=itemView.findViewById(R.id.likes);
            mLikes.setOnClickListener(this);

            mPostLayout=itemView.findViewById(R.id.post_layout);
            mPostLayout.setOnClickListener(this);



            mComments=itemView.findViewById(R.id.comments);
            mComments.setOnClickListener(this);

            mBtnMore=itemView.findViewById(R.id.btnmore);
            mBtnMore.setOnClickListener(this);


            mShare=itemView.findViewById(R.id.share);

            mPostername=itemView.findViewById(R.id.name);
            Typeface awasomeFont = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
            mPostername.setTypeface(awasomeFont);


            mDate=itemView.findViewById(R.id.date);
            mProfileImage=itemView.findViewById(R.id.profile_image);

            mPostImage=itemView.findViewById(R.id.post_image);
            mPostImage.setOnClickListener(this);

            mCard=itemView.findViewById(R.id.cardpostImage);
            mPostText=itemView.findViewById(R.id.posttext);
            mPostText.setOnClickListener(this);

            mControl=itemView.findViewById(R.id.imageControl);
            mImageProgressBar=itemView.findViewById(R.id.imageProgressBar);
            mDefaultImage=itemView.findViewById(R.id.default_image);

           // mUserImage=itemView.findViewById(R.id.my_image);

           // mPostPublish=itemView.findViewById(R.id.publish_post);
           // mPostPublish.setOnClickListener(this);

            Typeface robot=Typeface.createFromAsset(context.getAssets(),"font/Merienda-Regular.ttf");
            mPostText.setTypeface(robot);


        }
        public void SaveData(PostModel postModel, int position){
            Log.e(TAG, postModel.getPost());
            mDefaultImage.setVisibility(View.VISIBLE);
            mControl.setVisibility(View.VISIBLE);

            if (postModel.getPostImage().length()<5){
                mCard.setVisibility(View.GONE);
                mControl.setVisibility(View.VISIBLE);
                mDefaultImage.setVisibility(View.GONE);


            }
            else{
                mCard.setVisibility(View.VISIBLE);
                mControl.setVisibility(View.GONE);

                Glide.with(context)
                        .load(postModel.getPostImage())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                mPostImage.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mImageProgressBar.setVisibility(View.GONE);
                                mControl.setVisibility(View.GONE);
                                mDefaultImage.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mPostImage);
            }
            Glide.with(context)
                    .load(postModel.getPosterImage())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            return false;
                        }
                    })
                    .into(mProfileImage);
            mComments.setText(postModel.getComments()+" Comments");
            mDate.setText(postModel.getDate());
            mTotatlike.setText(""+postModel.getLikes());

            mPostername.setText(postModel.getUser());

            mPostText.setText(postModel.getPost());
            Linkify.addLinks(mPostText,Linkify.WEB_URLS);

            this.postModel = postModel;


        }
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.likes:


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        if (test){
                            mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, context.getTheme()));
                            test=false;
                        }
                        else{
                            mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp, context.getTheme()));
                            test=true;
                        }

                    } else {
                        if (test){
                            mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                            test=false;
                        }
                        else{
                            mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                            test=true;
                        }
                    }
                    break;
                case R.id.comments:
                    OpenPost();
                    break;
                case R.id.btnmore:
                     PopUp();
                    break;
                case R.id.post_image:
                    Intent showImage=new Intent(context,ViewPhotoLayoutActivity.class);
                   showImage.putExtra("image",postModel.getPostImage());
                    context.startActivity(showImage);
                    break;
                case R.id.posttext:
                    OpenPost();
                    break;

                default:
                    break;
            }

        }
        private void PopUp(){
            PopupMenu popup = new PopupMenu(context, mBtnMore);

            popup.inflate(R.menu.option_menu);

            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.share:
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = mPostText.getText().toString() + "\nwww.olbongo.com";
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            context.startActivity(Intent.createChooser(sharingIntent, "Share the Link"));
                            break;
                        default:
                            break;


                    }
                    return false;
                }
            });

            popup.show();
        }
        private void OpenPost(){
            Intent openPost=new Intent(context,PostWithCommentsActivity.class);
            openPost.putExtra(POST,new Gson().toJson(postModel));
            context.startActivity(openPost);
            ((Activity)context).finish();
        }
    }
}
