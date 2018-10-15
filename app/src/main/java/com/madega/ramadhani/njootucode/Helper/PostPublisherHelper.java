package com.madega.ramadhani.njootucode.Helper;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.madega.ramadhani.njootucode.Activity.PostPublishLayoutActivity;
import com.madega.ramadhani.njootucode.Activity.TestActivity;
import com.madega.ramadhani.njootucode.Adapter.PostAdapter;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.PostPublishModel;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import java.io.File;
import java.io.FileNotFoundException;

import es.dmoral.toasty.Toasty;

/**
 * Created by root on 10/15/18.
 */

public class PostPublisherHelper  {

    private PostPublishModel mPostPublishModel=new PostPublishModel();

    public PostPublisherHelper(PostPublishModel mPostPublishModel) {
        this.mPostPublishModel = mPostPublishModel;
    }


}
