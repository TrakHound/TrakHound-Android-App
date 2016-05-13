package org.trakhound.www.trakhound;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

        loadDevice(device);
    }

    public void loadDevice(Device d) {

        loadImages(d);

        loadDescription(d);

    }

    private void loadImages(Device d) {

        ImageView img;

        img = (ImageView) findViewById(R.id.DeviceImage);
        if (img != null) img.setImageBitmap(d.Image);

        img = (ImageView) findViewById(R.id.DeviceLogo);
        if (img != null) img.setImageBitmap(d.Logo);

    }

    private void loadDescription(Device d) {

        TextView txt;

        txt = (TextView) findViewById(R.id.Description);
        if (txt != null) txt.setText(d.Description);

        txt = (TextView) findViewById(R.id.Manufacturer);
        if (txt != null) txt.setText(d.Manufacturer);

        txt = (TextView) findViewById(R.id.Model);
        if (txt != null) txt.setText(d.Model);

        txt = (TextView) findViewById(R.id.Serial);
        if (txt != null) txt.setText(d.Serial);

        txt = (TextView) findViewById(R.id.Controller);
        if (txt != null) txt.setText(d.Controller);

    }

}
