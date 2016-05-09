package org.trakhound.www.trakhound;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar/ActionBar
        SetToolbar();
    }

    private void SetToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle(R.string.app_name);
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        // Set Navigation Button Icon
        //trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        setSupportActionBar(trakhoundToolbar);
    }

    public void userLogin(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), UserLoginActivity.class));
    }

    public void localLogin(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), LocalLoginActivity.class));
    }

}
