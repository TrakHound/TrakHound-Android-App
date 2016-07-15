// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import org.trakhound.www.trakhound.DeviceDetails;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.device_list.ListItem;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.api.users.UserConfiguration;


/**
 * Created by Patrick on 4/28/2016.
 */
public class StatusHandler implements Runnable {

    private DeviceDetails context;
    private Handler handler;
    private ListItem listItem;

    public StatusHandler(DeviceDetails context, Handler handler, ListItem listItem) {

        this.context = context;
        this.handler = handler;
        this.listItem = listItem;
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

                    if (user != null && listItem != null) {

                        final DeviceStatus status = DeviceStatus.get(user, listItem.uniqueId);
                        if (status != null) {

                            // Update UI on DeviceDetails
                            final Runnable refresh = new Runnable() {
                                public void run() {

                                    context.deviceStatus = status;
                                    context.loadData();
                                }
                            };

                            handler.post(refresh);
                        }
                    }
                }

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
