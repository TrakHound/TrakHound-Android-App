package org.trakhound.www.trakhound.device_details;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.devices.DeviceStatus;
import org.trakhound.www.trakhound.devices.DeviceStatusRequest;
import org.trakhound.www.trakhound.users.UserConfiguration;

/**
 * Created by Patrick on 5/22/2016.
 */
public class GetDeviceStatus extends AsyncTask<String,Void,String> {

    private DeviceList context;
    private ProgressDialog progressDialog;
    private DeviceStatusRequest statusRequest;

    public GetDeviceStatus(DeviceList context, DeviceStatusRequest statusRequest, ProgressDialog progressDialog) {

        this.context = context;
        this.statusRequest = statusRequest;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {

        UserConfiguration user = ((MyApplication) (context.getApplication())).User;
        Device[] devices = ((MyApplication) (context.getApplication())).Devices;

//        if (user != null && devices != null) {
//
//            DeviceStatus[] statuses = DeviceStatus.get(statusRequest);
//            if (statuses != null) {
//
//                for (int i = 0; i < statuses.length; i++) {
//
//                    for (int x = 0; x < devices.length; x++) {
//
//                        if (statuses[i].UniqueId != null &&
//                                devices[x].UniqueId != null &&
//                                statuses[i].UniqueId.equals(devices[x].UniqueId)) {
//
//                            devices[x].Status = statuses[i];
//                        }
//                    }
//                }
//            }
//        }

        return null;
    }

    @Override
    protected void onPostExecute(String dummy) {

//        context.updateStatus("test");

        progressDialog.dismiss();
    }

}
