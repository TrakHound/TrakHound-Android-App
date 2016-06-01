package org.trakhound.www.trakhound.status_info;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/26/2016.
 */
public class ControllerInfo {

    public String UniqueId;

    public String Availability;
    public String EmergencyStop;
    public String ControllerMode;
    public String ExecutionMode;
    public String SystemStatus;
    public String SystemMessage;
    public String ProgramName;

    public static ControllerInfo parse(JSONObject json) {

        ControllerInfo result = new ControllerInfo();

        try {

            result.UniqueId = json.getString("unique_id");

            result.Availability = json.getString("availability");
            result.EmergencyStop = json.getString("emergency_stop");
            result.ControllerMode = json.getString("controller_mode");
            result.ExecutionMode = json.getString("execution_mode");
            result.SystemStatus = json.getString("system_status");
            result.SystemMessage = json.getString("system_message");
            result.ProgramName = json.getString("program_name");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
