package org.trakhound.www.trakhound;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.trakhound.www.trakhound.devices.Device;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static String DEVICE_INDEX = "device_index";

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent intent = this.getIntent();

        int deviceIndex = intent.getIntExtra(DEVICE_INDEX, 0);

        Device[] devices = ((MyApplication) this.getApplication()).Devices;

        device = devices[deviceIndex];

        LoadDevice(device);
    }

    public void LoadDevice(Device d) {

        TextView description = (TextView) findViewById(R.id.Description);
        description.setText(d.Description);

    }

}
