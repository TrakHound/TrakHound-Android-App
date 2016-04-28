package org.trakhound.www.trakhound;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.trakhound.www.trakhound.devices.Device;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceListActivity extends Activity {

    private ListView deviceListView ;
    private DeviceListAdapter listAdapter ;


//    ArrayList<String> litItems = new ArrayList<String>();
//    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        // Set local variable to Id of ListView in layout
        deviceListView = (ListView) findViewById( R.id.device_list);


        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};



//        planetList.addAll(Arrays.asList(planets));



      // Load devices
//      Device[] devices = ((MyApplication) this.getApplication()).Devices;
//      if (devices != null && devices.length > 0)
//      {
//          ArrayList<String> deviceList = new ArrayList<String>();
//
//          // Initialize ArrayAdapter
//          listAdapter = new ArrayAdapter<String>(this, R.layout.layout_device_item, deviceList);
//
//          for (int i = 0; i < devices.length; i++) {
//
//              Device device = devices[i];
//              String uniqueId = device.UniqueId;
//
//              listAdapter.add(uniqueId);
//          }
//      }

        Device[] devices = ((MyApplication) this.getApplication()).Devices;
        if (devices != null && devices.length > 0)
        {
            ArrayList<Device> deviceList = new ArrayList<Device>();

            // Initialize ArrayAdapter
            listAdapter = new DeviceListAdapter(this, deviceList);

            for (int i = 0; i < devices.length; i++) {

                Device device = devices[i];
//                String uniqueId = device.UniqueId;

                listAdapter.add(device);
            }
        }

        // Set the ArrayAdapter as the ListView's adapter.
        deviceListView.setAdapter(listAdapter);




//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
