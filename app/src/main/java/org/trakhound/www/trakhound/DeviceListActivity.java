package org.trakhound.www.trakhound;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import java.util.ArrayList;

import org.trakhound.www.trakhound.devices.Device;

public class DeviceListActivity extends AppCompatActivity {

    private ListView deviceListView;
    public DeviceListAdapter listAdapter;

    Handler statusH = new Handler();

    Thread statusThread;


    public void updateStatus(String msg) {

        final String str = msg;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                listAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        // Setup Toolbar/ActionBar
        SetToolbar();


        // Set local variable to Id of ListView in layout
        deviceListView = (ListView) findViewById(R.id.device_list);

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


        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this, statusH);
        statusThread = new Thread(statusHandler);
        statusThread.start();

    }

    private void SetToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle(R.string.app_name);
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);



        // Set Navigation Button Icon
        trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        setSupportActionBar(trakhoundToolbar);
    }

}
