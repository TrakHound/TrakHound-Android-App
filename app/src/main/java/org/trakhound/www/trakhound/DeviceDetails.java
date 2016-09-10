// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.Period;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.data.TimersInfo;
import org.trakhound.www.trakhound.device_details.DeviceStatus;
import org.trakhound.www.trakhound.device_details.GetDeviceStatus;
import org.trakhound.www.trakhound.device_details.StatusHandler;
import org.trakhound.www.trakhound.device_list.ListItem;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.api.users.UserConfiguration;

public class DeviceDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String DEVICE_INDEX = "device_index";

    public static DeviceStatus deviceStatus;

    private Toolbar toolbar;
    private Thread statusThread;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        MyApplication.setCurrentActivity(this);

        hideLoading();

        Intent intent = this.getIntent();

        int deviceIndex = intent.getIntExtra(DEVICE_INDEX, 0);

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null) {

            ListItem listItem = listItems[deviceIndex];

            loadDevice(listItem);
        }

        // Set Status Bar Color
        setStatusBar();

        // Setup Toolbar/ActionBar
        setToolbar();

        // Setup Navigation Drawer
        setNavigationDrawer();
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (statusThread != null) statusThread.interrupt();
    }

    public void loadDevice(ListItem item) {

        if (deviceStatus == null) deviceStatus = new DeviceStatus();

        deviceStatus.uniqueId = item.uniqueId;
        deviceStatus.statusInfo = item.statusInfo;
        deviceStatus.controllerInfo = item.controllerInfo;
        deviceStatus.oeeInfo = item.oeeInfo;
        deviceStatus.timersInfo = item.timersInfo;

        //loadImages(d);

        loadDescription(item);

        loadData();

        if (statusThread == null) startStatusThread(item);
    }

    private void startStatusThread(ListItem listItem) {

        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this, handler, listItem);
        statusThread = new Thread(statusHandler);
        statusThread.start();
    }

