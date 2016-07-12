package org.trakhound.www.trakhound.api.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/27/2016.
 */
public class TimersInfo {

    public double total;

    public double active;
    public double idle;
    public double alert;

    public TimersInfo() {

        total = 0;

        active = 0;
        idle = 0;
        alert = 0;

    }

    public static TimersInfo parse(JSONObject json) {

        TimersInfo result = new TimersInfo();

        try {

            result.total = json.getDouble("total");

            result.active = json.getDouble("active");

            result.idle = json.getDouble("idle");

            result.alert = json.getDouble("alert");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
