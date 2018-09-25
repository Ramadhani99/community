package com.madega.ramadhani.njootucode.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.madega.ramadhani.njootucode.R;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private View mRegisterbtn;
    private EditText mFullName,mPhone,mEmail,mPassword,mForgetPassword;
    private View focusview=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);


        mRegisterbtn=findViewById(R.id.registerbtn);
        mFullName=findViewById(R.id.full_name);
        mPhone=findViewById(R.id.phone_number);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mForgetPassword=findViewById(R.id.confirm_password);


       mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String pass=mPassword.getText().toString().trim();
              String confirmpassword=mForgetPassword.getText().toString().trim();
              String name=mFullName.getText().toString().trim();
              String email=mEmail.getText().toString().trim();
              String phone=mPhone.getText().toString().trim();

              if (isValidFullname(name) && isValidPhone(phone)
                      &&isValidEmail(email)
                    && isValidPassword(pass,confirmpassword)

                      ){
                  Toasty.success(getBaseContext(),"Wao", Toast.LENGTH_SHORT).show();
              }

            }
        });
    }

    private boolean isValidFullname(String name){
        focusview=mFullName;
        if (name.isEmpty()){
            focusview.requestFocus();
            mFullName.setError("fullname is required");
            return false;
        }
        else if (name.split("\\s+").length<2){
            focusview.requestFocus();
            mFullName.setError("Enter atleast two name  ");
            return  false;
        }
        else{
            return true;
        }

    }

    private boolean isValidEmail(String mail){
        focusview=mEmail;
        if (mail.isEmpty()){
            focusview.requestFocus();
            mEmail.setError("email is required");
            return false;
        }

        else{
            return true;
        }


    }

    private boolean isValidPassword(String password,String confirmP){
        focusview=mPassword;
        if (password.isEmpty()){
            focusview.requestFocus();
            mPassword.setError("password is required");
            return false;
        }
        else if (!password.equals(confirmP)){
            focusview.requestFocus();
            mPassword.setError("The password dosenot match ");
            return  false;
        }
        else{
            return true;
        }


    }
    private boolean isValidPhone(String phone){
        focusview=mPhone;
        if (phone.isEmpty()){
            focusview.requestFocus();
            mPhone.setError("phone is required");
            return false;
        }
        else if (phone.length()<14){
            focusview.requestFocus();
            mPhone.setError("Enter valid phone number  ");
            return  false;
        }
        else{
            return true;
        }
    }
}
