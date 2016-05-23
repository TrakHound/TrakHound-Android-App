// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.device_list.DeviceStatus;
import org.trakhound.www.trakhound.devices.DeviceStatusRequest;
import org.trakhound.www.trakhound.users.UserConfiguration;


public class GetDeviceStatus extends AsyncTask<String,Void,String> {

    private DeviceList context;
    private ProgressDialog progressDialog;

    public GetDeviceStatus(DeviceList context, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        UserConfiguration user = ((MyApplication)(context.getApplication())).User;
        Device[] devices = ((MyApplication) (context.getApplication())).Devices;

        if (user != null && devices != null) {

            DeviceStatus[] statuses = DeviceStatus.get(user);
            if (statuses != null) {

                for (int i = 0; i < statuses.length; i++) {

                    for (int x = 0; x < devices.length; x++) {

                        if (statuses[i].UniqueId != null &&
                                devices[x].UniqueId != null &&
                                statuses[i].UniqueId.equals(devices[x].UniqueId)) {

//                            devices[x].Status = statuses[i];
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String dummy){

//        context.updateStatus("test");

        progressDialog.dismiss();
    }

}
