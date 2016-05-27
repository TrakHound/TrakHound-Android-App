package org.trakhound.www.trakhound.status_info;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 5/20/2016.
 */
public class ProductionInfo {

    public String UniqueId;

    public Boolean Alert;
    public Boolean Idle;
    public Boolean Production;

    public String ProductionStatus;
    public String ProductionStatusTimer;

    public ProductionInfo() {

        Alert = true;
        Idle = false;
        Production = false;

        ProductionStatus = "Default";
        ProductionStatusTimer = "0";
    }

    public static ProductionInfo parse(JSONObject json) {

        ProductionInfo result = new ProductionInfo();

        try {

            result.UniqueId = json.getString("unique_id");

            // Get Device Status
            String statusText = json.getString("status");
            if (statusText != null) {

                result.Alert = false;
                result.Idle = false;
                result.Production = false;

                if (statusText.equals("0")) result.Alert = true;
                else if (statusText.equals("1")) result.Idle = true;
                else if (statusText.equals("2")) result.Production = true;
            }

            // Get Production Status
            result.ProductionStatus = json.getString("production_status");

            // Get Production Status Timer
            result.ProductionStatusTimer = json.getString("production_status_timer");

            return result;
        }
        catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }

        return null;
    }
}
