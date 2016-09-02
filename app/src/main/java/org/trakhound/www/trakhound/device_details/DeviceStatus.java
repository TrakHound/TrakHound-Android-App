// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_details;

import android.net.Uri;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.api.data.ControllerInfo;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.data.TimersInfo;
import org.trakhound.www.trakhound.api.http.Requests;
import org.trakhound.www.trakhound.api.ApiConfiguration;
import org.trakhound.www.trakhound.api.users.UserConfiguration;
import org.trakhound.www.trakhound.api.users.UserManagement;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    public String uniqueId;

    public StatusInfo statusInfo;
    public ControllerInfo controllerInfo;
    public OeeInfo oeeInfo;
    public TimersInfo timersInfo;


    public DeviceStatus() {

        uniqueId = null;

        statusInfo = new StatusInfo();
        controllerInfo = new ControllerInfo();
        oeeInfo = new OeeInfo();
        timersInfo = new TimersInfo();
    }

    public static DeviceStatus get(UserConfiguration userConfig, String uniqueId) {

        if (userConfig != null) {

            DateTime now = DateTime.now();
            DateTime from = new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().get(), 0, 0, 0);
            DateTime to = from.plusDays(1);

            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            String fromStr = fmt.print(from);
            String toStr = fmt.print(to);

            String urlSuffix = "data/get/?" +
                "token=" + userConfig.sessionToken +
                "&sender_id=" + UserManagement.getSenderId() +
                "&unique_id=" + uniqueId +
                "&from=" + fromStr + "&to=" + toStr +
                "&command=" + "01111"; // Get Status, Controller, Oee, and Timers tables

            String url = Uri.withAppendedPath(ApiConfiguration.apiHost, urlSuffix).toString();

            String response = Requests.get(url);
            if (response != null && response.length() > 0) {

                try {

                    JSONArray a = new JSONArray(response);

                    if (a.length() > 0) {

                        JSONObject obj = a.getJSONObject(0);

                        DeviceStatus deviceStatus = new DeviceStatus();

                        deviceStatus.uniqueId = obj.getString("unique_id");

                        deviceStatus.statusInfo = StatusInfo.parse(obj.getJSONObject("status"));

                        deviceStatus.controllerInfo = ControllerInfo.parse(obj.getJSONObject("controller"));

                        deviceStatus.oeeInfo = OeeInfo.parse(obj.getJSONObject("oee"));

                        deviceStatus.timersInfo = TimersInfo.parse(obj.getJSONObject("timers"));

                        return deviceStatus;
                    }
                }
                catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            }
        }

        return null;
    }

}
