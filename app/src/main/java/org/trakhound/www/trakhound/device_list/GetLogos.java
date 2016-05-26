// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 5/26/2016.
 */
public class GetLogos extends AsyncTask<String,Void,String> {

    private DeviceList context;

    public GetLogos(DeviceList context) {

        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        ListItem[] listItems = MyApplication.ListItems;
        if (listItems != null) {

            for (int i = 0; i < listItems.length; i++) {

                if (listItems[i].Device != null) {

                    setLogoImage((listItems[i].Device));
                }
            }
        }

        return null;
    }

    public static boolean setLogoImage(Device device) {

        if (device.LogoUrl != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + device.LogoUrl;

            Bitmap logo = Requests.getImage(url);
            if (logo != null) {

                device.Logo = logo;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onPostExecute(String dummy){

       if (context != null) context.addDevices();

    }

}
