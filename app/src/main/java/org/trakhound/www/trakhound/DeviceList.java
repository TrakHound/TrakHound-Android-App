package org.trakhound.www.trakhound;

import android.app.Activity;
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

import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.devices.GetDeviceStatus;
import org.trakhound.www.trakhound.devices.GetDevices;
import org.trakhound.www.trakhound.tools.SwipeDetector;
import org.trakhound.www.trakhound.users.GetUserImage;
import org.trakhound.www.trakhound.users.Logout;
import org.trakhound.www.trakhound.users.UserConfiguration;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ListView deviceListView;
    public DeviceListAdapter listAdapter;

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
        Device[] devices = ((MyApplication) this.getApplication()).Devices;
        if (devices == null) {

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

    private void refreshStatus() {

        ProgressDialog progress = new ProgressDialog(this);

        new GetDeviceStatus(this, progress).execute();

        progress.setTitle("Refreshing");
        progress.setMessage("Please Wait...");
        progress.show();
    }

    public void updateStatus(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    listAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                }
            }
        });
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

                    Intent intent = new Intent(context, DeviceDetailsActivity.class);

                    // Pass the index of the device in the MyApplication.Devices array
                    intent.putExtra(DeviceDetailsActivity.DEVICE_INDEX, position);

                    context.startActivity(intent);
                }
            });

            //deviceListView.setOnTouchListener(swipeDetector);

            // Add each device found in static Devices array
            Device[] devices = ((MyApplication) this.getApplication()).Devices;
            if (devices != null && devices.length > 0) {
                ArrayList<Device> deviceList = new ArrayList<Device>();

                // Initialize ArrayAdapter
                listAdapter = new DeviceListAdapter(this, deviceList);

                for (int i = 0; i < devices.length; i++) {

                    Device device = devices[i];
                    listAdapter.add(device);
                }
            }

            // Set the ArrayAdapter as the ListView's adapter.
            deviceListView.setAdapter(listAdapter);

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










    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Device List");
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);


        // Set Navigation Button Icon
        //trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        toolbar = trakhoundToolbar;

        setSupportActionBar(trakhoundToolbar);
    }

    private void setNavigationDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Load Username
        UserConfiguration userConfig = ((MyApplication)((Activity)context).getApplication()).User;
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
            refreshStatus();

            if (connected) loadDevices();
            else refreshStatus();
        }

        return super.onOptionsItemSelected(item);
    }






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//
//                refreshStatus();
//
//                if (connected) loadDevices();
//                else refreshStatus();
//
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }







    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            logout();
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
