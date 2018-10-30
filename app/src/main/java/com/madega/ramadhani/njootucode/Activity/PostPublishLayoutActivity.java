package com.madega.ramadhani.njootucode.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.google.gson.Gson;
import com.madega.ramadhani.njootucode.Helper.CheckNetworkConnection;
import com.madega.ramadhani.njootucode.Models.PostPublishModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;


import es.dmoral.toasty.Toasty;

public class PostPublishLayoutActivity extends CheckNetworkConnection implements OnClickListener {
    private static String TAG="PostPublishLayoutActivity";


    private static User user;



    private ImageView mBackbtn,mAttachment,mChooseAttachemnt,mBtnclose,mMyImage;
    private LinearLayout mLayout_attachement;
    private ImagePicker imagePicker;
    private TextView mPublishBtn,mPostText,mUserName;
    private boolean imageIsset=false;
    private String imgUrl;
    private PostPublishModel mPubModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_publish_layout);
        user= SharedPreferenceHelper.getUSer(getBaseContext());

        mAttachment=findViewById(R.id.Attachment);

        mChooseAttachemnt=findViewById(R.id.choose_attachment);
        mChooseAttachemnt.setOnClickListener(this);

        ActivityCompat.requestPermissions(PostPublishLayoutActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);


        mBackbtn=findViewById(R.id.backbtn);
        mBackbtn.setOnClickListener(this);



        mPostText=findViewById(R.id.txtpost);

//        mMyImage=findViewById(R.id.my_image);
//        Glide.with(getBaseContext()).load(user.getProfileImgaePath()).into(mMyImage);

        mPublishBtn=findViewById(R.id.publish_btn);
        mPublishBtn.setOnClickListener(this);

        mBtnclose=findViewById(R.id.btnclose);
        mBtnclose.setOnClickListener(this);

        mLayout_attachement=findViewById(R.id.layout_attachment);

//        mUserName=findViewById(R.id.username);
//        mUserName.setText(user.getFullname());

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( ImagePicker.shouldHandle(requestCode, resultCode, data))

        {
            mLayout_attachement.setVisibility(View.VISIBLE);


            Glide.with(getBaseContext()).load(ImagePicker.getFirstImageOrNull(data).getPath()).into(mAttachment);
             imgUrl=ImagePicker.getFirstImageOrNull(data).getPath();
            Log.e(TAG,ImagePicker.getFirstImageOrNull(data).getPath());
            Log.e(TAG,data.toString());
            imageIsset=true;
            mAttachment.setVisibility(View.VISIBLE);

        }
        else {

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //imagePicker.handlePermission(requestCode, grantResults);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbtn:
                setResult(Activity.RESULT_CANCELED,new Intent());
                finish();
                break;
            case R.id.choose_attachment:

                ImagePicker.create(this)
                        // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                        .includeVideo(true) // Show video on image picker
                        .single() // single mode
                        .multi() // multi mode (default mode)
                        .limit(10) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)

                        .start(); // start afirm.imagepicker.features.ImagePicker.create(this).start(1);

                //Pix.start(PostPublishLayoutActivity.this,1,1);

                break;
            case R.id.btnclose:
                mLayout_attachement.setVisibility(View.GONE);
                mAttachment.setVisibility(View.GONE);
                mAttachment.setImageURI(null);
                imageIsset=false;
                break;
            case R.id.publish_btn:
                if (mPostText.getText().toString().length()<1 && !imageIsset){
                    Toasty.error(getBaseContext(),"No post to publish",Toast.LENGTH_SHORT).show();

                }
                else{
                    mPubModel=new PostPublishModel();
                    mPubModel.setUser_token(user.getToken());
                    mPubModel.setTextPost(""+mPostText.getText().toString());

                    if (imgUrl!=null) {

                        mPubModel.setImgPost(imgUrl);
                    }
                    else {
                        mPubModel.setImgPost("1");

                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",new Gson().toJson(mPubModel,PostPublishModel.class));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                    //createPost();
                    mPostText.setText("");
                    mAttachment.setImageURI(null);
                    imgUrl=null;

                }


                break;
                default:
                    break;
        }

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED,new Intent());
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


//

    }


}
