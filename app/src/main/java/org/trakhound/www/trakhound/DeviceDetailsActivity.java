// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

        setToolbar();
    }

    public void loadDevice(Device d) {

        loadImages(d);

        loadDescription(d);

        refresh();
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


    private void refresh() {

        if (device != null) {

            updateProductionStatus(device);
            updateOeeStatus(device);
            updateControllerStatus(device);
        }
    }

    private void updateProductionStatus(Device d) {

        // Banner
        View banner = findViewById(R.id.StatusBanner);
        if (banner != null) {

            if (d.Status.Status.Alert) {
                banner.setBackgroundColor(getResources().getColor(R.color.statusRed));
            } else if (d.Status.Status.Idle) {
                banner.setBackgroundColor(getResources().getColor(R.color.statusYellow));
            } else {
                banner.setBackgroundColor(getResources().getColor(R.color.statusGreen));
            }
        }

        ImageView img = (ImageView)findViewById(R.id.AlertIndicator);
        if (img != null) {
            if (d.Status.Status.Alert) img.setVisibility(View.VISIBLE);
            else img.setVisibility(View.INVISIBLE);
        }


        // Percentages
//        if (d.Status.SecondsTotal > 0) {
//
//
//
//            double production = ((double)d.Status.SecondsProduction / d.Status.SecondsTotal) * 100;
//            double idle = ((double)d.Status.SecondsIdle / d.Status.SecondsTotal) * 100;
//            double alert = ((double)d.Status.SecondsAlert / d.Status.SecondsTotal) * 100;
//
//            // Progress Bars
//
//            // Production
//            ProgressBar pb = (ProgressBar)findViewById(R.id.ProductionProgressBar);
//            if (pb != null) pb.setProgress((int)Math.round(production));
//
//            // Idle
//            pb = (ProgressBar)findViewById(R.id.IdleProgressBar);
//            if (pb != null) pb.setProgress((int)Math.round(idle));
//
//            // Alert
//            pb = (ProgressBar)findViewById(R.id.AlertProgressBar);
//            if (pb != null) pb.setProgress((int)Math.round(alert));
//
//
//            // Percentage TextViews
//
//            // Production
//            TextView txt = (TextView)findViewById(R.id.ProductionPercentage);
//            if (txt != null) txt.setText(String.format("%.0f%%", production));
//
//            // Idle
//            txt = (TextView)findViewById(R.id.IdlePercentage);
//            if (txt != null) txt.setText(String.format("%.0f%%", idle));
//
//            // Alert
//            txt = (TextView)findViewById(R.id.AlertPercentage);
//            if (txt != null) txt.setText(String.format("%.0f%%", alert));
//
//        }
    }


    private void updateOeeStatus(Device d) {

        // Set OEE
        TextView txt = (TextView) findViewById(R.id.OEE);
        if (txt != null) {

            double val = device.Status.Oee.Oee * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }

        // Set Availability
        txt = (TextView) findViewById(R.id.AvailabilityVariable);
        if (txt != null) {

            double val = device.Status.Oee.Availability * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }

        // Set Performance
        txt = (TextView) findViewById(R.id.Performance);
        if (txt != null) {

            double val = device.Status.Oee.Performance * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }
    }


    private void updateControllerStatus(Device d) {

        updateControllerStatus_EmergencyStop(d);
        updateControllerStatus_ControllerMode(d);
        updateControllerStatus_ExecutionMode(d);
        updateControllerStatus_Alarm(d);
    }

    private void updateControllerStatus_EmergencyStop(Device d) {

//        TextView txt = (TextView)findViewById(R.id.EmergencyStop);
//        if (txt != null) {
//            txt.setText(d.Status.EmergencyStop);
//
//            if (d.Status.EmergencyStop.equals("ARMED"))
//                txt.setTextColor(getResources().getColor(R.color.statusGreen));
//            else if (d.Status.EmergencyStop.equals("TRIGGERED"))
//                txt.setTextColor(getResources().getColor(R.color.statusRed));
//            else
//                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
//        }
    }

    private void updateControllerStatus_ControllerMode(Device d) {

//        TextView txt = (TextView)findViewById(R.id.ControllerMode);
//        if (txt != null) {
//            txt.setText(d.Status.ControllerMode);
//
//            if (d.Status.ControllerMode.equals("AUTOMATIC"))
//                txt.setTextColor(getResources().getColor(R.color.statusGreen));
//            else
//                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
//        }
    }

    private void updateControllerStatus_ExecutionMode(Device d) {

//        TextView txt = (TextView)findViewById(R.id.ExecutionMode);
//        if (txt != null) {
//            txt.setText(d.Status.ExecutionMode);
//
//            if (d.Status.ExecutionMode.equals("ACTIVE"))
//                txt.setTextColor(getResources().getColor(R.color.statusGreen));
//            else if (d.Status.ExecutionMode.equals("STOPPED") || d.Status.ExecutionMode.equals("INTERRUPTED"))
//                txt.setTextColor(getResources().getColor(R.color.statusRed));
//            else
//                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
//        }
    }

    private void updateControllerStatus_Alarm(Device d) {

//        TextView txt = (TextView)findViewById(R.id.Alarm);
//        if (txt != null) {
//            txt.setText(d.Status.SystemMessage);
//
//            if (d.Status.SystemStatus.equals("FAULT"))
//                txt.setTextColor(getResources().getColor(R.color.statusRed));
//            else
//                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
//        }
    }


    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Device Details");
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        setSupportActionBar(trakhoundToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:

                refresh();

                break;

            default:
                break;
        }

        return true;
    }

}
