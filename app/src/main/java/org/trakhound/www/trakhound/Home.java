package org.trakhound.www.trakhound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void userLogin(View view){

        // Open the Device List
        getApplicationContext().startActivity(new Intent(getApplicationContext(), DeviceListActivity2.class));
    }

    public void localLogin(View view){

        // Open the Device List
        getApplicationContext().startActivity(new Intent(getApplicationContext(), DeviceListActivity2.class));
    }

}
