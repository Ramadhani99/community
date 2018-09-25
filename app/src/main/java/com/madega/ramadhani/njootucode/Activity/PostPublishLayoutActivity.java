package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.madega.ramadhani.njootucode.R;
import com.myhexaville.smartimagepicker.ImagePicker;

public class PostPublishLayoutActivity extends AppCompatActivity implements OnClickListener {

    private ImageView mBackbtn,mAttachment,mChooseAttachemnt,mBtnclose;
    private LinearLayout mLayout_attachement;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_publish_layout);
        mAttachment=findViewById(R.id.Attachment);

        mChooseAttachemnt=findViewById(R.id.choose_attachment);
        mChooseAttachemnt.setOnClickListener(this);

        mBackbtn=findViewById(R.id.backbtn);
        mBackbtn.setOnClickListener(this);

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
}
