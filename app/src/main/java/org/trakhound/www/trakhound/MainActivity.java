// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import org.trakhound.www.trakhound.device_list.GetDevices;
import org.trakhound.www.trakhound.api.users.UserManagement;


public class MainActivity extends AppCompatActivity {

    public static boolean error;

    public static String showError = "show_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setCurrentActivity(this);

        // Set Status Bar Color
        setStatusBar();

        UserManagement.context = getApplicationContext();

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                error = extras.getBoolean(showError);
            }
        }

        if (error) showError();

        // Attempt to login using saved credentials
        if (MyApplication.User == null) tokenLogin();
    }

    private void showError() {

        TextView errorTextView = (TextView)findViewById(R.id.ErrorLabel);
        if (errorTextView != null) errorTextView.setVisibility(View.VISIBLE);
    }

    public void tokenLogin() {

        String username = UserManagement.getRememberUsername();
        String token = UserManagement.getRememberToken();

        if (token != null) userLogin(token, username);
    }

//    public void tokenLogin() {
//
//        String username = UserManagement.getRememberUsername();
//        String token = UserManagement.getRememberToken();
//        if (token != null) {
//            if (token.startsWith("%%")) {
//
//                localLogin(token, username);
//
//            } else {
//
//                userLogin(token, username);
//            }
//        }
//    }

    public void login(View view) {

        String username = ((TextView)findViewById(R.id.UsernameText)).getText().toString();
        String password = ((TextView)findViewById(R.id.PasswordText)).getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {

            showLoading(username);

            // Create Token Login
            new Login(this, null).execute(username, password, "");

        } else showError();
    }

    private void showLoading(String username) {

        // Open Loading screen
        String displayUsername = TH_Tools.capitalizeFirst(username);
        if (displayUsername != null) Loading.Open(this, "Logging in " + displayUsername + "..");
        else Loading.Open(this, "Logging in..");
    }

    public void userLogin(String token, String username) {

        showLoading(username);

        new Login(this, null).execute(token);
    }

//    private void localLogin(String token, String username) {
//
//        // Show Loading Activity
//        Loading.Open(this, "Loading Devices for " + TH_Tools.capitalizeFirst(username) + "..");
//
//        new GetDevices(this, GetDevices.LoginType.LOCAL).execute(token);
//    }


//    public void openLocalLogin(View view){
//
//        Context context = getBaseContext();
//
//        // Open the Local Login Screen
//        Intent localLoginIntent = new Intent(context, LocalLogin.class);
//        localLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(localLoginIntent);
//    }

    public void openAbout(View view) {

        Context context = getBaseContext();

        // Open the About Screen
        Intent aboutIntent = new Intent(context, About.class);
        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(aboutIntent);
    }

    private void setStatusBar(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar_color));
        }
    }

}
