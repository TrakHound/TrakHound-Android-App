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
            new GetDevices(this, GetDevices.LoginType.CREATE_TOKEN).execute(username, password, "");

        } else {

            // Basic Login
            new GetDevices(this, GetDevices.LoginType.BASIC).execute(username, password);
        }
    }

    public void userLogin(String token, String username) {

        // Show Loading Activity
        Loading.Open(this, "Logging in " + TH_Tools.capitalizeFirst(username) + "..");

        new GetDevices(this, GetDevices.LoginType.TOKEN).execute(token);
    }

    private void localLogin(String token) {

//        UserConfiguration userConfig = UserManagement.localLogin(token, false);
//
//        MyApplication.User = userConfig;
//        MyApplication.LoggedIn = true;
//
//        Context context = getBaseContext();
//
//        // Open the Local Home Screen
//        Intent deviceListIntent = new Intent(context, DeviceList.class);
//        deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Open the Device List Page
//        context.startActivity(deviceListIntent);
    }


    public void openLocalLogin(View view){

        Context context = getBaseContext();

        // Open the Local Login Screen
        Intent localLoginIntent = new Intent(context, LocalLogin.class);
        localLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(localLoginIntent);
    }

    public void openAbout(View view) {

        Context context = getBaseContext();

        // Open the About Screen
        Intent aboutIntent = new Intent(context, About.class);
        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(aboutIntent);
    }

}
