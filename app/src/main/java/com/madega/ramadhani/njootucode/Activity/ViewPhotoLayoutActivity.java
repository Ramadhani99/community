package com.madega.ramadhani.njootucode.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.chrisbanes.photoview.PhotoView;
import com.madega.ramadhani.njootucode.R;



import static android.provider.CalendarContract.CalendarCache.URI;

public class ViewPhotoLayoutActivity extends AppCompatActivity {
    private PhotoView mViewImage;
    private static String TAG="ViewPhotoLayoutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.view_photo_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.BLACK);
        }

        mViewImage=findViewById(R.id.view_image);
        Log.e(TAG,getIntent().getStringExtra("image"));


         Glide.with(ViewPhotoLayoutActivity.this)
                .load(getIntent().getStringExtra("image")).listener(new RequestListener<Drawable>() {
             @Override
             public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                 return false;
             }

             @Override
             public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                 return false;
             }
         })
                 .into(mViewImage);

         findViewById(R.id.backBtnPic).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });




    }
}
