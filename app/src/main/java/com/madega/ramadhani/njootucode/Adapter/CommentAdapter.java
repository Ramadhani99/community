package com.madega.ramadhani.njootucode.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Activity.PostWithCommentsActivity;
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
    private CommentAdapterCallBack commentAdapterCallBack;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.context = context;
        commentAdapterCallBack=  (CommentAdapterCallBack) context;


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

        private View mCommentViewLayout;

       private TextView mOpenPost,mCommentView,mAuthorName, mTotal_like,mCommentedTime;

       private ImageView mOpenPostImage,mBtnmore,mDefaulImage,mAuthorImage,mLikes;

        public Itemholder(View itemView) {

            super(itemView);

            //mOpenPostImage=itemView.findViewById(R.id.open_post_image);




            mAuthorName=itemView.findViewById(R.id.comenter);
            mCommentViewLayout=itemView.findViewById(R.id.comment_layout);

            mAuthorImage=itemView.findViewById(R.id.my_image);
            mTotal_like =itemView.findViewById(R.id.count_like);
            mLikes=itemView.findViewById(R.id.likebtn);
            mLikes.setOnClickListener(this);

           // mCommentLayout=itemView.findViewById(R.id.comment_layout);





            mCommentView=itemView.findViewById(R.id.comenter_view);

            mCommentedTime=itemView.findViewById(R.id.date_comment);



            mBtnmore=itemView.findViewById(R.id.btnmore);
            mBtnmore.setOnClickListener(this);


        }
        public void SaveData(Comment comment,int position){
            current_position=position;


            if (comment.isHasComment()){

                mCommentViewLayout.setVisibility(View.VISIBLE);

                Log.e(TAG,comment.getBody());
                mCommentView.setText(comment.getBody());
                mAuthorName.setText(comment.getCommenter());
                Glide.with(context).load(comment.getCommenterPhoto()).into(mAuthorImage);
                mTotal_like.setText(""+comment.getLikes());

                mCommentedTime.setText(comment.getData_commented());
                LikePost(comment.isLiked());

            }
            else {
                mCommentViewLayout.setVisibility(View.GONE);

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
                case R.id.likebtn:
                    commentAdapterCallBack.Like_Unlike_Comment(comment);
                    if (comment.isLiked()){
                       comment.setLikes(comment.getLikes()-1);
                    }else{
                        comment.setLikes(comment.getLikes()+1);
                    }
                    comment.setLiked(!comment.isLiked());

                    LikePost(comment.isLiked());

                    break;
            }
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
        private void DeleteComment(){


            AsyncHttpClient deletePostRequest=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("token",LOGINUSER.getToken());
            params.put("comment_id",comment.getId());
            deletePostRequest.get(StaticInformation.DELETE_COMMENT,params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toasty.error(context, responseString, Toast.LENGTH_SHORT).show();
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

    public interface CommentAdapterCallBack{
        void Like_Unlike_Comment(Comment comment);

    }
    public void addComment(Comment comment){
        Toasty.info(context.getApplicationContext(),"Inafika",Toast.LENGTH_SHORT).show();
        comments.add(getItemCount(),comment);
        notifyItemInserted(getItemCount());


    }




}
