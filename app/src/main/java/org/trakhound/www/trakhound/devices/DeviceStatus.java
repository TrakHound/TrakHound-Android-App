package org.trakhound.www.trakhound.devices;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    private static String ADR_UNIQUE_ID = "device_unique_id";
    private static String ADR_STATUS = "status";
    private static String ADR_PRODUCTION_STATUS = "production_status";
    private static String ADR_PRODUCTION_STATUS_TIMER = "production_status_timer";

    private static String ADR_EMERGENCY_STOP = "emergency_stop";
    private static String ADR_CONTROLLER_MODE = "controller_mode";
    private static String ADR_EXECUTION_MODE = "execution_mode";
    private static String ADR_SYSTEM_STATUS = "system_status";
    private static String ADR_SYSTEM_MESSAGE = "system_message";

    private static String ADR_OEE = "oee";
    private static String ADR_OEE_AVAILABILITY = "oee_availability";
    private static String ADR_OEE_PERFORMANCE = "oee_performance";

    private static String ADR_SECONDS_TOTAL = "seconds_total";
    private static String ADR_SECONDS_PRODUCTION = "seconds_production";
    private static String ADR_SECONDS_IDLE = "seconds_idle";
    private static String ADR_SECONDS_ALERT = "seconds_alert";

    private static String NAME = "NAME";
    private static String VALUE = "VALUE";


    public String UniqueId;

    public Boolean Alert;
    public Boolean Idle;
    public Boolean Production;

    public String ProductionStatus;
    public String ProductionStatusTimer;

    public String EmergencyStop;
    public String ControllerMode;
    public String ExecutionMode;
    public String SystemStatus;
    public String SystemMessage;

    public double Oee;
    public double OeeAvailability;
    public double OeePerformance;

    public int SecondsTotal;
    public int SecondsProduction;
    public int SecondsIdle;
    public int SecondsAlert;


    public DeviceStatus() {

        UniqueId = null;

        Alert = true;
        Idle = false;
        Production = false;

        ProductionStatus = "Default";
        ProductionStatusTimer = "0";
    }

    public static DeviceStatus[] get(UserConfiguration userConfig) {

        String url = "https://www.feenux.com/trakhound/api/mobile/get/";

        PostData[] postDatas = new PostData[1];
        postDatas[0] = new PostData("user_id", userConfig.Username);

        String response = Requests.post(url, postDatas);
        if (response != null && response.length() > 0) {

            Table[] tables = Table.get(response);
            if (tables != null) {

                ArrayList<DeviceStatus> statusList = new ArrayList<>();

                for (int i = 0; i < tables.length; i++) {

                    DeviceStatus status = parse(tables[i].Data);
                    if (status != null) statusList.add(status);
                }

                DeviceStatus[] statusArray = new DeviceStatus[statusList.size()];
                return statusList.toArray(statusArray);
            }
        }

        return null;
    }

    public static DeviceStatus parse(String s) {

        DeviceStatus result = null;

        if (s != null && s.length() > 0) {

            result = new DeviceStatus();

            try {

                JSONArray a = new JSONArray(s);
                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    String name = obj.getString(NAME);

                    // Get Device Unique ID
                    if (name.equals(ADR_UNIQUE_ID)) result.UniqueId = obj.getString(VALUE);

                    // Get Device Status (Alert, Idle, or Production)
                    if (name.equals(ADR_STATUS)) {

                        result.Alert = false;
                        result.Idle = false;
                        result.Production = false;

                        String status = obj.getString(VALUE);
                        if (status.equals("Alert")) result.Alert = true;
                        else if (status.equals("Idle")) result.Idle = true;
                        else if (status.equals("Production")) result.Production = true;
                    }

                    // Get Device Production Status
                    if (name.equals(ADR_PRODUCTION_STATUS)) {

                        result.ProductionStatus = obj.getString(VALUE);
                    }

                    if (name.equals(ADR_PRODUCTION_STATUS_TIMER)) {

                        result.ProductionStatusTimer = obj.getString(VALUE);
                    }

                    // Get Controller Status
                    if (name.equals(ADR_EMERGENCY_STOP)) result.EmergencyStop = obj.getString(VALUE);
                    if (name.equals(ADR_CONTROLLER_MODE)) result.ControllerMode = obj.getString(VALUE);
                    if (name.equals(ADR_EXECUTION_MODE)) result.ExecutionMode = obj.getString(VALUE);
                    if (name.equals(ADR_SYSTEM_STATUS)) result.SystemStatus = obj.getString(VALUE);
                    if (name.equals(ADR_SYSTEM_MESSAGE)) result.SystemMessage = obj.getString(VALUE);

                    // Get OEE Status
                    if (name.equals(ADR_OEE)) result.Oee = obj.getDouble(VALUE);
                    if (name.equals(ADR_OEE_AVAILABILITY)) result.OeeAvailability = obj.getDouble(VALUE);
                    if (name.equals(ADR_OEE_PERFORMANCE)) result.OeePerformance = obj.getDouble(VALUE);

                    // Production Status Counters
                    if (name.equals(ADR_SECONDS_TOTAL)) result.SecondsTotal = obj.getInt(VALUE);
                    if (name.equals(ADR_SECONDS_PRODUCTION)) result.SecondsProduction = obj.getInt(VALUE);
                    if (name.equals(ADR_SECONDS_IDLE)) result.SecondsIdle = obj.getInt(VALUE);
                    if (name.equals(ADR_SECONDS_ALERT)) result.SecondsAlert = obj.getInt(VALUE);

                }
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            catch (Exception ex) { Log.d("Exception", ex.getMessage()); }
        }

        return result;
    }

}
