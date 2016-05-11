package org.trakhound.www.trakhound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;


public class Login extends AsyncTask<String,Void,UserConfiguration> {

    private Context context;
    private TextView errorText;
    private ProgressDialog progressDialog;

    public Login(Context context, TextView errorText, ProgressDialog progressDialog) {

        this.context = context;
        this.errorText = errorText;
        this.progressDialog = progressDialog;
    }


    @Override
    protected UserConfiguration doInBackground(String... arg0) {

        if (arg0.length == 1) {

            String token = arg0[0];

            return UserManagement.tokenLogin(token);

        } else if (arg0.length == 2) {

            String id = arg0[0];
            String password = arg0[1];

            return UserManagement.basicLogin(id, password);

        } else if (arg0.length == 3) {

            String id = arg0[0];
            String password = arg0[1];
            String senderId = arg0[2];

            return UserManagement.createTokenLogin(id, password, senderId);

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserConfiguration userConfig){

        ((MyApplication)((Activity)context).getApplication()).User = userConfig;

        if (userConfig != null) {

            if (errorText != null) errorText.setVisibility(View.INVISIBLE);

            ((MyApplication)(((Activity)context).getApplication())).LoggedIn = true;

            // Open the User Home Screen
            context.startActivity(new Intent(context, UserHome.class));

        } else {

            if (errorText != null) errorText.setVisibility(View.VISIBLE);

            ((MyApplication)(((Activity)context).getApplication())).LoggedIn = false;
        }

        progressDialog.dismiss();
    }

}
