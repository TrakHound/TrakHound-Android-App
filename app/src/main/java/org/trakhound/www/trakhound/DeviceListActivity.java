package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import org.trakhound.www.trakhound.devices.Device;

public class DeviceListActivity extends AppCompatActivity {

    public ListView deviceListView;
    public DeviceListAdapter listAdapter;

    Handler statusH = new Handler();

    Thread statusThread;

    Context context;


    public void updateStatus(String msg) {

        final String str = msg;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        context = this;

        Log.d("test", "onCreate");

        // Setup Toolbar/ActionBar
        SetToolbar();

        AddDevices();

        // Start Status Updates on separate thread
        StatusHandler statusHandler = new StatusHandler(this, statusH);
        statusThread = new Thread(statusHandler);
        statusThread.start();
    }

    @Override
    protected void onResume() {

        super.onResume();

        Log.d("test", "onResume");
    }

    @Override
    protected void onStart() {

        super.onStart();

        Log.d("test","onStart");
    }

    @Override
    protected void onRestart() {

        super.onRestart();

        Log.d("test","onRestart");
    }

    @Override
    protected void onPause() {

        super.onPause();

        Log.d("test","onPause");
    }

    @Override
    protected void onStop() {

        super.onStop();

        Log.d("test","onStop");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Log.d("test", "onDestroy");
    }






    private void AddDevices() {

        // Set local variable to Id of ListView in layout
        deviceListView = (ListView) findViewById(R.id.device_list);

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

        // Add each device found in static Devices array
        Device[] devices = ((MyApplication) this.getApplication()).Devices;
        if (devices != null && devices.length > 0) {
            ArrayList<Device> deviceList = new ArrayList<Device>();

            // Initialize ArrayAdapter
            listAdapter = new DeviceListAdapter(this, deviceList);


            for (int i = 0; i < devices.length; i++) {

                Device device = devices[i];
//                if (device.Enabled) listAdapter.add(device);
                listAdapter.add(device);
            }
        }

        // Set the ArrayAdapter as the ListView's adapter.
        deviceListView.setAdapter(listAdapter);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//
////                Device.readAll()
//
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }

}
