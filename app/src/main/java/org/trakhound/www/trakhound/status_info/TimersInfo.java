package org.trakhound.www.trakhound.status_info;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/27/2016.
 */
public class TimersInfo {

    public String UniqueId;

    public double Total;
    public double Production;
    public double Idle;
    public double Alert;

    public TimersInfo() {

        Total = 0;
        Alert = 0;
        Idle = 0;
        Production = 0;
    }

    public static TimersInfo parse(JSONObject json) {

        TimersInfo result = new TimersInfo();

        try {

            result.UniqueId = json.getString("unique_id");

            result.Total = json.getDouble("total");

            result.Production = json.getDouble("production");

            result.Idle = json.getDouble("idle");

            result.Alert = json.getDouble("alert");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
