package org.trakhound.www.trakhound.devices;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceListActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.UserConfiguration;


public class GetDeviceStatus extends AsyncTask<String,Void,String> {

    private DeviceListActivity context;
    private ProgressDialog progressDialog;

    public GetDeviceStatus(DeviceListActivity context, ProgressDialog progressDialog) {

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

                            devices[x].Status = statuses[i];
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String dummy){

        context.updateStatus("test");

        progressDialog.dismiss();
    }

}
