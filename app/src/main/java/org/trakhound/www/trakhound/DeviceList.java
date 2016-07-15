// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.trakhound.www.trakhound.device_details.GetDeviceStatus;
import org.trakhound.www.trakhound.device_list.*;
import org.trakhound.www.trakhound.device_list.StatusHandler;
import org.trakhound.www.trakhound.api.users.UserConfiguration;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ListView deviceListView;
    public ListAdapter listAdapter;

    Thread statusThread;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        MyApplication.setCurrentActivity(this);
        
        context = this;
        deviceListView = (ListView) findViewById(R.id.device_list);
        listAdapter = new ListAdapter(this, new ArrayList<ListItem>());
        deviceListView.setAdapter(listAdapter);

        // Set onClick listener
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Loading.Open(context, "Loading Details..");

                new GetDeviceStatus(context, position).execute();
            }
        });

        // Set Status Bar Color
        setStatusBar();

        // Setup Toolbar/ActionBar
        setToolbar();

        // Setup Navigation Drawer
        setNavigationDrawer();

        // Load Devices
        ListItem[] listItems = MyApplication.ListItems;
        if (listItems == null) {

            loadDevices();

        } else {

            addDevices();
        }

        // Load Device Logo Images
//        new GetLogos(this).execute();
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (statusThread != null) statusThread.interrupt();
    }


    private void loadDevices() {

        // Show Loading Activity
        Loading.Open(this, "Loading Devices..");

        new GetDevices(this, GetDevices.LoginType.NONE).execute();
    }

    private void refresh() {

        // Show Loading Overlay
        showLoading();

        new GetDevices(this, GetDevices.LoginType.NONE).execute();
    }

    public void updateStatus(DeviceStatus[] deviceStatus) {

        final DeviceStatus[] statuses = deviceStatus;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    for (int i = 0; i < statuses.length; i++) {

                        DeviceStatus status = statuses[i];
                        String uniqueId = status.uniqueId;

                        int listIndex = getListIndex(uniqueId);
                        if (listIndex >= 0) {

                            ListItem listItem = listAdapter.getItem(listIndex);
                            listItem.statusInfo = status.statusInfo;
                            listItem.oeeInfo = status.oeeInfo;
                        }
                    }

                    listAdapter.notifyDataSetChanged();

                } catch (Exception ex) { }
            }
        });
    }

    private int getListIndex(String uniqueId) {

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null) {

            for (int i = 0; i < listItems.length; i++) {

                if (listItems[i].uniqueId.equals(uniqueId)) return i;
            }
        }

        return -1;
    }

//    private int getListIndex(String uniqueId) {
//
//        Device[] devices = MyApplication.Devices;
//        if (devices != null) {
//
//            for (int i = 0; i < devices.length; i++) {
//
//                if (devices[i].UniqueId.equals(uniqueId)) return i;
//            }
//        }
//
//        return -1;
//    }

    public void addDevices() {

        listAdapter.clear();

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null && listItems.length > 0) {

            listAdapter.addAll(listItems);

            // Hide 'No Devices Found' text
            View v = findViewById(R.id.NoDevicesText);
            if (v != null) v.setVisibility(View.GONE);

        } else {

            // Show 'No Devices Found' text
            View v = findViewById(R.id.NoDevicesText);
            if (v != null) v.setVisibility(View.VISIBLE);
        }

        if (statusThread == null) startStatusThread();

        hideLoading();

    }

    private void startStatusThread() {

        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this);
        statusThread = new Thread(statusHandler);
        statusThread.start();
    }


    private boolean connected;

    public void updateConnectionStatus(boolean connected) {

        this.connected = connected;
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

        Loading.Close();

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
            window.setStatusBarColor(this.getResources().getColor(R.color.accent_normal_color));
        }
    }

    //region Toolbar

    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Device List");
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

                String username = null;

                if (userConfig.type == UserConfiguration.UserType.REMOTE) {

                    username = TH_Tools.capitalizeFirst(userConfig.username);
                }
                else {

                    username = TH_Tools.capitalizeFirst(userConfig.id);
                    username = username.substring(2);
                    username = username.toUpperCase();
                }

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion

}
