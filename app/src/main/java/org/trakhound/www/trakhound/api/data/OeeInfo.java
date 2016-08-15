package org.trakhound.www.trakhound.api.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/20/2016.
 */
public class OeeInfo {

    public double oee;
    public double availability;
    public double performance;
    public double quality;

    public OeeInfo() {

        oee = 0;
        availability = 0;
        performance = 0;
        quality = 0;
    }

    public static OeeInfo parse(JSONObject json) {

        OeeInfo result = new OeeInfo();

        if (json != null) {

            try {

                result.oee = json.getDouble("oee");
                result.availability = json.getDouble("availability");
                result.performance = json.getDouble("performance");
                result.quality = json.getDouble("quality");

                return result;
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return null;
    }
}
