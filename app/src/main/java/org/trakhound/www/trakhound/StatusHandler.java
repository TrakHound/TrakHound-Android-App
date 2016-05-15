package org.trakhound.www.trakhound;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.devices.DeviceStatus;


/**
 * Created by Patrick on 4/28/2016.
 */
public class StatusHandler implements Runnable {

    private Context context;

//    private Handler handler;

    public StatusHandler(Context context, Handler handler) {

        this.context = context;
//        this.handler = handler;
    }


    @Override
    public void run() {

        while (true) {

            try
            {
                boolean connected = isNetworkConnected();

                // Only run if connected to Wifi or Ethernet
                if (connected) {

                    UserConfiguration user = ((MyApplication) (((DeviceListActivity) context).getApplication())).User;
                    Device[] devices = ((MyApplication) (((DeviceListActivity) context).getApplication())).Devices;
                    if (user != null && devices != null) {

                        DeviceStatus[] statuses = DeviceStatus.get(user);
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

                    ((DeviceListActivity) context).updateStatus("test");
                }

                ((DeviceListActivity) context).updateConnectionStatus(connected);

                Thread.sleep(2000);

            } catch (Exception ex) { }
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
