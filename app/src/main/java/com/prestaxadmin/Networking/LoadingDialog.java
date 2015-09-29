package com.prestaxadmin.Networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.prestaxadmin.R;

/**
 * Created by jose.sanchez on 18/08/2015.
 */
public class LoadingDialog {

    ProgressDialog progressDialog;

    public LoadingDialog(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    public void show(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.layout_loading_dialog);
    }

    public void dismiss(){
        progressDialog.dismiss();
    }
}
