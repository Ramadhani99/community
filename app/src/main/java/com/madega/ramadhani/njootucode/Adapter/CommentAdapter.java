package com.madega.ramadhani.njootucode.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.ViewPhotoLayoutActivity;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.Comment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by root on 9/18/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Itemholder> {
    private static String TAG="CommentAdapter";
    private List<Comment> comments=new ArrayList<>();
    private PostModel post;
    private Context context;

    public CommentAdapter(Context context,List<Comment> comments) {
        this.comments = comments;
        this.context = context;
        this.post=post;
    }

    @NonNull
    @Override
    public Itemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new Itemholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Itemholder holder, int position) {
         holder.SaveData(comments.get(position),position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class Itemholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private User LOGINUSER= SharedPreferenceHelper.getUSer(context);

        private Comment comment;
        private  int current_position;

       private TextView mOpenPost,mCommentView,mAuthorName,mLikes,mTotalCommments,mCommentedTime;

       private ImageView mOpenPostImage,mBtnmore,mDefaulImage,mAuthorImage;

        public Itemholder(View itemView) {

            super(itemView);

            //mOpenPostImage=itemView.findViewById(R.id.open_post_image);




            mAuthorName=itemView.findViewById(R.id.comenter);

            mAuthorImage=itemView.findViewById(R.id.my_image);
            mLikes=itemView.findViewById(R.id.count_like);

           // mCommentLayout=itemView.findViewById(R.id.comment_layout);



            mCommentView=itemView.findViewById(R.id.comenter_view);

            mCommentedTime=itemView.findViewById(R.id.date_comment);



            mBtnmore=itemView.findViewById(R.id.btnmore);
            mBtnmore.setOnClickListener(this);


        }
        public void SaveData(Comment comment,int position){
            current_position=position;

//            if (position<1){
//                post=comment.getPostModel();
//                mPostcard.setVisibility(View.VISIBLE);
//
//
//                //mDefaulImage.setVisibility(View.VISIBLE);
//
//               // mAllComment.setVisibility(View.VISIBLE);
//
//
//
//
//
//            }
//            else {
//                mPostcard.setVisibility(View.GONE);
//                mAllComment.setVisibility(View.GONE);
//            }
            if (comment.isHasComment()){


                Log.e(TAG,comment.getBody());
                mCommentView.setText(comment.getBody());
                mAuthorName.setText(comment.getCommenter());
                Glide.with(context).load(comment.getCommenterPhoto()).into(mAuthorImage);
                mLikes.setText(""+comment.getLikes());

                mCommentedTime.setText(comment.getData_commented());

            }
            else {

                //mTotalCommments.setText("No Comments");
               // mCommentLayout.setVisibility(View.GONE);



            }
            this.comment=comment;



    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.btnmore:
                    PopupMenu popup = new PopupMenu(context, mBtnmore);

                    popup.inflate(R.menu.option_menu);
                    if (LOGINUSER.getId()!=comment.getCommenterId()){
                        popup.getMenu().findItem(R.id.delete).setVisible(false);
                    }


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.share:
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    String shareBody =  mCommentView.getText().toString()+"\nwww.olbongo.com";
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    context.startActivity(Intent.createChooser(sharingIntent, "Share the Link"));
                                    break;
                                case R.id.delete:
                                    DeleteComment();

                                    default:
                                        break;
                            }
                            return false;
                        }
                    });

                    popup.show();

                    break;
                case R.id.open_post_image:

                        break;
            }
        }
        private void DeleteComment(){

            AsyncHttpClient deletepost=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("token",LOGINUSER.getToken());
            params.put("comment_id",comment.getId());
            deletepost.get(StaticInformation.DELETE_COMMENT,params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toasty.error(context,responseString, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toasty.success(context,"delete successfully", Toast.LENGTH_SHORT).show();

                    comments.remove(current_position);
                    notifyItemRemoved(current_position);
                    notifyItemRangeRemoved(current_position,getItemCount());
                    notifyDataSetChanged();
                }
            });


        }
    }


}
