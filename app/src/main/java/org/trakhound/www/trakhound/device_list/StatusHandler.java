// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.api.users.UserConfiguration;


/**
 * Created by Patrick on 4/28/2016.
 */
public class StatusHandler implements Runnable {

    private Context context;

    public StatusHandler(Context context) {

        this.context = context;
    }


    @Override
    public void run() {

        boolean exit = false;

        while (!exit) {

            try
            {
                boolean connected = isNetworkConnected();

                // Only run if connected to Wifi or Ethernet
                if (connected) {

                    UserConfiguration user = MyApplication.User;
                    //Device[] devices = MyApplication.Devices;

                   // if (user != null && devices != null) {
                    if (user != null) {

                        DeviceStatus[] statuses = DeviceStatus.get(user);
                        if (statuses != null) {

                            ((DeviceList) context).updateStatus(statuses);
                        }
                    }
                }

                ((DeviceList) context).updateConnectionStatus(connected);

                Thread.sleep(5000);
            }
            catch (InterruptedException ex) {  }
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected;

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) isConnected = true;
        else isConnected = false;

        boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        boolean isEthernet = activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET;

        return isConnected && (isWifi || isEthernet);
    }

}
