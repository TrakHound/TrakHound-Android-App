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
                Device[] devices = ((MyApplication)(((DeviceListActivity)context).getApplication())).Devices;
                if (devices != null && devices.length > 0) {

                    for (int i = 0; i < devices.length; i++) {

                        DeviceStatus status = DeviceStatus.get(devices[i]);
                        if (status != null) devices[i].Status = status;

                    }
                }

                ((DeviceListActivity) context).updateStatus("test");


                Thread.sleep(5000);

            } catch (InterruptedException ex) { }

        }







//        handler.postDelayed(new StatusHandler(context, handler), 5000);
//        handler.postDelayed(this, 5000);

    }

}
