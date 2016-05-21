// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.devices;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

import org.trakhound.www.trakhound.device_list.OeeInfo;
import org.trakhound.www.trakhound.device_list.StatusInfo;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;
import org.trakhound.www.trakhound.users.UserManagement;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

//    private static String ADR_UNIQUE_ID = "device_unique_id";
//    private static String ADR_STATUS = "status";
//    private static String ADR_PRODUCTION_STATUS = "production_status";
//    private static String ADR_PRODUCTION_STATUS_TIMER = "production_status_timer";
//
//    private static String ADR_EMERGENCY_STOP = "emergency_stop";
//    private static String ADR_CONTROLLER_MODE = "controller_mode";
//    private static String ADR_EXECUTION_MODE = "execution_mode";
//    private static String ADR_SYSTEM_STATUS = "system_status";
//    private static String ADR_SYSTEM_MESSAGE = "system_message";
//
//    private static String ADR_OEE = "oee";
//    private static String ADR_OEE_AVAILABILITY = "oee_availability";
//    private static String ADR_OEE_PERFORMANCE = "oee_performance";
//
//    private static String ADR_SECONDS_TOTAL = "seconds_total";
//    private static String ADR_SECONDS_PRODUCTION = "seconds_production";
//    private static String ADR_SECONDS_IDLE = "seconds_idle";
//    private static String ADR_SECONDS_ALERT = "seconds_alert";
//
//    private static String NAME = "NAME";
//    private static String VALUE = "VALUE";


    public String UniqueId;

    public StatusInfo Status;
    public OeeInfo Oee;







