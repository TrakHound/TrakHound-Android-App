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

            try {

                int connected = json.getInt("connected");
                if (connected > 0) result.connected = true;

                result.deviceStatus = json.getString("device_status");
                result.productionStatus = json.getString("production_status");
                result.deviceStatusTimer = json.getInt("device_status_timer");
                result.productionStatusTimer = json.getInt("production_status_timer");

            }
            catch (JSONException ex) { ex.getStackTrace(); }
        }

        return result;
    }

}
