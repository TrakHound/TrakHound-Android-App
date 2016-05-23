// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.users;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;


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

            return UserManagement.createTokenLogin(id, password);

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserConfiguration userConfig){

        if (context != null) {

            ((MyApplication)((Activity)context).getApplication()).User = userConfig;

            if (userConfig != null) {

                if (errorText != null) errorText.setVisibility(View.INVISIBLE);

                ((MyApplication)(((Activity)context).getApplication())).LoggedIn = true;

                Intent deviceListIntent = new Intent(context, DeviceList.class);
                deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                // Open the Device List Page
                context.startActivity(deviceListIntent);

            } else {

                if (errorText != null) errorText.setVisibility(View.VISIBLE);

                ((MyApplication)(((Activity)context).getApplication())).LoggedIn = false;
            }
        }

        if (progressDialog != null) progressDialog.dismiss();
    }

}
