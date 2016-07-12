// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.MainActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.api.users.UserManagement;


public class Logout extends AsyncTask<String,Void,String> {

    private Context context;

    public Logout(Context context) {

        this.context = context;
    }


    @Override
    protected String doInBackground(String... arg0) {

//        if (arg0.length == 1) {
//
//            String senderId = arg0[0];
//
//
//
////            UserManagement.deleteToken(senderId);
//        }

        UserManagement.logout();

        return null;
    }

    @Override
    protected void onPostExecute(String dummy){


        // Clear MyApplication variables
        MyApplication.User = null;
        MyApplication.Devices = null;
        MyApplication.LoggedIn = false;

        // Clear the Remember Me Token in SharedPreferences
        UserManagement.clearRememberToken();

        // Open the Main Login Screen
        context.startActivity(new Intent(context, MainActivity.class));
    }

}
