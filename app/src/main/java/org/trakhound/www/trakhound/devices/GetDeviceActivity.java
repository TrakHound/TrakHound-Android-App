package org.trakhound.www.trakhound.devices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.trakhound.www.trakhound.DeviceListActivity;
import org.trakhound.www.trakhound.MainActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.UserConfiguration;
import org.trakhound.www.trakhound.UserManagement;

import java.lang.reflect.AccessibleObject;

public class GetDeviceActivity extends AsyncTask<String,Void,Device[]> {

    private Context context;
    private TextView errorText;
    private ProgressDialog progressDialog;

    public GetDeviceActivity(Context context) {

        this.context = context;
        this.errorText = errorText;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute(){

    }

    @Override
    protected Device[] doInBackground(String... arg0) {

        Device[] result = null;

        String id = arg0[0];
        String password = arg0[1];

        UserConfiguration userConfig = UserManagement.Login(id, password);
        if (userConfig != null) {

            result = Device.readAll(userConfig);
        }

        ((MyApplication)(((MainActivity)context).getApplication())).User = userConfig;

        return result;
    }

    @Override
    protected void onPostExecute(Device[] devices){

        if (devices != null) {

//            ((MyApplication)((Activity)context.getApplication())).LoggedIn = true;

            ((MyApplication)(((MainActivity)context).getApplication())).Devices = devices;

            // Open the Device List
            context.startActivity(new Intent(context, DeviceListActivity.class));

        } else {

            errorText.setVisibility(View.VISIBLE);

            ((MyApplication)(((MainActivity)context).getApplication())).LoggedIn = false;

        }

        progressDialog.dismiss();

    }

}
