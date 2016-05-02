package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.widget.TextView;

import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.devices.DeviceStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Patrick on 4/28/2016.
 */
public class StatusHandler implements Runnable {

    private Context context;

    private Handler handler;

    public StatusHandler(Context context, Handler handler) {

        this.context = context;
        this.handler = handler;

    }


    @Override
    public void run() {

        while (true) {

            try
            {
                UserConfiguration user = ((MyApplication)(((DeviceListActivity)context).getApplication())).User;
                Device[] devices = ((MyApplication)(((DeviceListActivity)context).getApplication())).Devices;
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

                Thread.sleep(2000);

            } catch (Exception ex) { }
        }
    }

}
