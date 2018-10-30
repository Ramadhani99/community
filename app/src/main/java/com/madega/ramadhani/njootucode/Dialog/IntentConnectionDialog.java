package com.madega.ramadhani.njootucode.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.madega.ramadhani.njootucode.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by root on 10/16/18.
 */

public class IntentConnectionDialog extends DialogFragment {






        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View dialogView=inflater.inflate(R.layout.no_connection_layout,container,false);

                  dialogView.findViewById(R.id.cancelbtn).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          dismiss();
                      }
                  });
            return dialogView;


        }

        @Override
        public void onStart() {
            super.onStart();
            getDialog().getWindow().setLayout(MATCH_PARENT,WRAP_CONTENT);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog d=super.onCreateDialog(savedInstanceState);
            d.setCanceledOnTouchOutside(true);
            return  d;
        }




}
