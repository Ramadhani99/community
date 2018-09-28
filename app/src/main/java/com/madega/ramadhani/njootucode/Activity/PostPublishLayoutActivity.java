package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;
import com.myhexaville.smartimagepicker.ImagePicker;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class PostPublishLayoutActivity extends AppCompatActivity implements OnClickListener {
    private static String TAG="PostPublishLayoutActivity";

    private static User user;

    private ImageView mBackbtn,mAttachment,mChooseAttachemnt,mBtnclose;
    private LinearLayout mLayout_attachement;
    private ImagePicker imagePicker;
    private TextView mPublishBtn,mPostText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_publish_layout);
        mAttachment=findViewById(R.id.Attachment);

        mChooseAttachemnt=findViewById(R.id.choose_attachment);
        mChooseAttachemnt.setOnClickListener(this);

        mBackbtn=findViewById(R.id.backbtn);
        mBackbtn.setOnClickListener(this);

        mPostText=findViewById(R.id.txtpost);

        mPublishBtn=findViewById(R.id.publish_btn);
        mPublishBtn.setOnClickListener(this);

        mBtnclose=findViewById(R.id.btnclose);
        mBtnclose.setOnClickListener(this);

        mLayout_attachement=findViewById(R.id.layout_attachment);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbtn:
                Intent goBack=new Intent(this,TestActivity.class);
                startActivity(goBack);
                finish();
                break;
            case R.id.choose_attachment:

                imagePicker = new ImagePicker(this,null,
                        (Uri imageUri) -> {

                            mAttachment.setImageURI(imageUri);
                        });


                imagePicker.choosePicture(true);

                break;
            case R.id.btnclose:
                mLayout_attachement.setVisibility(View.GONE);
                mAttachment.setVisibility(View.GONE);
                mAttachment.setImageURI(null);
                break;
            case R.id.publish_btn:

               createPost();
                break;
                default:
                    break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
        if (resultCode==RESULT_OK){
           mLayout_attachement.setVisibility(View.VISIBLE);
           mAttachment.setVisibility(View.VISIBLE);
        }
        else {

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }
    private void createPost(){
        user= SharedPreferenceHelper.getUSer(getBaseContext());
        AsyncHttpClient createpost=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("token",user.getToken());
        params.put("content",mPostText.getText().toString());
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
