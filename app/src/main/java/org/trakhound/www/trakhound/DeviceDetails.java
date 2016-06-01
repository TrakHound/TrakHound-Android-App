// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import org.trakhound.www.trakhound.users.GetUserImage;
import org.trakhound.www.trakhound.users.Logout;
import org.trakhound.www.trakhound.users.UserConfiguration;

import java.util.Timer;
import java.util.TimerTask;

public class DeviceDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String DEVICE_INDEX = "device_index";

    Toolbar toolbar;

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

        // Setup Toolbar/ActionBar
        setToolbar();

        // Setup Navigation Drawer
        setNavigationDrawer();
    }

    public void loadDevice(Device d) {

        loadImages(d);

        loadDescription(d);

        refresh();

        if (statusThread == null) startStatusThread();
    }

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
    }


    public void refresh() {

        if (Status != null) {

            updateProductionStatus(Status);
            updateOeeStatus(Status);
            updateControllerStatus(Status);
        }
    }

    private void updateProductionStatus(DeviceStatus status) {

        if (status.Production != null) {

            // Current Status Indicator
            View banner = findViewById(R.id.DeviceStatusIndicator);
            if (banner != null) {

                if (status.Production.Alert) {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusRed));
                } else if (status.Production.Idle) {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusYellow));
                } else {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusGreen));
                }
            }

            // Current Status Text
            TextView txt = (TextView)findViewById(R.id.DeviceStatusText);
            txt.setText(status.Production.ProductionStatus);

            // Current Status Timer
            int seconds = Integer.parseInt(status.Production.ProductionStatusTimer);
            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

            txt = (TextView)findViewById(R.id.DeviceStatusTime);
            txt.setText(statusPeriod);

        }





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

            updateControllerStatus_Availability(status);
            updateControllerStatus_EmergencyStop(status);
            updateControllerStatus_ControllerMode(status);
            updateControllerStatus_ExecutionMode(status);
            updateControllerStatus_SystemStatus(status);
        }
    }

    private void updateControllerStatus_Availability(DeviceStatus status) {

        boolean avail = status.Controller.Availability.equals("AVAILABLE");

        // Set Power On Background
        View bkgrd = findViewById(R.id.PowerOnBackground);
        if (bkgrd != null) {

            if (avail) bkgrd.setBackgroundColor(getResources().getColor(R.color.statusGreen));
            else bkgrd.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set Power Off Background
        bkgrd = findViewById(R.id.PowerOffBackground);
        if (bkgrd != null) {

            if (avail) bkgrd.setBackgroundColor(Color.TRANSPARENT);
            else bkgrd.setBackgroundColor(getResources().getColor(R.color.statusRed));
        }


        // Set Power On Image
        ImageView img = (ImageView)findViewById(R.id.PowerOnImage);
        if (img != null) {

            if (avail) img.setColorFilter(Color.WHITE);
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // Set Power Off Image
        img = (ImageView)findViewById(R.id.PowerOffImage);
        if (img != null) {

            if (avail) img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
            else img.setColorFilter(Color.WHITE);
        }


        // Set Text
        TextView txt = (TextView)findViewById(R.id.AvailabilityText);
        if (txt != null) {

            txt.setText(status.Controller.Availability);

            if (avail) txt.setTextColor(getResources().getColor(R.color.statusGreen));
            else txt.setTextColor(getResources().getColor(R.color.statusRed));
        }
    }

    private void updateControllerStatus_EmergencyStop(DeviceStatus status) {

        // Set color based on value
        int color = getResources().getColor(R.color.foreground_normal_color);

        if (status.Controller.EmergencyStop.equals("ARMED"))
            color = getResources().getColor(R.color.statusGreen);

        else if (status.Controller.EmergencyStop.equals("TRIGGERED"))
            color = getResources().getColor(R.color.statusRed);


        // Set Image
        ImageView img = (ImageView)findViewById(R.id.EmergencyStopImage);
        if (img != null) img.setColorFilter(color);

        // Set Text
        TextView txt = (TextView)findViewById(R.id.EmergencyStopText);
        if (txt != null) {
            txt.setText(status.Controller.EmergencyStop);
            txt.setTextColor(color);
        }
    }

    private void updateControllerStatus_ControllerMode(DeviceStatus status) {

        String s = status.Controller.ControllerMode;

        // Automatic
        ImageView img = (ImageView)findViewById(R.id.ControllerMode_Auto);
        if (img != null)
        {
            if (s.equals("AUTOMATIC")) img.setColorFilter(getResources().getColor(R.color.statusGreen));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // MDI (Manual Data Input)
        img = (ImageView)findViewById(R.id.ControllerMode_MDI);
        if (img != null)
        {
            if (s.equals("MANUAL_DATA_INPUT")) img.setColorFilter(getResources().getColor(R.color.accent_normal_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // SBLK (Single Block Mode)
        img = (ImageView)findViewById(R.id.ControllerMode_SBLK);
        if (img != null)
        {
            if (s.equals("SEMI_AUTOMATIC")) img.setColorFilter(getResources().getColor(R.color.accent_normal_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // Manual (Jog, etc.)
        img = (ImageView)findViewById(R.id.ControllerMode_Manaul);
        if (img != null)
        {
            if (s.equals("MANUAL")) img.setColorFilter(getResources().getColor(R.color.accent_normal_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // Edit
        img = (ImageView)findViewById(R.id.ControllerMode_Edit);
        if (img != null)
        {
            if (s.equals("EDIT")) img.setColorFilter(getResources().getColor(R.color.accent_normal_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }
    }

    private void updateControllerStatus_ExecutionMode(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.ExecutionModeText);
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

    private void updateControllerStatus_SystemStatus(DeviceStatus status) {

        // Set System Status
        TextView txt = (TextView)findViewById(R.id.SystemStatusText);
        if (txt != null) {

            String s1 = status.Controller.SystemStatus;

            txt.setText(s1);

            if (s1.equals("Normal"))

                txt.setTextColor(getResources().getColor(R.color.statusGreen));

            else if (s1.equals("Fault"))

                txt.setTextColor(getResources().getColor(R.color.statusRed));

            else

                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));


            // Set System Message
            txt = (TextView)findViewById(R.id.SystemMessageText);
            if (txt != null) {

                String s2 = status.Controller.SystemMessage;

                // Set System Message Text
                txt.setText(s2);

                // Set Visibility of System Message
                if (s2 == null || s2.isEmpty()) txt.setVisibility(View.GONE);
                else txt.setVisibility(View.VISIBLE);

                // Set Colors
                if (!s1.equals("Normal")) {

                    txt.setTextColor(Color.WHITE);
                    txt.setBackgroundColor(Color.RED);

                } else {

                    txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
                    txt.setBackgroundColor(Color.YELLOW);
                }
            }
        }
    }


    public void logout(){

        Loading.Open(this, "Logging Out..");

        new Logout(this).execute();
    }


    //region Toolbar

    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Device Details");
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        toolbar = trakhoundToolbar;

        setSupportActionBar(trakhoundToolbar);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_refresh) {

            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Navigation Drawer

    private void setNavigationDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Load Username
        UserConfiguration userConfig = MyApplication.User;
        if (userConfig != null) {

            String username = TH_Tools.capitalizeFirst(userConfig.Username);

            View headerView = navigationView.getHeaderView(0);
            if (headerView != null) {

                TextView txt = (TextView) headerView.findViewById(R.id.Username);
                if (txt != null) txt.setText(username);

                new GetUserImage(headerView, userConfig).execute();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            logout();

        } else if (id == R.id.nav_about) {

            // Open the About Page
            startActivity(new Intent(getBaseContext(), About.class));

        } else if (id == R.id.nav_deviceList) {

            // Open the Device Details Page
            startActivity(new Intent(getBaseContext(), DeviceList.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion


}
