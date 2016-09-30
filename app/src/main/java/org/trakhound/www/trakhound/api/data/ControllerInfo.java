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

        if (json != null) {

            result.availability = json.optString("availability");
            result.emergencyStop = json.optString("emergency_stop");
            result.controllerMode = json.optString("controller_mode");
            result.executionMode = json.optString("execution_mode");
            result.systemStatus = json.optString("system_status");
            result.systemMessage = json.optString("system_message");
            result.programName = json.optString("program_name");

            return result;
        }
        
        return null;
    }
}
