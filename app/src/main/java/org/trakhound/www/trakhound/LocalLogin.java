// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;

public class LocalLogin extends AppCompatActivity {

    private EditText mobileIdText;
    private TextView errorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_login);

        // Setup Toolbar/ActionBar
        setToolbar();

        // Assign EditTexts to local variables
        mobileIdText = (EditText)findViewById(R.id.MobileIdText);

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

        // Set Navigation Button Icon
//        trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        setSupportActionBar(trakhoundToolbar);
    }

    public void login(View view){

        String id = mobileIdText.getText().toString();

        CheckBox rememberCHKBX = (CheckBox)findViewById(R.id.RememberCHKBX);
        Boolean remember = rememberCHKBX.isChecked();

        UserConfiguration userConfig = UserManagement.localLogin(id, remember);

        MyApplication.User = userConfig;
        MyApplication.LoggedIn = true;
//        ((MyApplication) this.getApplication()).User = userConfig;
//        ((MyApplication) this.getApplication()).LoggedIn = true;


        Context context = getBaseContext();

        // Open the Local Home Screen
        Intent deviceListIntent = new Intent(context, DeviceList.class);
        deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        // Open the Device List Page
        context.startActivity(deviceListIntent);

//        startActivity(new Intent(getBaseContext(), LocalHome.class));

    }
}
