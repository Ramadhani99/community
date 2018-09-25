package com.madega.ramadhani.njootucode.Adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.madega.ramadhani.njootucode.Models.Comment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.R;

import java.util.ArrayList;
import java.util.List;

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

       private TextView mOpenPost,mCommentView,mAuthorName,mLikes,mTotalCommments;
       private View mPostcard,mAllComment,mCommentLayout;
       private ImageView mOpenPostImage,mBtnmore,mDefaulImage,mAuthorImage;

        public Itemholder(View itemView) {

            super(itemView);
            mOpenPost=itemView.findViewById(R.id.open_post);
            mOpenPostImage=itemView.findViewById(R.id.open_post_image);

            mPostcard=itemView.findViewById(R.id.post_card);
            mAllComment=itemView.findViewById(R.id.all_comments);
            mAuthorName=itemView.findViewById(R.id.comenter);
            mAuthorImage=itemView.findViewById(R.id.my_image);
            mLikes=itemView.findViewById(R.id.count_like);

            mCommentLayout=itemView.findViewById(R.id.comment_layout);

            mTotalCommments=itemView.findViewById(R.id.total_comment);

            mCommentView=itemView.findViewById(R.id.comenter_view);


            mBtnmore=itemView.findViewById(R.id.btnmore);
            mBtnmore.setOnClickListener(this);

            Typeface robot=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Medium.ttf");
            mOpenPost.setTypeface(robot);

        }
        public void SaveData(Comment comment,int position){

            if (position==0){
                post=comment.getPostModel();
                mPostcard.setVisibility(View.VISIBLE);

                mOpenPost.setText(post.getPost());
                Linkify.addLinks(mOpenPost,Linkify.ALL);
                //mDefaulImage.setVisibility(View.VISIBLE);

                mAllComment.setVisibility(View.VISIBLE);



                Glide.with(context)
                        .load(post.getPostImage())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                mOpenPostImage.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                //mDefaulImage.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mOpenPostImage);

            }
            if (comment.isHasComment()){

                Log.e(TAG,comment.getBody());
                mCommentView.setText(comment.getBody());
                mAuthorName.setText(comment.getCommenter());
                Glide.with(context).load(comment.getCommenterPhoto()).into(mAuthorImage);
                mLikes.setText(""+comment.getLikes());
                mTotalCommments.setText(""+post.getComments()+" Comments");

            }
            else {

                mTotalCommments.setText("No Comments");
                mCommentLayout.setVisibility(View.GONE);



            }



    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.btnmore:
                    PopupMenu popup = new PopupMenu(context, mBtnmore);

                    popup.inflate(R.menu.option_menu);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return false;
                        }
                    });

                    popup.show();

                    break;
                    default:
                        break;
            }
        }
    }


}
