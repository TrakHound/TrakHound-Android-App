// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.trakhound.www.trakhound.users.Login;
import org.trakhound.www.trakhound.users.UserManagement;


public class UserLoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private CheckBox rememberCHKBX;
    private TextView errorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Setup Toolbar/ActionBar
        setToolbar();

        // Assign EditTexts to local variables
        usernameText = (EditText)findViewById(R.id.UsernameText);
        passwordText = (EditText)findViewById(R.id.PasswordText);
        rememberCHKBX = (CheckBox)findViewById(R.id.RememberCHKBX);

        // Assign TextViews local variable
        errorLabel = (TextView)findViewById(R.id.ErrorLabel);
    }

    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle(R.string.app_name);
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        setSupportActionBar(trakhoundToolbar);
    }

    public void login(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        Boolean r = rememberCHKBX.isChecked();

        ProgressDialog progress = new ProgressDialog(this);

        if (r) {

            String senderId = UserManagement.getSenderId();

            // Create Token Login
            new Login(this, errorLabel, progress).execute(username, password, senderId);
        } else {

            // Basic Login
            new Login(this, errorLabel, progress).execute(username, password);
        }

        progress.setTitle("Logging In " + username);
        progress.setMessage("Please Wait...");
        progress.show();
    }
}
