package org.trakhound.www.trakhound.devices;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import org.trakhound.www.trakhound.UserConfiguration;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    private static String ADR_UNIQUE_ID = "device_unique_id";
    private static String ADR_STATUS = "status";
    private static String ADR_PRODUCTION_STATUS= "production_status";
    private static String ADR_PRODUCTION_STATUS_TIMER= "production_status_timer";

    private static String NAME = "NAME";
    private static String VALUE = "VALUE";


    public String UniqueId;

    public Boolean Alert;
    public Boolean Idle;
    public Boolean Production;

    public String ProductionStatus;
    public String ProductionStatusTimer;

    public DeviceStatus() {

        UniqueId = null;

        Alert = true;
        Idle = false;
        Production = false;

        ProductionStatus = "Default";
        ProductionStatusTimer = "0";

    }

    public static DeviceStatus[] get(UserConfiguration userConfig) {

        String url = "https://www.feenux.com/php/mobile/get.php";

        PostData[] postDatas = new PostData[1];
        postDatas[0] = new PostData("user_id", userConfig.Username);

        String response = Requests.post(url, postDatas);
        if (response != null && response.length() > 0) {

            Table[] tables = Table.get(response);
            if (tables != null) {

                ArrayList<DeviceStatus> statusList = new ArrayList<>();

                for (int i = 0; i < tables.length; i++) {

                    DeviceStatus status = parse(tables[i].Data);
                    if (status != null) statusList.add(status);
                }

                DeviceStatus[] statusArray = new DeviceStatus[statusList.size()];
                return statusList.toArray(statusArray);
            }
        }

        return null;
    }

    public static DeviceStatus parse(String s) {

        DeviceStatus result = null;

        if (s != null && s.length() > 0) {

            result = new DeviceStatus();

            try {

                JSONArray a = new JSONArray(s);
                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    String name = obj.getString(NAME);

                    // Get Device Unique ID
                    if (name.equals(ADR_UNIQUE_ID)) result.UniqueId = obj.getString(VALUE);

                    // Get Device Status (Alert, Idle, or Production)
                    if (name.equals(ADR_STATUS)) {

                        result.Alert = false;
                        result.Idle = false;
                        result.Production = false;

                        String status = obj.getString(VALUE);
                        if (status.equals("Alert")) result.Alert = true;
                        else if (status.equals("Idle")) result.Idle = true;
                        else if (status.equals("Production")) result.Production = true;
                    }

                    // Get Device Production Status
                    if (name.equals(ADR_PRODUCTION_STATUS)) {

                        result.ProductionStatus = obj.getString(VALUE);

                    }

                    if (name.equals(ADR_PRODUCTION_STATUS_TIMER)) {

                        result.ProductionStatusTimer = obj.getString(VALUE);

                    }

//                    if (address.equals(ADR_ALERT)) result.Alert = Boolean.parseBoolean(obj.getString("VALUE"));
//                    else if (address.equals(ADR_IDLE)) result.Idle = Boolean.parseBoolean(obj.getString("VALUE"));
//                    else if (address.equals(ADR_PRODUCTION)) result.Production = Boolean.parseBoolean(obj.getString("VALUE"));




//                    else if (address.equals(ADR_PRODUCTION_STATUS)) {
//                        result.ProductionStatus = obj.getString("VALUE");
//
//                        String startString = obj.getString("PREVIOUS_TIMESTAMP");
//                        String endString = obj.getString("TIMESTAMP");
//
//                        if (startString != null && endString != null) {
//
//                            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//
//
//                            DateTime start = dateFormatter.parseDateTime(startString);
//                            DateTime end = dateFormatter.parseDateTime(endString);
//
//                            Period duration = new Period(start, end);
//
//                            String elapsed= String.format("%02d:%02d:%02d", duration.getHours(), duration.getMinutes(), duration.getSeconds());
//
//                            result.ProductionStatusDuration = elapsed;
//
//                        } else result.ProductionStatusDuration = "00:00";
//                    }
                }
            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

}
