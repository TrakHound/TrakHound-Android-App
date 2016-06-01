// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_details;

import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceDetails;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.users.UserConfiguration;

public class GetDeviceStatus extends AsyncTask<String,Void,DeviceStatus> {

    private DeviceDetails context;
    private String uniqueId;

    public GetDeviceStatus(DeviceDetails context, String uniqueId) {

        this.context = context;
        this.uniqueId = uniqueId;
    }

    protected void onPreExecute(){

        if (context != null) {

            context.showLoading();
        }
    }

    @Override
    protected DeviceStatus doInBackground(String... arg0) {

        UserConfiguration user = MyApplication.User;
        if (user != null && uniqueId != null) {

            DeviceStatus status = DeviceStatus.get(user, uniqueId);
            return status;
        }

        return null;
    }

    @Override
    protected void onPostExecute(DeviceStatus status){

        context.Status = status;
        context.loadData();

        if (context != null) {

            context.hideLoading();
        }

    }

}
