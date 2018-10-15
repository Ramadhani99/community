package com.madega.ramadhani.njootucode.Adapter;


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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostWithCommentsActivity;
import com.madega.ramadhani.njootucode.Activity.ViewPhotoLayoutActivity;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Fragments.HomeFragment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

import static com.madega.ramadhani.njootucode.Activity.PostWithCommentsActivity.POST;


/**
 * Created by root on 9/14/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Itemholder> {
    private  static boolean isliked;

    private static PostModel mypostmodel;
    public PostAdapterCallback adapterCallback;






    private static String TAG="PostAdapter";
    private List<PostModel> mPostModels =new ArrayList<>();
    private  Context context;

    public PostAdapter(Context context, List<PostModel> postModels,HomeFragment fragment ) {

        this.mPostModels = postModels;
        this.context = context;
        this.adapterCallback = fragment;


    }
    public interface PostAdapterCallback{
        void sendObject(PostModel postModel);
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

        protected  int position;

        private  User LOGINUSER=SharedPreferenceHelper.getUSer(context);

        private PostModel postModel;


        private TextView mComments,mShare,mPostername,mDate,mPostText,mTotatlike;
        private ImageView mLikes,mProfileImage,mPostImage,mBtnMore,mDefaultImage,mUserImage;
        private ProgressBar mImageProgressBar;
        private View mCard,mControl,mWrite_post,mPostPublish,mPostLayout;
        private TextView mPdfView;







        public Itemholder(View itemView) {
            super(itemView);
            mWrite_post=itemView.findViewById(R.id.write_post);

            mTotatlike=itemView.findViewById(R.id.total_like);
            mLikes=itemView.findViewById(R.id.likes);
            mLikes.setOnClickListener(this);

            mPostLayout=itemView.findViewById(R.id.post_layout);
            mPostLayout.setOnClickListener(this);

            mPdfView=itemView.findViewById(R.id.pdfView);



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

            Typeface robot=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            mPostText.setTypeface(robot);


        }
        public void SaveData(PostModel postModel, int position){
            mPdfView.setVisibility(View.GONE);
            position=position;
            Log.e(TAG, postModel.getPost());
            Log.e(TAG,""+postModel.ATTACHMENT_TYPE);

            if (postModel.getPostImage().length()<3){
                mCard.setVisibility(View.GONE);
                mControl.setVisibility(View.VISIBLE);
                mDefaultImage.setVisibility(View.GONE);

                Log.e(TAG,postModel.getPostImage());


            }
            else{
                if (postModel.ATTACHMENT_TYPE==1) {
                    mCard.setVisibility(View.VISIBLE);
                    mControl.setVisibility(View.GONE);
                    mDefaultImage.setVisibility(View.VISIBLE);
                    Log.e(TAG, postModel.getPostImage());

                    Glide.with(context)
                            .load(postModel.getPostImage())
                            .listener(new RequestListener<Drawable>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    mImageProgressBar.setVisibility(View.GONE);
                                    mControl.setVisibility(View.GONE);
                                    mDefaultImage.setVisibility(View.GONE);
                                    mPostImage.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .into(mPostImage);
                }
                else if (postModel.ATTACHMENT_TYPE==2){
                    mPostImage.setVisibility(View.GONE);
                    mControl.setVisibility(View.VISIBLE);
                    mCard.setVisibility(View.GONE);
                    mDefaultImage.setVisibility(View.GONE);
                    mPdfView.setVisibility(View.VISIBLE);
                    mPdfView.setText(postModel.getPostImage());


                }
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

            mPostername.setText(postModel.getPublisherName());

            mPostText.setText(postModel.getPost());
            Linkify.addLinks(mPostText,Linkify.WEB_URLS);



            LikePost(postModel.isLike());
            this.postModel = postModel;



        }
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.likes:
                    if (postModel.isLike()){
                        adapterCallback.sendObject(postModel);
                        postModel.setLike(false);
                        postModel.setLikes(postModel.getLikes()-1);
                        LikePost(postModel.isLike());
                        mTotatlike.setText(""+postModel.getLikes());

                    }else{
                        adapterCallback.sendObject(postModel);

                        postModel.setLike(true);
                        postModel.setLikes(postModel.getLikes()+1);
                        LikePost(postModel.isLike());
                        mTotatlike.setText( postModel.getLikes()+"");


                    }

                    Log.e(TAG,"hii hapa"+postModel);

                    break;
                case R.id.comments:
                    Log.e(TAG,"hii hapa"+postModel.getPostname());
                    OpenPost();
                    break;
                case R.id.btnmore:
                     PopUp();
                    break;
                case R.id.post_image:
                    Intent showImage=new Intent(context,ViewPhotoLayoutActivity.class);
                   showImage.putExtra("image",postModel.getPostImage());
                    Log.e(TAG,"hii hapa"+postModel.getPostImage());
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

            if (LOGINUSER.getId()!=postModel.getPublisherId()){
                popup.getMenu().findItem(R.id.delete).setVisible(false);
            }


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
                        case R.id.delete:
                            DeletePost();

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

        }

        private void LikePost(Boolean test){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (test) {
                    mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp, context.getTheme()));

                } else {
                    mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, context.getTheme()));

                }
            }
            else{

                if (test){
                    mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));


                }
                else {
                    mLikes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));

                }

            }

        }
        private void DeletePost(){

            AsyncHttpClient deletepost=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("token",LOGINUSER.getToken());
            params.put("post_id",postModel.getId());
            deletepost.get(StaticInformation.DELETE_POST,params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toasty.error(context,responseString,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toasty.success(context,"delete successfully", Toast.LENGTH_SHORT).show();

                    mPostModels.remove(position);
                     notifyItemRemoved(position);
                     notifyItemRangeRemoved(position,getItemCount());
                     notifyDataSetChanged();



                }
            });


        }

    }

}
