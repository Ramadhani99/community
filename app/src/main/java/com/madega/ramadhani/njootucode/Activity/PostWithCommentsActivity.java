package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    private RelativeLayout maddCommentView;
    private EditText mTextcomment;




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

        mBackbtn=findViewById(R.id.backbtn);
        mBackbtn.setOnClickListener(this);

        mAddcommentBtn=findViewById(R.id.addcommentbtn);
        mAddcommentBtn.setOnClickListener(this);

        maddCommentView=findViewById(R.id.bottom);
        mTextcomment=findViewById(R.id.text_comment);

        LoginUser= SharedPreferenceHelper.getUSer(getBaseContext());


        post=new Gson().fromJson(getIntent().getStringExtra(POST), PostModel.class);
        SendData();





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
                            comment.setLikes(object.getInt("total_likes"));

                            //commenter object
                            JSONObject author=object.getJSONObject("author");
                            comment.setCommenter(author.getString("display_name"));
                            comment.setCommenterPhoto(author.getString("dp"));

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
                Intent goBack=new Intent(this,TestActivity.class);
                startActivity(goBack);
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

        createpost.post(StaticInformation.PUBLISH_POST, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG,responseString);
                Toasty.error(getBaseContext(),responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toasty.success(getBaseContext(),responseString, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
