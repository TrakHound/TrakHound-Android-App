package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
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

//    public StatusHandler(Context context) {
//
//        this.context = context;
//    }

    @Override
    public void run() {

        Device[] devices = ((MyApplication)(((DeviceListActivity)context).getApplication())).Devices;
        if (devices != null && devices.length > 0) {

            for (int i = 0; i < devices.length; i++) {

                DeviceStatus status = DeviceStatus.get(devices[i]);
                if (status != null) devices[i].Status = status;

//                devices[i].Status = DeviceStatus.get(devices[i]);

//                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
//                Date currentLocalTime = cal.getTime();
//                DateFormat date = new SimpleDateFormat("HH:mm:ss a");
//                // you can get seconds by adding  "...:ss" to it
//                date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
//
//                String localTime = date.format(currentLocalTime);
//
//                devices[i].Status.ProductionStatus = localTime;

            }
        }




        ((DeviceListActivity) context).updateStatus("test");


        handler.postDelayed(new StatusHandler(context, handler), 5000);
//
//        Message msg = handler.obtainMessage();
//        handler.sendMessage(msg);

//        handler.postDelayed(this, 2000);

    }

}






//public class StatusActivity extends AsyncTask<Integer,Void,String> {
//
//    private Context context;
//
//    public StatusActivity(Context context) {
//
//        this.context = context;
//
//    }
//
//
//    protected void onPreExecute(){
//
//    }
//
//    @Override
//    protected String doInBackground(Integer... arg0) {
//
//        Device[] devices = ((MyApplication)(((DeviceListActivity)context).getApplication())).Devices;
//        if (devices != null && devices.length > 0) {
//
//            for (int i = 0; i < devices.length; i++) {
//
//                devices[i].Status = DeviceStatus.get(devices[i]);
//
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String arg) {
//
//        ((DeviceListActivity) context).listAdapter.notifyDataSetChanged();
//
//    }
//
//}