//    private void loadImages(Device d) {
//
//        ImageView img;
//
//        img = (ImageView) findViewById(R.id.DeviceImage);
//        if (img != null) img.setImageBitmap(d.Image);
//
//        img = (ImageView) findViewById(R.id.DeviceLogo);
//        if (img != null) img.setImageBitmap(d.Logo);
//    }

    private void loadDescription(ListItem item) {

        TextView txt;

        txt = (TextView) findViewById(R.id.Description);
        if (txt != null) txt.setText(item.descriptionInfo.description);

        txt = (TextView) findViewById(R.id.DeviceId);
        if (txt != null) txt.setText(item.descriptionInfo.deviceId);

        txt = (TextView) findViewById(R.id.Manufacturer);
        if (txt != null) txt.setText(item.descriptionInfo.manufacturer);

        txt = (TextView) findViewById(R.id.Model);
        if (txt != null) txt.setText(item.descriptionInfo.model);

        txt = (TextView) findViewById(R.id.Serial);
        if (txt != null) txt.setText(item.descriptionInfo.serial);

    }

    public void loadData() {

        if (deviceStatus != null) {

            updateStatus(deviceStatus);

            updateOeeStatus(deviceStatus);

            updateDevicePercentages(deviceStatus);

            updateControllerStatus(deviceStatus);
        }
    }

    public void refresh() {

        if (deviceStatus != null) new GetDeviceStatus(this, deviceStatus.uniqueId).execute();
    }

    private void updateStatus(DeviceStatus status) {

        if (status.statusInfo == null) status.statusInfo = new StatusInfo();

        if (status.statusInfo.deviceStatus != null) {

            String deviceStatus = status.statusInfo.deviceStatus;

            // Current Status Indicator
            View banner = findViewById(R.id.DeviceStatusIndicator);
            if (banner != null) {

                if (deviceStatus.equals("Alert")) {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusRed));
                } else if (deviceStatus.equals("Idle")) {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusOrange));
                } else {
                    banner.setBackgroundColor(getResources().getColor(R.color.statusGreen));
                }
            }

            // Current Status Text
            TextView txt = (TextView)findViewById(R.id.DeviceStatusText);
            if (txt != null) txt.setText(deviceStatus);

            // Current Status Timer
            Period period = new Period(status.statusInfo.deviceStatusTimer * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

            txt = (TextView)findViewById(R.id.DeviceStatusTime);
            if (txt != null) txt.setText(statusPeriod);

        } else clearStatus();
    }

    private void clearStatus() {

        View banner = findViewById(R.id.DeviceStatusIndicator);
        if (banner != null) banner.setBackgroundColor(Color.TRANSPARENT);

        TextView txt = (TextView)findViewById(R.id.DeviceStatusText);
        if (txt != null) txt.setText("");

        txt = (TextView)findViewById(R.id.DeviceStatusTime);
        if (txt != null) txt.setText("");
    }

    private void updateDevicePercentages(DeviceStatus status) {

        // Percentages
        if (status.timersInfo != null && status.timersInfo.total > 0) {

            TimersInfo info = status.timersInfo;

            double active = (info.active / info.total) * 100;
            double idle = (info.idle / info.total) * 100;
            double alert = (info.alert / info.total) * 100;

            // Progress Bars

            // Active
            ProgressBar pb = (ProgressBar)findViewById(R.id.ActiveProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(active));

            // Idle
            pb = (ProgressBar)findViewById(R.id.IdleProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(idle));

            // Alert
            pb = (ProgressBar)findViewById(R.id.AlertProgressBar);
            if (pb != null) pb.setProgress((int)Math.round(alert));


            // Percentage TextViews

            // Active
            TextView txt = (TextView)findViewById(R.id.ActivePercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", active));

            // Idle
            txt = (TextView)findViewById(R.id.IdlePercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", idle));

            // Alert
            txt = (TextView)findViewById(R.id.AlertPercentage);
            if (txt != null) txt.setText(String.format("%.0f%%", alert));

            // Time Elapsed TextViews

            // Active
            Integer seconds = Integer.valueOf((int) Math.round(info.active));
            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.ActiveTime);
            if (txt != null) txt.setText(statusPeriod);

            // Idle
            seconds = Integer.valueOf((int) Math.round(info.idle));
            period = new Period(seconds * 1000);
            statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.IdleTime);
            if (txt != null) txt.setText(statusPeriod);

            // Alert
            seconds = Integer.valueOf((int) Math.round(info.alert));
            period = new Period(seconds * 1000);
            statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());
            txt = (TextView)findViewById(R.id.AlertTime);
            if (txt != null) txt.setText(statusPeriod);

        } else clearDevicePercentages();
    }

    private void clearDevicePercentages() {

        // Active
        TextView txt = (TextView)findViewById(R.id.ActivePercentage);
        if (txt != null) txt.setText("");
        txt = (TextView)findViewById(R.id.ActiveTime);
        if (txt != null) txt.setText("");

        // Idle
        txt = (TextView)findViewById(R.id.IdlePercentage);
        if (txt != null) txt.setText("");
        txt = (TextView)findViewById(R.id.IdleTime);
        if (txt != null) txt.setText("");

        // Alert
        txt = (TextView)findViewById(R.id.AlertPercentage);
        if (txt != null) txt.setText("");
        txt = (TextView)findViewById(R.id.AlertTime);
        if (txt != null) txt.setText("");
    }

    private void updateOeeStatus(DeviceStatus status) {

        if (status.oeeInfo != null) {

            // Set OEE
            double val = status.oeeInfo.oee * 100;
            String s = String.format("%.0f%%", val);
            TextView txt = (TextView) findViewById(R.id.OEE);
            if (txt != null) txt.setText(s);

            // Set Availability
            val = status.oeeInfo.availability * 100;
            s = String.format("%.0f%%", val);
            txt = (TextView) findViewById(R.id.AvailabilityVariable);
            if (txt != null) txt.setText(s);

            // Set Performance
            val = status.oeeInfo.performance * 100;
            s = String.format("%.0f%%", val);
            txt = (TextView) findViewById(R.id.Performance);
            if (txt != null) txt.setText(s);

        } else clearOeeStatus();
    }

    private void clearOeeStatus() {

        TextView txt = (TextView) findViewById(R.id.OEE);
        if (txt != null) txt.setText("");

        txt = (TextView) findViewById(R.id.AvailabilityVariable);
        if (txt != null) txt.setText("");

        txt = (TextView) findViewById(R.id.Performance);
        if (txt != null) txt.setText("");
    }


    private void updateControllerStatus(DeviceStatus status) {

        if (status != null && status.controllerInfo != null) {

            updateControllerStatus_Availability(status);
            updateControllerStatus_EmergencyStop(status);
            updateControllerStatus_ControllerMode(status);
            updateControllerStatus_ExecutionMode(status);
            updateControllerStatus_SystemStatus(status);
            updateControllerStatus_CurrentProgram(status);
        }
    }

    private void updateControllerStatus_Availability(DeviceStatus status) {

        boolean avail = status.controllerInfo.availability.equals("AVAILABLE");

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

            txt.setText(status.controllerInfo.availability);

            if (avail) txt.setTextColor(getResources().getColor(R.color.statusGreen));
            else txt.setTextColor(getResources().getColor(R.color.statusRed));
        }
    }

    private void updateControllerStatus_EmergencyStop(DeviceStatus status) {

        // Set color based on value
        int color = getResources().getColor(R.color.foreground_normal_color);

        if (status.controllerInfo.emergencyStop.equals("ARMED"))
            color = getResources().getColor(R.color.statusGreen);

        else if (status.controllerInfo.emergencyStop.equals("TRIGGERED"))
            color = getResources().getColor(R.color.statusRed);


        // Set Image
        ImageView img = (ImageView)findViewById(R.id.EmergencyStopImage);
        if (img != null) img.setColorFilter(color);

        // Set Text
        TextView txt = (TextView)findViewById(R.id.EmergencyStopText);
        if (txt != null) {
            txt.setText(status.controllerInfo.emergencyStop);
            txt.setTextColor(color);
        }
    }

    private void updateControllerStatus_ControllerMode(DeviceStatus status) {

        String s = status.controllerInfo.controllerMode;

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
            if (s.equals("MANUAL_DATA_INPUT")) img.setColorFilter(getResources().getColor(R.color.accent_light_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // SBLK (Single Block Mode)
        img = (ImageView)findViewById(R.id.ControllerMode_SBLK);
        if (img != null)
        {
            if (s.equals("SEMI_AUTOMATIC")) img.setColorFilter(getResources().getColor(R.color.accent_light_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // Manual (Jog, etc.)
        img = (ImageView)findViewById(R.id.ControllerMode_Manaul);
        if (img != null)
        {
            if (s.equals("MANUAL")) img.setColorFilter(getResources().getColor(R.color.accent_light_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }

        // Edit
        img = (ImageView)findViewById(R.id.ControllerMode_Edit);
        if (img != null)
        {
            if (s.equals("EDIT")) img.setColorFilter(getResources().getColor(R.color.accent_light_color));
            else img.setColorFilter(getResources().getColor(R.color.foreground_light_color));
        }
    }

    private void updateControllerStatus_ExecutionMode(DeviceStatus status) {

        TextView txt = (TextView)findViewById(R.id.ExecutionModeText);
        if (txt != null) {

            txt.setText(status.controllerInfo.executionMode);

            if (status.controllerInfo.executionMode.equals("ACTIVE"))

                txt.setTextColor(getResources().getColor(R.color.statusGreen));

            else if (status.controllerInfo.executionMode.equals("STOPPED") || status.controllerInfo.executionMode.equals("INTERRUPTED"))

                txt.setTextColor(getResources().getColor(R.color.statusRed));

            else

                txt.setTextColor(getResources().getColor(R.color.foreground_normal_color));
        }
    }

    private void updateControllerStatus_SystemStatus(DeviceStatus status) {

        // Set System Status
        TextView txt = (TextView)findViewById(R.id.SystemStatusText);
        if (txt != null) {

            String s1 = status.controllerInfo.systemStatus;

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

                String s2 = status.controllerInfo.systemMessage;

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

    private void updateControllerStatus_CurrentProgram(DeviceStatus status) {

        if (status.controllerInfo != null) {

            // Set System Status
            String s = status.controllerInfo.programName;
            TextView txt = (TextView)findViewById(R.id.CurrentProgramText);
            if (txt != null) txt.setText(s);

        } else clearControllerStatus_CurrentProgram();
    }

    private void clearControllerStatus_CurrentProgram() {

        TextView txt = (TextView)findViewById(R.id.CurrentProgramText);
        if (txt != null) txt.setText("");
    }


    public void logout(){

        Loading.Open(this, "Logging Out..");

        new Logout(this).execute();
    }

    //region Loading Overlay

    public void showLoading() {

        View v = findViewById(R.id.loadingOverlay);
        if (v != null) {

            v.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {

        View v = findViewById(R.id.loadingOverlay);
        if (v != null) {

            v.setVisibility(View.GONE);
        }
    }

    //endregion


    private void setStatusBar(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar_color));
        }
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

        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {

            // Load Username
            UserConfiguration userConfig = MyApplication.User;
            if (userConfig != null) {

                String username = TH_Tools.capitalizeFirst(userConfig.username);

                TextView txt = (TextView) headerView.findViewById(R.id.Username);
                if (txt != null) txt.setText(username);

//                new GetUserImage(headerView, userConfig).execute();
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