//    public Boolean Alert;
//    public Boolean Idle;
//    public Boolean Production;
//
//    public String ProductionStatus;
//    public String ProductionStatusTimer;
//
//    public String EmergencyStop;
//    public String ControllerMode;
//    public String ExecutionMode;
//    public String SystemStatus;
//    public String SystemMessage;
//
//    public double Oee;
//    public double OeeAvailability;
//    public double OeePerformance;
//
//    public int SecondsTotal;
//    public int SecondsProduction;
//    public int SecondsIdle;
//    public int SecondsAlert;


    public DeviceStatus() {

        UniqueId = null;

        Status = new StatusInfo();
        Oee = new OeeInfo();

    }

    public static DeviceStatus[] get(DeviceStatusRequest request) {

        if (request != null && request.User != null) {

            String url = "https://www.feenux.com/trakhound/api/mobile/get/?" +
                "token=" + request.User.SessionToken +
                "&sender_id=" + UserManagement.getSenderId() +
                "&command=" + "0101"; // Get Status and Oee tables

            String response = Requests.get(url);
            if (response != null && response.length() > 0) {

                ArrayList<DeviceStatus> statuses = new ArrayList<>();

                ArrayList<StatusInfo> statusInfos = new ArrayList<>();
                ArrayList<OeeInfo> oeeInfos = new ArrayList<>();

                try {

                    JSONArray a = new JSONArray(response);

                    // Status is first array
                    statusInfos = processStatusArray(a.getJSONArray(0));

                    // Oee is second array
                    oeeInfos = processOeeArray(a.getJSONArray(1));

                }
                catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }


                // Add StatusInfos
                for (int i = 0; i < statusInfos.size(); i++) {

                    DeviceStatus status = null;

                    for (int x = 0; x < statuses.size(); x++) {

                        if (statuses.get(x).UniqueId.equals(statusInfos.get(i).UniqueId)) {

                            status = statuses.get(x);
                            status.Status = statusInfos.get(i);
                        }
                    }

                    if (status == null) {

                        status = new DeviceStatus();
                        status.UniqueId = statusInfos.get(i).UniqueId;
                        status.Status = statusInfos.get(i);
                        statuses.add(status);
                    }
                }

                // Add OeeInfos
                for (int i = 0; i < oeeInfos.size(); i++) {

                    DeviceStatus status = null;

                    for (int x = 0; x < statuses.size(); x++) {

                        if (statuses.get(x).UniqueId.equals(oeeInfos.get(i).UniqueId)) {

                            status = statuses.get(x);
                            status.Oee = oeeInfos.get(i);
                        }
                    }

                    if (status == null) {

                        status = new DeviceStatus();
                        status.UniqueId = oeeInfos.get(i).UniqueId;
                        status.Oee = oeeInfos.get(i);
                        statuses.add(status);
                    }
                }


                DeviceStatus[] statusArray = new DeviceStatus[statuses.size()];
                return statuses.toArray(statusArray);
            }
        }

        return null;
    }

    private static ArrayList<StatusInfo> processStatusArray(JSONArray a) {

        ArrayList<StatusInfo> result = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {

            try {

                JSONObject obj = a.getJSONObject(i);

                StatusInfo status = StatusInfo.parse(obj);

                if (status != null) result.add(status);
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return result;
    }

    private static ArrayList<OeeInfo> processOeeArray(JSONArray a) {

        ArrayList<OeeInfo> result = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {

            try {

                JSONObject obj = a.getJSONObject(i);

                OeeInfo status = OeeInfo.parse(obj);

                if (status != null) result.add(status);
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return result;
    }








//    public static DeviceStatus parseStatus(JSONObject json) {
//
//        DeviceStatus result = new DeviceStatus();
//
//        try {
//
//            result.UniqueId = json.getString("unique_id");
//
//            // Get Device Status
//            String statusText = json.getString("status");
//            if (statusText != null) {
//
//                result.Alert = false;
//                result.Idle = false;
//                result.Production = false;
//
//                if (statusText.equals("0")) result.Alert = true;
//                else if (statusText.equals("1")) result.Idle = true;
//                else if (statusText.equals("2")) result.Production = true;
//            }
//
//            // Get Production Status
//            result.ProductionStatus = json.getString("production_status");
//
//            // Get Production Status Timer
//            result.ProductionStatusTimer = json.getString("production_status_timer");
//
//            return result;
//
//        }
//        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
//        catch (Exception ex) { Log.d("Exception", ex.getMessage()); }
//
//        return null;
//    }


//    public static DeviceStatus parseOee(JSONObject json) {
//
//        DeviceStatus result = new DeviceStatus();
//
//        try {
//
//            result.UniqueId = json.getString("unique_id");
//
//            // Get Device Status
//            String statusText = json.getString("status");
//            if (statusText != null) {
//
//                result.Alert = false;
//                result.Idle = false;
//                result.Production = false;
//
//                if (statusText.equals("0")) result.Alert = true;
//                else if (statusText.equals("1")) result.Idle = true;
//                else if (statusText.equals("2")) result.Production = true;
//            }
//
//            // Get Production Status
//            result.ProductionStatus = json.getString("production_status");
//
//            // Get Production Status Timer
//            result.ProductionStatusTimer = json.getString("production_status_timer");
//
//
//            return result;
//        }
//        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
//        catch (Exception ex) { Log.d("Exception", ex.getMessage()); }
//
//        return null;
//    }








//    public static DeviceStatus parse(String s) {
//
//        DeviceStatus result = null;
//
//        if (s != null && s.length() > 0) {
//
//            result = new DeviceStatus();
//
//            try {
//
//                JSONArray a = new JSONArray(s);
//                for (int i = 0; i < a.length(); i++) {
//
//                    JSONObject obj = a.getJSONObject(i);
//
//                    String name = obj.getString(NAME);
//
//                    // Get Device Unique ID
//                    if (name.equals(ADR_UNIQUE_ID)) result.UniqueId = obj.getString(VALUE);
//
//                    // Get Device Status (Alert, Idle, or Production)
//                    if (name.equals(ADR_STATUS)) {
//
//                        result.Alert = false;
//                        result.Idle = false;
//                        result.Production = false;
//
//                        String status = obj.getString(VALUE);
//                        if (status.equals("Alert")) result.Alert = true;
//                        else if (status.equals("Idle")) result.Idle = true;
//                        else if (status.equals("Production")) result.Production = true;
//                    }
//
//                    // Get Device Production Status
//                    if (name.equals(ADR_PRODUCTION_STATUS)) {
//
//                        result.ProductionStatus = obj.getString(VALUE);
//                    }
//
//                    if (name.equals(ADR_PRODUCTION_STATUS_TIMER)) {
//
//                        result.ProductionStatusTimer = obj.getString(VALUE);
//                    }
//
//                    // Get Controller Status
//                    if (name.equals(ADR_EMERGENCY_STOP)) result.EmergencyStop = obj.getString(VALUE);
//                    if (name.equals(ADR_CONTROLLER_MODE)) result.ControllerMode = obj.getString(VALUE);
//                    if (name.equals(ADR_EXECUTION_MODE)) result.ExecutionMode = obj.getString(VALUE);
//                    if (name.equals(ADR_SYSTEM_STATUS)) result.SystemStatus = obj.getString(VALUE);
//                    if (name.equals(ADR_SYSTEM_MESSAGE)) result.SystemMessage = obj.getString(VALUE);
//
//                    // Get OEE Status
//                    if (name.equals(ADR_OEE)) result.Oee = obj.getDouble(VALUE);
//                    if (name.equals(ADR_OEE_AVAILABILITY)) result.OeeAvailability = obj.getDouble(VALUE);
//                    if (name.equals(ADR_OEE_PERFORMANCE)) result.OeePerformance = obj.getDouble(VALUE);
//
//                    // Production Status Counters
//                    if (name.equals(ADR_SECONDS_TOTAL)) result.SecondsTotal = obj.getInt(VALUE);
//                    if (name.equals(ADR_SECONDS_PRODUCTION)) result.SecondsProduction = obj.getInt(VALUE);
//                    if (name.equals(ADR_SECONDS_IDLE)) result.SecondsIdle = obj.getInt(VALUE);
//                    if (name.equals(ADR_SECONDS_ALERT)) result.SecondsAlert = obj.getInt(VALUE);
//
//                }
//            }
//            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
//            catch (Exception ex) { Log.d("Exception", ex.getMessage()); }
//        }
//
//        return result;
//    }

}
