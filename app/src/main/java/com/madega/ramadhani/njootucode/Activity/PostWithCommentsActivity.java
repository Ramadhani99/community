package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.madega.ramadhani.njootucode.Adapter.CommentAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Fragments.HomeFragment;
import com.madega.ramadhani.njootucode.Models.Comment;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class PostWithCommentsActivity extends AppCompatActivity implements  View.OnClickListener {
    private static String TAG="PostWithCommentsActivity";
    public static String POST="post";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter mCommentAdapter;
    private List<Comment> comments=new ArrayList<>();
    private PostModel post;
    private Comment comment;
    private ImageView mBackbtn,mAddcommentBtn;

    private User LoginUser;
    private TextView mOpenPost,mAllComment,mTotalCommments;

    private RelativeLayout maddCommentView;
    private EditText mTextcomment;
    private SmoothProgressBar smoothProgressBar;

    private ImageView  mOpenPostImage;
    private View minputView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_with_comments_layout);
        mRecyclerView=findViewById(R.id.recyclerview);
        linearLayoutManager=new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mCommentAdapter=new CommentAdapter(this,comments);
        mRecyclerView.setAdapter(mCommentAdapter);

        //mPostcard=findViewById(R.id.post_card);
        //mAllComment=findViewById(R.id.all_comments);
        mTotalCommments=findViewById(R.id.total_comment);
        mOpenPost=findViewById(R.id.open_post);

        mBackbtn=findViewById(R.id.backbtn);
        mBackbtn.setOnClickListener(this);

        minputView=findViewById(R.id.post_comment_view);

        mAddcommentBtn=findViewById(R.id.addcommentbtn);
        mAddcommentBtn.setOnClickListener(this);

        maddCommentView=findViewById(R.id.bottom);
        mTextcomment=findViewById(R.id.text_comment);

        LoginUser= SharedPreferenceHelper.getUSer(getBaseContext());

        smoothProgressBar=findViewById(R.id.progress);


        post=new Gson().fromJson(getIntent().getStringExtra(POST), PostModel.class);

        mTotalCommments.setText(""+post.getComments()+" Comments");

        mOpenPost.setText(post.getPost());
        Linkify.addLinks(mOpenPost,Linkify.ALL);

        mOpenPostImage=findViewById(R.id.open_post_image);

        Glide.with(getBaseContext())
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
        mOpenPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showImage=new Intent(PostWithCommentsActivity.this,ViewPhotoLayoutActivity.class);
                showImage.putExtra("image",post.getPostImage());
                startActivity(showImage);


            }
        });


        Typeface robot=Typeface.createFromAsset(getAssets(),"font/Roboto-Medium.ttf");
        mOpenPost.setTypeface(robot);

        SendData();
        if (comments.size()<3){
            //mRecyclerView.setL
        }





    }


    private void SendData(){





        Log.e(TAG,post.getPost() );

        AsyncHttpClient get_post_comment=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        //postData

        params.put("token", LoginUser.getToken());
        params.put("post_id",post.getId());



        get_post_comment.get(StaticInformation.COMMENT,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                  Log.e(TAG,responseString+"\n"+statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                smoothProgressBar.setVisibility(View.GONE);
                try {
                    Log.e(TAG,responseString );
                    JSONArray allcomments = new JSONArray(responseString);
                    if (allcomments.length()==0){
                        comment=new Comment();
                        comment.setPostModel(post);
                        comment.setHasComment(false);
                        comments.add(comment);
                        mCommentAdapter.notifyDataSetChanged();

                        Log.e(TAG,comment.getPostModel().getPost());

                    }
                    else{
                        for (int k=0;k<allcomments.length();k++){
                            comment=new Comment();
                            JSONObject object=allcomments.getJSONObject(k);
                            comment.setHasComment(true);
                            comment.setBody(object.optString("content"));
                            comment.setPostModel(post);
                            comment.setId(object.optInt("id"));
                            comment.setLikes(object.getInt("total_likes"));
                            comment.setData_commented(object.optString("published_at"));

                            //commenter object
                            JSONObject author=object.getJSONObject("author");
                            comment.setCommenter(author.getString("display_name"));
                            comment.setCommenterPhoto(author.getString("dp"));
                            comment.setCommenterId(author.getInt("id"));

                            comments.add(comment);
                            mCommentAdapter.notifyDataSetChanged();
                            Log.e(TAG,comment.getBody());

                        }

                    }

                } catch (Exception e) {


                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbtn:

                finish();
                break;
            case  R.id.addcommentbtn:
                Createcomment();
                break;
                default:
                    break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public  boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu,menu);

        return true;
    }
    private void Createcomment(){

        AsyncHttpClient createpost=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("token",LoginUser.getToken());
        params.put("content",mTextcomment.getText().toString());
        params.put("post_id",post.getId());

        createpost.post(StaticInformation.PUBLISH_COMMENT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG,responseString);
                Toasty.error(getBaseContext(),responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
               Toasty.success(getBaseContext(),responseString, Toast.LENGTH_SHORT).show();
               comments.clear();
                //JSONArray allcomments = new JSONArray(responseString);
                SendData();
                InputMethodManager inp=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inp.hideSoftInputFromWindow( minputView.getWindowToken(),0);
                mTextcomment.setText("");




            }
        });

    }




}
