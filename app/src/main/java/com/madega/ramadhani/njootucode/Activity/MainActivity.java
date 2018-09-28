package com.madega.ramadhani.njootucode.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.madega.ramadhani.njootucode.BasicInfo.StaticInformation;
import com.madega.ramadhani.njootucode.Models.User;
import com.madega.ramadhani.njootucode.R;
import com.madega.ramadhani.njootucode.Storage.SharedPreferenceHelper;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MAIN ACTIVITY";
    private EditText mUsername, mPassword;
    private TextView mLogo;
    private ProgressDialog mProgressDialog;
    private View focusvIew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //initilize
        mUsername = findViewById(R.id.useranme);
        mPassword = findViewById(R.id.password);


        mLogo=findViewById(R.id.logo);
        Typeface robot=Typeface.createFromAsset(getAssets(),"font/FasterOne-Regular.ttf");
        mLogo.setTypeface(robot);



        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                LoginProcess(username,password);

            }
        });
        findViewById(R.id.sigupbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isValidPassword(String password) {


        if (password.isEmpty()) {
            focusvIew = mPassword;
            focusvIew.requestFocus();
            Toasty.warning(getBaseContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }

    }

    private boolean isValidUsername(String username) {
        if (username.isEmpty()) {
            focusvIew = mUsername;
            focusvIew.requestFocus();
            Toasty.warning(getBaseContext(), "Enter username", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }
    private void LoginProcess(String name,String password){

        AsyncHttpClient login=new AsyncHttpClient();
        RequestParams params =new RequestParams();
        params.put("username",name);
        params.put("password",password);
        login.post(StaticInformation.LOGIN,params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog=new ProgressDialog(MainActivity.this);
                mProgressDialog.setTitle("Login");
                mProgressDialog.setMessage("Please wait..........");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toasty.error(getBaseContext(),"failure to login try again",Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                Log.e(TAG,responseString );

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mProgressDialog.dismiss();
                boolean status=false;
                String msg="";
                String token="";
                JSONObject userObject=null;
                try {
                    JSONObject obj=new JSONObject(responseString);

                    status=obj.getBoolean("status");
                    token=obj.optString("token");
                    msg=obj.optString("message");
                    userObject= obj.getJSONObject("user");
                    Log.e(TAG, msg );
                    Log.e(TAG, token );
                    Log.e(TAG,""+status);
                }catch (Exception e){
                    Log.e(TAG,e.getMessage() );

                }
                if (status){
                    Toasty.success(getBaseContext(),"Successfully login",Toast.LENGTH_SHORT).show();

                    //Store the json to object


                    User myuser=new User();
                    myuser.setToken(token);
                    myuser.setId(Integer.parseInt(userObject.optString("id")));
                    myuser.setFullname(userObject.optString("displayname"));
                    myuser.setStatus("status");
                    myuser.setProfileImgaePath(userObject.optString("dp"));

                    String mysuserJson=new Gson().toJson(myuser);
                    SharedPreferenceHelper.StoreUser(getBaseContext(),mysuserJson);



                    Log.e(TAG,myuser.getFullname()+" "+myuser.getProfileImgaePath()+" "+myuser.getToken());




                    Intent login=new Intent(MainActivity.this,TestActivity.class);
                    login.putExtra("token",token);
                    startActivity(login);
                    finish();

                }
                else{
                    Toasty.error(getBaseContext(),"Wrong username and password",Toast.LENGTH_SHORT).show();
                }





            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPreferenceHelper.getUSer(getBaseContext())!=null){
            Intent login=new Intent(MainActivity.this,TestActivity.class);
            //login.putExtra("token",token);
            startActivity(login);
            finish();

        };
    }
}
