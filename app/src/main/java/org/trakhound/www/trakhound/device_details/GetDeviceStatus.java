// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_details;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceDetails;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.api.users.UserConfiguration;

public class GetDeviceStatus extends AsyncTask<String,Void,DeviceStatus> {

    private DeviceDetails deviceDetails;
    private Context context;
    private String uniqueId;
    private int position;

    public GetDeviceStatus(DeviceDetails deviceDetails, String uniqueId) {

        this.deviceDetails = deviceDetails;
        this.uniqueId = uniqueId;
    }

    public GetDeviceStatus(Context context, int position) {

        this.context = context;
        this.position = position;

        if (MyApplication.ListItems != null) {

            if (position < MyApplication.ListItems.length) {

                String uniqueId = MyApplication.ListItems[position].uniqueId;
                if (uniqueId != null) {

                    this.uniqueId = uniqueId;
                }
            }
        }
    }

    protected void onPreExecute(){

        if (deviceDetails != null) {

            deviceDetails.showLoading();
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

        deviceDetails.deviceStatus = status;

        if (deviceDetails != null) {

            deviceDetails.loadData();

            deviceDetails.hideLoading();

        } else {

            Intent deviceDetailsIntent = new Intent(context, DeviceDetails.class);
            deviceDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Pass the index of the device in the MyApplication.Devices array
            deviceDetailsIntent.putExtra(DeviceDetails.DEVICE_INDEX, position);

            // Open the Device List Page
            context.startActivity(deviceDetailsIntent);
        }

    }

}
