// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.Period;
import org.trakhound.www.trakhound.device_details.DeviceStatus;
import org.trakhound.www.trakhound.device_details.GetDeviceStatus;
import org.trakhound.www.trakhound.device_list.ListItem;
import org.trakhound.www.trakhound.device_details.StatusHandler;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.status_info.OeeInfo;

import java.util.Timer;
import java.util.TimerTask;

public class DeviceDetails extends AppCompatActivity {

    public static String DEVICE_INDEX = "device_index";

    public Device Device;
    public DeviceStatus Status;

    private Thread statusThread;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        MyApplication.setCurrentActivity(this);

        Intent intent = this.getIntent();

        int deviceIndex = intent.getIntExtra(DEVICE_INDEX, 0);

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null) {

            ListItem listItem = listItems[deviceIndex];

            Device = listItem.Device;

            if (listItem.Status != null) {

                Status = new DeviceStatus();
                Status.Oee = listItem.Status.Oee;
            }

            loadDevice(Device);
        }

        setToolbar();
    }

    public void loadDevice(Device d) {

        loadImages(d);

        loadDescription(d);

        refresh();

//        startStatusTimer();

        if (statusThread == null) startStatusThread();
    }

//    private void startStatusTimer() {
//
//        if (Device != null) {
//
//            final String uniqueId = Device.UniqueId;
//
////            Timer timer = new Timer();
////            timer.scheduleAtFixedRate(new TimerTask() {
////                @Override
////                public void run() {
////
////                    new GetDeviceStatus(this, uniqueId).execute();
////
////                }
////            }, 10000, 10000);
////            timer.scheduleAtFixedRate(new TimerTask() {
////                @Override
////                public void run() {
////
////                    new GetDeviceStatus(this, uniqueId).execute();
////
////                }
////            }, 10000, 10000);
//        }
//    }

    private void startStatusThread() {

        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this, handler, Device);
        statusThread = new Thread(statusHandler);
        statusThread.start();
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

//        txt = (TextView) findViewById(R.id.Manufacturer);
//        if (txt != null) txt.setText(d.Manufacturer);
//
//        txt = (TextView) findViewById(R.id.Model);
//        if (txt != null) txt.setText(d.Model);
//
//        txt = (TextView) findViewById(R.id.Serial);
//        if (txt != null) txt.setText(d.Serial);
//
//        txt = (TextView) findViewById(R.id.Controller);
//        if (txt != null) txt.setText(d.Controller);
    }


    public void refresh() {

        if (Status != null) {

            updateProductionStatus(Status);
            updateOeeStatus(Status);
            updateControllerStatus(Status);
        }
    }

    private void updateProductionStatus(DeviceStatus status) {

        // Banner
//        View banner = findViewById(R.id.StatusBanner);
//        if (banner != null) {
//
//            if (status.Production.Alert) {
//                banner.setBackgroundColor(getResources().getColor(R.color.statusRed));
//            } else if (status.Production.Idle) {
//                banner.setBackgroundColor(getResources().getColor(R.color.statusYellow));
//            } else {
//                banner.setBackgroundColor(getResources().getColor(R.color.statusGreen));
//            }
//        }

//        ImageView img = (ImageView)findViewById(R.id.AlertIndicator);
//        if (img != null) {
//            if (d.Status.Status.Alert) img.setVisibility(View.VISIBLE);
//            else img.setVisibility(View.INVISIBLE);
//        }


        // Percentages
        if (status.Timers != null && status.Timers.Total > 0) {

            double production = (status.Timers.Production / status.Timers.Total) * 100;
            double idle = (status.Timers.Idle / status.Timers.Total) * 100;
            double alert = (status.Timers.Alert / status.Timers.Total) * 100;

            // Progress Bars

            // Production
            ProgressBar pb = (ProgressBar)findViewById(R.id.ProductionProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(production));

            // Idle
            pb = (ProgressBar)findViewById(R.id.IdleProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(idle));

            // Alert
            pb = (ProgressBar)findViewById(R.id.AlertProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(alert));


            // Percentage TextViews

            // Production
            TextView txt = (TextView)findViewById(R.id.ProductionPercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", production));

            // Idle
            txt = (TextView)findViewById(R.id.IdlePercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", idle));

            // Alert
            txt = (TextView)findViewById(R.id.AlertPercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", alert));

            // Time Elapsed TextViews

            // Production
            Integer seconds = Integer.valueOf((int) Math.round(status.Timers.Production));
            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.ProductionTime);
            txt.setText(statusPeriod);

            // Idle
            seconds = Integer.valueOf((int) Math.round(status.Timers.Idle));
            period = new Period(seconds * 1000);
            statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.IdleTime);
            txt.setText(statusPeriod);

            // Alert
            seconds = Integer.valueOf((int) Math.round(status.Timers.Alert));
            period = new Period(seconds * 1000);
            statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.AlertTime);
            txt.setText(statusPeriod);

        }
    }


    private void updateOeeStatus(DeviceStatus status) {

        if (status.Oee == null) status.Oee = new OeeInfo();

        // Set OEE
        TextView txt = (TextView) findViewById(R.id.OEE);
        if (txt != null) {

            double val = status.Oee.Oee * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }

        // Set Availability
        txt = (TextView) findViewById(R.id.AvailabilityVariable);
        if (txt != null) {

            double val = status.Oee.Availability * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }

        // Set Performance
        txt = (TextView) findViewById(R.id.Performance);
        if (txt != null) {

            double val = status.Oee.Performance * 100;
            String s = String.format("%.0f%%", val);
            txt.setText(s);
        }
    }


    private void updateControllerStatus(DeviceStatus status) {

        if (status != null && status.Controller != null) {

            updateControllerStatus_EmergencyStop(status);
            updateControllerStatus_ControllerMode(status);
            updateControllerStatus_ExecutionMode(status);
            updateControllerStatus_Alarm(status);
        }
    }

    private void updateControllerStatus_EmergencyStop(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.EmergencyStop);
        if (txt != null) {
            txt.setText(status.Controller.EmergencyStop);

            if (status.Controller.EmergencyStop.equals("ARMED"))
                txt.setTextColor(getResources().getColor(R.color.statusGreen));
            else if (status.Controller.EmergencyStop.equals("TRIGGERED"))
                txt.setTextColor(getResources().getColor(R.color.statusRed));
            else
                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
        }
    }

    private void updateControllerStatus_ControllerMode(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.ControllerMode);
        if (txt != null) {
            txt.setText(status.Controller.ControllerMode);

            if (status.Controller.ControllerMode.equals("AUTOMATIC"))
                txt.setTextColor(getResources().getColor(R.color.statusGreen));
            else
                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
        }
    }

    private void updateControllerStatus_ExecutionMode(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.ExecutionMode);
        if (txt != null) {
            txt.setText(status.Controller.ExecutionMode);

            if (status.Controller.ExecutionMode.equals("ACTIVE"))
                txt.setTextColor(getResources().getColor(R.color.statusGreen));
            else if (status.Controller.ExecutionMode.equals("STOPPED") || status.Controller.ExecutionMode.equals("INTERRUPTED"))
                txt.setTextColor(getResources().getColor(R.color.statusRed));
            else
                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
        }
    }

    private void updateControllerStatus_Alarm(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.Alarm);
        if (txt != null) {
            txt.setText(status.Controller.SystemMessage);

            if (status.Controller.SystemStatus.equals("FAULT"))
                txt.setTextColor(getResources().getColor(R.color.statusRed));
            else
                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
        }
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
