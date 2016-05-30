// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.TextView;

import org.trakhound.www.trakhound.device_list.GetDevices;
import org.trakhound.www.trakhound.users.Login;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setCurrentActivity(this);

        UserManagement.context = getApplicationContext();

        // Attempt to login using saved credentials
        if (MyApplication.User == null) tokenLogin();
    }

    public void tokenLogin() {

        String username = UserManagement.getRememberUsername();
        String token = UserManagement.getRememberToken();
        if (token != null) {
            if (token.startsWith("%%")) {

                localLogin(token);

            } else {

                userLogin(token, username);
            }
        }
    }

    public void login(View view) {

        String username = ((TextView)findViewById(R.id.UsernameText)).getText().toString();
        String password = ((TextView)findViewById(R.id.PasswordText)).getText().toString();

        Boolean r = ((CheckBox)findViewById(R.id.RememberCHKBX)).isChecked();

        TextView errorLabel = (TextView)findViewById(R.id.ErrorLabel);

        Loading.Open(this, "Logging in " + TH_Tools.capitalizeFirst(username) + "..");

        if (r) {

            // Create Token Login
//            new Login(this, errorLabel).execute(username, password, "");
            new GetDevices(this).execute(username, password, "");

        } else {

            // Basic Login
            new GetDevices(this).execute(username, password);
//            new Login(this, errorLabel).execute(username, password);
        }

//        startActivity(new Intent(getBaseContext(), Loading.class));

    }

    public void userLogin(String token, String username) {

        // Show Loading Activity
        Loading.Open(this, "Logging in " + TH_Tools.capitalizeFirst(username) + "..");

        //new Login(this, null).execute(token);
        new GetDevices(this).execute(token);


//        Intent loading = new Intent(this, Loading.class);
//        loading.putExtra(Loading.LOADING_TEXT,"Logging in " + TH_Tools.capitalizeFirst(username) + "..");
//        startActivity(loading);

    }

    private void localLogin(String token) {

        UserConfiguration userConfig = UserManagement.localLogin(token, false);

        MyApplication.User = userConfig;
        MyApplication.LoggedIn = true;

        Context context = getBaseContext();

        // Open the Local Home Screen
        Intent deviceListIntent = new Intent(context, DeviceList.class);
        deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        // Open the Device List Page
        context.startActivity(deviceListIntent);
    }


    public void openLocalLogin(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), LocalLoginActivity.class));
    }

    public void openAbout(View view) {

        // Open the About Page
        startActivity(new Intent(getBaseContext(), About.class));
    }

}
