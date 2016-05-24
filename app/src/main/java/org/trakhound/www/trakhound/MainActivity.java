// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.TextView;

import org.trakhound.www.trakhound.users.Login;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManagement.context = getApplicationContext();

        // Attempt to login using saved credentials
        tokenLogin();
    }

    public void tokenLogin() {

        String token = UserManagement.getRememberToken();
        if (token != null) {
            if (token.startsWith("%%")) {

                localLogin(token);
            } else {

                userLogin(token);
            }
        }
    }

    public void login(View view) {

        String username = ((TextView)findViewById(R.id.UsernameText)).getText().toString();
        String password = ((TextView)findViewById(R.id.PasswordText)).getText().toString();

        Boolean r = ((CheckBox)findViewById(R.id.RememberCHKBX)).isChecked();

        TextView errorLabel = (TextView)findViewById(R.id.ErrorLabel);

        ProgressDialog progress = new ProgressDialog(this);

        if (r) {

            // Create Token Login
            new Login(this, errorLabel, progress).execute(username, password, "");
        } else {

            // Basic Login
            new Login(this, errorLabel, progress).execute(username, password);
        }

        progress.setTitle("Logging In " + username);
        progress.setMessage("Please Wait...");
        progress.show();
    }

    public void userLogin(String token) {

        ProgressDialog progress = new ProgressDialog(this);

        new Login(this, null, progress).execute(token);

        progress.setTitle("Logging In");
        progress.setMessage("Wait while loading...");
        progress.show();
    }

    private void localLogin(String token) {

        UserConfiguration userConfig = UserManagement.localLogin(token, false);

        MyApplication.User = userConfig;
        MyApplication.LoggedIn = true;
//        ((MyApplication) this.getApplication()).User = userConfig;
//        ((MyApplication) this.getApplication()).LoggedIn = true;

        // Open the Local Home Screen
        startActivity(new Intent(getBaseContext(), DeviceList.class));
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
