package org.trakhound.www.trakhound.device_details;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/20/2016.
 */
public class OeeInfo {

    public String UniqueId;

    public double Oee;
    public double Availability;
    public double Performance;
    public double Quality;

    public static OeeInfo parse(JSONObject json) {

        OeeInfo result = new OeeInfo();

        try {

            result.UniqueId = json.getString("unique_id");

            result.Oee = json.getDouble("oee");
            result.Availability = json.getDouble("availability");
            result.Performance = json.getDouble("performance");
            result.Quality = json.getDouble("quality");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
