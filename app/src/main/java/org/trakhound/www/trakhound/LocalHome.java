package org.trakhound.www.trakhound;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LocalHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_home);

        setOnClickListeners();
        setToolbar();
    }

    private void setOnClickListeners() {

        View v;

        v = findViewById(R.id.DeviceListButton);
        if (v != null) v.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDeviceList(v);
                }
            });

        v = findViewById(R.id.LogoutButton);
        if (v != null) v.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    logout(v);
                }
            });
    }

    public void openDeviceList(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), DeviceListActivity.class));
    }

    public void logout(View view){

        // Clear the Remember Me Token in SharedPreferences
        UserManagement.clearRememberToken();

        // Open the Main Login Screen
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Home");
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        setSupportActionBar(trakhoundToolbar);
    }
}
