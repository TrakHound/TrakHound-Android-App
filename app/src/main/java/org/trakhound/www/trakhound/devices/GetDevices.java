// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.devices;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.users.UserConfiguration;


public class GetDevices extends AsyncTask<String,Void,Device[]> {

    private DeviceList context;
    private ProgressDialog progressDialog;

    public GetDevices(DeviceList context, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute(){

    }

    @Override
    protected Device[] doInBackground(String... arg0) {

        Device[] result = null;

//        UserConfiguration userConfig = ((MyApplication)(context.getApplication())).User;
        UserConfiguration userConfig = MyApplication.User;
        if (userConfig != null) {

            result = Device.readAll(userConfig);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Device[] devices){

//        ((MyApplication)(context.getApplication())).Devices = devices;
        MyApplication.Devices = devices;

        context.addDevices();

        if (progressDialog != null) progressDialog.dismiss();
    }

}
