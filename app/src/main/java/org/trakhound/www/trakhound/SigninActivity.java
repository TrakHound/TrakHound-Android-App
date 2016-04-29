package org.trakhound.www.trakhound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;

import java.lang.Object;
import android.content.Intent;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.trakhound.www.trakhound.devices.Device;

public class SigninActivity extends AsyncTask<String,Void,Device[]> {

    private TextView statusField,roleField;
    private Context context;

    public SigninActivity(Context context,TextView statusField,TextView roleField) {

        this.context = context;
        this.statusField = statusField;
        this.roleField = roleField;

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

        return result;
    }

    @Override
    protected void onPostExecute(Device[] devices){

        if (devices != null) {

            this.statusField.setText("Login Successful");
            this.roleField.setText(Integer.toString(devices.length));

            ((MyApplication)(((MainActivity)context).getApplication())).loggedIn = true;
            ((MyApplication)(((MainActivity)context).getApplication())).Devices = devices;

            // Open the Device List
            context.startActivity(new Intent(context, DeviceListActivity.class));

        } else {

            this.statusField.setText("Login Failed");

            ((MyApplication)context).loggedIn = false;

        }

    }

}
