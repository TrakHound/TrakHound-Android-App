// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_details;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.api.data.ControllerInfo;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.data.TimersInfo;
import org.trakhound.www.trakhound.api.http.Requests;
import org.trakhound.www.trakhound.api.users.ApiConfiguration;
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


//    public String UniqueId;

//    public ProductionInfo Production;
//    public OeeInfo Oee;
//    public ControllerInfo Controller;
//    public TimersInfo Timers;

    public DeviceStatus() {

        uniqueId = null;

        statusInfo = new StatusInfo();
        controllerInfo = new ControllerInfo();
        oeeInfo = new OeeInfo();
        timersInfo = new TimersInfo();

//        UniqueId = null;
//
//        Production = new ProductionInfo();
//        Oee = new OeeInfo();
    }

    public static DeviceStatus get(UserConfiguration userConfig, String uniqueId) {

        if (userConfig != null) {

            String urlSuffix = "data/get/?" +
                "token=" + userConfig.sessionToken +
                "&sender_id=" + UserManagement.getSenderId() +
                "&unique_id=" + uniqueId +
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





                    //devicesStatuses.add(deviceStatus);


//                    DeviceStatus result = new DeviceStatus();
//
//                    JSONArray a = new JSONArray(response);
//
//                    // Status is first array
//                    result.Production = processStatusArray(a.getJSONArray(0));
//
//                    // Controller is second array
//                    result.Controller = processControllerArray(a.getJSONArray(1));
//
//                    // Oee is Third array
//                    result.Oee = processOeeArray(a.getJSONArray(2));
//
//                    // Timers is Fourth array
//                    result.Timers = processTimersArray(a.getJSONArray(3));
//
//                    return result;
                }
                catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            }
        }

        return null;
    }

//    public static ProductionInfo processStatusArray(JSONArray a) {
//
//        ProductionInfo result = null;
//
//        try {
//
//            JSONObject obj = a.getJSONObject(0);
//
//            ProductionInfo status = ProductionInfo.parse(obj);
//            if (status != null) result = status;
//        }
//        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
//
//        return result;
//    }

    public static ControllerInfo processControllerArray(JSONArray a) {

        ControllerInfo result = null;

        try {

            JSONObject obj = a.getJSONObject(0);

            ControllerInfo status = ControllerInfo.parse(obj);
            if (status != null) result = status;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return result;
    }

    public static OeeInfo processOeeArray(JSONArray a) {

        OeeInfo result = null;

        try {

            JSONObject obj = a.getJSONObject(0);

            OeeInfo status = OeeInfo.parse(obj);
            if (status != null) result = status;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return result;
    }

    public static TimersInfo processTimersArray(JSONArray a) {

        TimersInfo result = null;

        try {

            JSONObject obj = a.getJSONObject(0);

            TimersInfo status = TimersInfo.parse(obj);
            if (status != null) result = status;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return result;
    }

}
