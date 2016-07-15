// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.api.http.Requests;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.ApiConfiguration;
import org.trakhound.www.trakhound.api.users.UserConfiguration;
import org.trakhound.www.trakhound.api.users.UserManagement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    public String uniqueId;

    public StatusInfo statusInfo;
    public OeeInfo oeeInfo;

    public DeviceStatus() {

        uniqueId = null;

        statusInfo = new StatusInfo();
        oeeInfo = new OeeInfo();

    }

    public static DeviceStatus[] get(UserConfiguration userConfig) {

        if (userConfig != null) {

            try {

                String urlSuffix = "data/get/?" +
                        "token=" + URLEncoder.encode(userConfig.sessionToken, "UTF-8") +
                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
                        "&command=0101"; // Get Status and Oee tables

                String url = Uri.withAppendedPath(ApiConfiguration.apiHost, urlSuffix).toString();

                String response = Requests.get(url);
                if (response != null && response.length() > 0) {

                    ArrayList<DeviceStatus> devicesStatuses = new ArrayList<>();

                    try {

                        JSONArray a = new JSONArray(response);

                        for (int i = 0; i < a.length(); i++) {

                            JSONObject obj = a.getJSONObject(i);

                            DeviceStatus deviceStatus = new DeviceStatus();

                            deviceStatus.uniqueId = obj.getString("unique_id");

                            deviceStatus.statusInfo = StatusInfo.parse(obj.getJSONObject("status"));

                            deviceStatus.oeeInfo = OeeInfo.parse(obj.getJSONObject("oee"));

                            devicesStatuses.add(deviceStatus);
                        }
                    }
                    catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

                    DeviceStatus[] deviceStatusArray = new DeviceStatus[devicesStatuses.size()];
                    return devicesStatuses.toArray(deviceStatusArray);
                }

            } catch (UnsupportedEncodingException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return null;
    }

}
