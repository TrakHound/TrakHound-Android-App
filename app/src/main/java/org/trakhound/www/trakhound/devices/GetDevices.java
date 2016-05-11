package org.trakhound.www.trakhound.devices;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceListActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.UserConfiguration;


public class GetDevices extends AsyncTask<String,Void,Device[]> {

    private DeviceListActivity context;
    private ProgressDialog progressDialog;

    public GetDevices(DeviceListActivity context, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute(){

    }

    @Override
    protected Device[] doInBackground(String... arg0) {

        Device[] result = null;

        UserConfiguration userConfig = ((MyApplication)(context.getApplication())).User;
        if (userConfig != null) {

            result = Device.readAll(userConfig);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Device[] devices){

        ((MyApplication)(context.getApplication())).Devices = devices;

        context.addDevices();

        progressDialog.dismiss();
    }

}
