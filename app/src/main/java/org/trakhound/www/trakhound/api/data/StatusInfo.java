package org.trakhound.www.trakhound.api.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 7/11/2016.
 */
public class StatusInfo {

    public Boolean connected;
    public String deviceStatus;
    public String productionStatus;
    public Integer deviceStatusTimer;
    public Integer productionStatusTimer;


    public static StatusInfo parse(JSONObject json) {

        StatusInfo result = null;

        if (json != null) {

            result = new StatusInfo();

            int connected = json.optInt("connected");
            if (connected > 0) result.connected = true;
            else result.connected = false;

            result.deviceStatus = json.optString("device_status");
            result.productionStatus = json.optString("production_status");
            result.deviceStatusTimer = json.optInt("device_status_timer");
            result.productionStatusTimer = json.optInt("production_status_timer");
        }

        return result;
    }

}
