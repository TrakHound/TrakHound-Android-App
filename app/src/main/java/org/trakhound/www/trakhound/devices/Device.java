package org.trakhound.www.trakhound.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.UserConfiguration;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/27/2016.
 */
public class Device {

    private static String TABLE_DELIMITER_START = "^^^";
    private static String TABLE_DELIMITER_END = "~~~";

    // Table Addresses ------------------------------------------------------------------

    // General
    private static String ADR_UNIQUE_ID = "/UniqueId";
    private static String ADR_CLIENT_ENABLED = "/ClientEnabled";
    private static String ADR_INDEX = "/Index";

    // Description
    private static String ADR_DESCRIPTION = "/Description/Description";
    private static String ADR_MANUFACTURER = "/Description/Manufacturer";
    private static String ADR_DEVICE_ID = "/Description/Device_ID";
    private static String ADR_MODEL = "/Description/Model";
    private static String ADR_SERIAL = "/Description/Serial";
    private static String ADR_CONTROLLER = "/Description/Controller";

    // Database
    private static String ADR_DATABASE_ID = "/DatabaseId";

    // ----------------------------------------------------------------------------------


    public String UniqueId;
    public String Tablename;
    public Integer Index;
    public String DatabaseId;

    public Boolean ClientEnabled;

    // Description
    public String Description;
    public String Device_Id;
    public String Manufacturer;
    public String Model;
    public String Serial;
    public String Controller;


    public static Device[] ReadAll(UserConfiguration userConfig) {

        Device[] result = null;

        String url = "https://www.feenux.com/php/configurations/v2/getconfigurationslist.php";

        PostData[] postDatas = new PostData[1];
        postDatas[0] = new PostData("username", userConfig.Username);

        String response = Requests.POST(url, postDatas);
        if (response != null) {

            String[] tables = GetTablesJSON(response);
            if (tables != null) {

                ArrayList<Device> devices = new ArrayList<>();

//                result = new Device[tables.length];

                for (int i = 0; i < tables.length; i++) {

                    Table table = GetDeviceTable(tables[i]);
                    if (table != null) {
                        Device device = Parse(table);
                        if (device != null) {
                            devices.add(device);
                        }
                    }
                }

                Device[] devArray = new Device[devices.size()];
                result = devices.toArray(devArray);
            }
        }

        return result;
    }

    private static String[] GetTablesJSON(String s) {

        return s.split(TABLE_DELIMITER_START);

    }

    private static Table GetDeviceTable(String s) {

        Table result = null;

        int delimiter = s.indexOf(TABLE_DELIMITER_END);
        if (delimiter > 0) {

            result = new Table();
            result.Name = s.substring(TABLE_DELIMITER_START.length(), delimiter);
            result.Data = s.substring(delimiter + TABLE_DELIMITER_END.length());

        }

        return result;
    }

    private static Device Parse(Table table) {

        Device result = null;

        if (table != null) {

            result = new Device();

            // Set Table name
            result.Tablename = table.Name;

            try {

                JSONArray a = new JSONArray(table.Data);
                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    String address = obj.getString("address");
                    if (address.equals(ADR_UNIQUE_ID)) result.UniqueId = obj.getString("value");
                    else if (address.equals(ADR_INDEX)) result.Index = Integer.parseInt(obj.getString("value"));
                    else if (address.equals(ADR_CLIENT_ENABLED)) result.ClientEnabled = Boolean.parseBoolean(obj.getString("value"));
                    else if (address.equals(ADR_DATABASE_ID)) result.DatabaseId = obj.getString("value");

                    else if (address.equals(ADR_DESCRIPTION)) result.Description = obj.getString("value");
                    else if (address.equals(ADR_DEVICE_ID)) result.Device_Id = obj.getString("value");
                    else if (address.equals(ADR_MANUFACTURER)) result.Manufacturer = obj.getString("value");
                    else if (address.equals(ADR_MODEL)) result.Model = obj.getString("value");
                    else if (address.equals(ADR_SERIAL)) result.Serial = obj.getString("value");
                    else if (address.equals(ADR_CONTROLLER)) result.Controller = obj.getString("value");

                }

            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

}
