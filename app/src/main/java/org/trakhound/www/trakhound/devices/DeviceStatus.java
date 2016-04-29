package org.trakhound.www.trakhound.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    private static String ADR_ALERT = "Alert";
    private static String ADR_IDLE = "Idle";
    private static String ADR_PRODUCTION = "Production";

    private static String ADR_PRODUCTION_STATUS= "Production Status";



    public Boolean Alert;
    public Boolean Idle;
    public Boolean Production;

    public String ProductionStatus;

    public DeviceStatus() {

        Alert = true;
        Idle = false;
        Production = false;

        ProductionStatus = "Default";

    }

    public static DeviceStatus get(Device device) {

        DeviceStatus result = null;

        String url = "https://www.feenux.com/php/Retrieve.php";
        String sql = "SELECT NAME,VALUE FROM " + device.DatabaseId + "_snapshots";

        PostData[] postDatas = new PostData[5];
        postDatas[0] = new PostData("server", "localhost");
        postDatas[1] = new PostData("user", device.DatabaseUsername);
        postDatas[2] = new PostData("password", device.DatabasePassword);
        postDatas[3] = new PostData("db", device.DatabaseName);
        postDatas[4] = new PostData("query", sql);

        String response = Requests.post(url, postDatas);
        if (response != null) {

            result = parse(response);

        }

        return result;
    }

    private static DeviceStatus parse(String s) {

        DeviceStatus result = null;

        if (s != null) {

            result = new DeviceStatus();

            try {

                JSONArray a = new JSONArray(s);
                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    String address = obj.getString("NAME");

                    if (address.equals(ADR_ALERT)) result.Alert = Boolean.parseBoolean(obj.getString("VALUE"));
                    else if (address.equals(ADR_IDLE)) result.Idle = Boolean.parseBoolean(obj.getString("VALUE"));
                    else if (address.equals(ADR_PRODUCTION)) result.Production = Boolean.parseBoolean(obj.getString("VALUE"));

                    else if (address.equals(ADR_PRODUCTION_STATUS)) result.ProductionStatus = obj.getString("VALUE");

                }

            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

}
