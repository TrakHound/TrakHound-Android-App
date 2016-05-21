// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.devices.DeviceStatus;
import org.trakhound.www.trakhound.devices.DeviceStatusRequest;
import org.trakhound.www.trakhound.users.UserConfiguration;


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

                    UserConfiguration user = ((MyApplication) (((DeviceList) context).getApplication())).User;
                    Device[] devices = ((MyApplication) (((DeviceList) context).getApplication())).Devices;

                    if (user != null && devices != null) {

                        DeviceStatusRequest statusRequest = new DeviceStatusRequest();
                        statusRequest.User = user;
                        statusRequest.GetStatus = true;

                        DeviceStatus[] statuses = DeviceStatus.get(statusRequest);
                        if (statuses != null) {

                            for (int i = 0; i < statuses.length; i++) {

                                for (int x = 0; x < devices.length; x++) {

                                    if (statuses[i].UniqueId != null &&
                                        devices[x].UniqueId != null &&
                                        statuses[i].UniqueId.equals(devices[x].UniqueId)) {

                                        devices[x].Status = statuses[i];
                                    }
                                }
                            }
                        }
                    }

                    ((DeviceList) context).updateStatus("test");
                }

                ((DeviceList) context).updateConnectionStatus(connected);

                Thread.sleep(2000);
            }
            catch (InterruptedException ex) { exit = true; }
            catch (Exception ex) { Log.d("Exception", ex.getMessage()); }
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
