package org.trakhound.www.trakhound.api.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/26/2016.
 */
public class ControllerInfo {

    public String availability;
    public String emergencyStop;
    public String controllerMode;
    public String executionMode;
    public String systemStatus;
    public String systemMessage;
    public String programName;

    public static ControllerInfo parse(JSONObject json) {

        ControllerInfo result = new ControllerInfo();

        try {

            result.availability = json.getString("availability");
            result.emergencyStop = json.getString("emergency_stop");
            result.controllerMode = json.getString("controller_mode");
            result.executionMode = json.getString("execution_mode");
            result.systemStatus = json.getString("system_status");
            result.systemMessage = json.getString("system_message");
            result.programName = json.getString("program_name");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
