// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.trakhound.www.trakhound.device_list.*;
import org.trakhound.www.trakhound.device_list.StatusHandler;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.tools.SwipeDetector;
import org.trakhound.www.trakhound.users.GetUserImage;
import org.trakhound.www.trakhound.users.Logout;
import org.trakhound.www.trakhound.users.UserConfiguration;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ListView deviceListView;
    public ListAdapter listAdapter;

    Thread statusThread;
    Context context;
    Toolbar toolbar;

    SwipeDetector swipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list2);

        context = this;

        swipeDetector = new SwipeDetector();

        Log.d("test", "onCreate");

        // Setup Toolbar/ActionBar
        setToolbar();

        // Setup Navigation Drawer
        setNavigationDrawer();

        // Load Devices
        ListItem[] listItems = MyApplication.ListItems;
        if (listItems == null) {

            Log.d("test", "devices == null");

            loadDevices();

        } else {

            Log.d("test", "devices != null");

            addDevices();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (statusThread != null) statusThread.interrupt();

        Log.d("test","onStop");
    }


    private void loadDevices() {

        ProgressDialog progress = new ProgressDialog(this);

        new GetDevices(this, progress).execute();

        progress.setTitle("Loading Devices");
        progress.setMessage("Please Wait...");
        progress.show();
    }

    private void refresh() {

        ProgressDialog progress = new ProgressDialog(this);

        new GetDevices(this, progress).execute();

        progress.setTitle("Refreshing");
        progress.setMessage("Please Wait...");
        progress.show();
    }

    public void updateStatus(DeviceStatus[] deviceStatus) {

        final DeviceStatus[] statuses = deviceStatus;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    for (int i = 0; i < statuses.length; i++) {

                        DeviceStatus status = statuses[i];
                        String uniqueId = status.UniqueId;

                        int listIndex = getListIndex(uniqueId);
                        if (listIndex >= 0) {

                            ListItem listItem = listAdapter.getItem(listIndex);
                            listItem.Status = status;
                        }
                    }

                    listAdapter.notifyDataSetChanged();

                } catch (Exception ex) { }
            }
        });
    }

    private int getListIndex(String uniqueId) {

        Device[] devices = MyApplication.Devices;
        if (devices != null) {

            for (int i = 0; i < devices.length; i++) {

                if (devices[i].UniqueId.equals(uniqueId)) return i;
            }
        }

        return -1;
    }

    public void addDevices() {

        // Set local variable to Id of ListView in layout
        deviceListView = (ListView) findViewById(R.id.device_list);
        if (deviceListView != null) {

            // Set onClick listener
            deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Intent intent = new Intent(context, DeviceDetails.class);

                    // Pass the index of the device in the MyApplication.Devices array
//                    intent.putExtra(DeviceDetails.DEVICE_INDEX, position);
                    intent.putExtra(DeviceDetails.DEVICE_INDEX, position);

                    context.startActivity(intent);
                }
            });

            //deviceListView.setOnTouchListener(swipeDetector);

            ListItem[] listItems = MyApplication.ListItems;
            if (listItems != null && listItems.length > 0) {

                ArrayList<ListItem> itemList = new ArrayList<>();

                // Initialize ArrayAdapter
                listAdapter = new ListAdapter(this, itemList);

                listAdapter.addAll(listItems);

                // Set the ArrayAdapter as the ListView's adapter.
                deviceListView.setAdapter(listAdapter);
            }

            if (statusThread == null) startStatusThread();
        }
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

        ProgressDialog progress = new ProgressDialog(this);

        new Logout(this, progress).execute();

        progress.setTitle("Logging Out");
        progress.setMessage("Please Wait...");
        progress.show();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id == R.id.action_refresh) {

            refresh();

//            loadDevices();

//            if (connected) loadDevices();
//            else refreshStatus();
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion
}
