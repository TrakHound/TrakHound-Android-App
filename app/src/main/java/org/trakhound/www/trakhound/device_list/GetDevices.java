// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.http.Requests;
import org.trakhound.www.trakhound.tools.Json;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;

import java.util.ArrayList;


public class GetDevices extends AsyncTask<String,Void,ListItem[]> {

    private DeviceList context;
    private ProgressDialog progressDialog;

    public GetDevices(DeviceList context, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected void onPreExecute(){

    }

    @Override
    protected ListItem[] doInBackground(String... arg0) {

        ListItem[] result = null;

        UserConfiguration userConfig = MyApplication.User;
        if (userConfig != null) {

            result = get(userConfig);
        }

        return result;
    }

    @Override
    protected void onPostExecute(ListItem[] listItems){

        MyApplication.Devices = null;

        if (listItems != null) {

            Device[] devices = new Device[listItems.length];

            for (int i = 0; i < listItems.length; i++) {

                devices[i] = listItems[i].Device;
            }

            MyApplication.Devices = devices;
        }

        MyApplication.ListItems = listItems;

        context.addDevices();
//        context.listAdapter.addAll(listItems);

        new GetLogos(context).execute();

        if (progressDialog != null) progressDialog.dismiss();
    }


    public static ListItem[] get(UserConfiguration userConfig) {

        if (userConfig != null) {

            String url = "https://www.feenux.com/trakhound/api/mobile/get/?" +
                    "token=" + userConfig.SessionToken +
                    "&sender_id=" + UserManagement.getSenderId() +
                    "&command=" + "1101"; // Get Description, Status, and Oee tables

            String response = Requests.get(url);
            if (response != null && response.length() > 0) {

                try {

                    // Create ArrayList to return
                    ArrayList<ListItem> result = new ArrayList<>();

                    // Get Parent Array from response string
                    JSONArray a = new JSONArray(response);
                    if (a.length() > 0) {

                        // Description Array is first index
                        JSONArray descriptionArray = a.getJSONArray(0);

                        JSONArray productionArray = null;
                        JSONArray oeeArray = null;

                        if (a.length() > 1) productionArray = a.getJSONArray(1);
                        if (a.length() > 2) oeeArray = a.getJSONArray(2);

                        for (int i = 0; i < descriptionArray.length(); i++) {

                            JSONObject obj = descriptionArray.getJSONObject(i);

                            Device device = Device.parse(obj);
                            if (device != null) {

                                String uniqueId = device.UniqueId;
                                if (uniqueId != null) {

                                    ListItem listItem = new ListItem();
                                    listItem.Device = device;

                                    // Get Production Info
                                    obj = Json.find(productionArray, "unique_id", uniqueId);
                                    if (obj != null) {

                                        listItem.Status.Production = ProductionInfo.parse(obj);
                                    }

                                    // Get Oee Info
                                    obj = Json.find(oeeArray, "unique_id", uniqueId);
                                    if (obj != null) {

                                        listItem.Status.Oee = OeeInfo.parse(obj);
                                    }

                                    result.add(listItem);
                                }
                            }
                        }
                    }

                    ListItem[] resultArray = new ListItem[result.size()];
                    return result.toArray(resultArray);

                } catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            }
        }

        return null;
    }

}
