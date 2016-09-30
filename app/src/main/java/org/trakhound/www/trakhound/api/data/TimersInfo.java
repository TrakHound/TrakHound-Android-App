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

        if (json != null) {

            result.total = json.optDouble("total");
            result.active = json.optDouble("active");
            result.idle = json.optDouble("idle");
            result.alert = json.optDouble("alert");

            return result;
        }

        return null;
    }
}
