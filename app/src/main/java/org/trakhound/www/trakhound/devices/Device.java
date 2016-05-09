package org.trakhound.www.trakhound.devices;

import android.graphics.Bitmap;

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

    // Table Addresses ------------------------------------------------------------------

    // General
    private static String ADR_UNIQUE_ID = "device_unique_id";
    private static String ADR_ENABLED = "device_client_enabled";

    // Description
    private static String ADR_DESCRIPTION = "device_description";
    private static String ADR_MANUFACTURER = "device_manufacturer";
    private static String ADR_DEVICE_ID = "device_device_id";
    private static String ADR_MODEL = "device_model";
    private static String ADR_SERIAL = "device_serial";
    private static String ADR_CONTROLLER = "device_controller";

    // Images
    private static String ADR_LOGO_URL = "device_logo_url";
    private static String ADR_IMAGE_URL = "device_image_url";

    // ----------------------------------------------------------------------------------


    public DeviceStatus Status;

    public String UniqueId;

    public Boolean Enabled;

    // Description
    public String Description;
    public String Device_Id;
    public String Manufacturer;
    public String Model;
    public String Serial;
    public String Controller;

    // Manufacturer Logo
    public String LogoUrl;
    public Bitmap Logo;

    // Device Image
    public String ImageUrl;
    public Bitmap Image;

    public Device() {

        Status = new DeviceStatus();

    }

    public static Device[] readAll(UserConfiguration userConfig) {

        Device[] result = null;

        String url = "https://www.feenux.com/php/mobile/get.php";

        PostData[] postDatas = new PostData[1];
        postDatas[0] = new PostData("user_id", userConfig.Username);

        String response = Requests.post(url, postDatas);
        if (response != null) {

            Table[] tables = Table.get(response);

            ArrayList<Device> devices = new ArrayList<>();

            for (int i = 0; i < tables.length; i++) {

                Device device = parse(tables[i]);
                if (device != null) {

                    // Get Manufacturer Logo
                    setLogoImage(device);

                    // Get Device Image
                    setDeviceImage(device);

                    // Get any Status data that is available
                    DeviceStatus status = DeviceStatus.parse(tables[i].Data);
                    if (status != null) device.Status = status;

                    devices.add(device);
                }
            }

            Device[] devArray = new Device[devices.size()];
            result = devices.toArray(devArray);

        }

        return result;
    }

    private static Device parse(Table table) {

        Device result = null;

        if (table != null) {

            result = new Device();

            try {

                JSONArray a = new JSONArray(table.Data);
                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.getJSONObject(i);

                    String address = obj.getString("NAME");
                    if (address.equals(ADR_UNIQUE_ID)) result.UniqueId = obj.getString("VALUE");
                    else if (address.equals(ADR_ENABLED)) result.Enabled = Boolean.parseBoolean(obj.getString("VALUE"));

                    else if (address.equals(ADR_DESCRIPTION)) result.Description = obj.getString("VALUE");
                    else if (address.equals(ADR_DEVICE_ID)) result.Device_Id = obj.getString("VALUE");
                    else if (address.equals(ADR_MANUFACTURER)) result.Manufacturer = obj.getString("VALUE");
                    else if (address.equals(ADR_MODEL)) result.Model = obj.getString("VALUE");
                    else if (address.equals(ADR_SERIAL)) result.Serial = obj.getString("VALUE");
                    else if (address.equals(ADR_CONTROLLER)) result.Controller = obj.getString("VALUE");

                    else if (address.equals(ADR_LOGO_URL)) result.LogoUrl = obj.getString("VALUE");
                    else if (address.equals(ADR_IMAGE_URL)) result.ImageUrl = obj.getString("VALUE");
                }
            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

    private static void setLogoImage(Device device) {

        if (device.LogoUrl != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + device.LogoUrl;

            Bitmap logo = Requests.getImage(url);
            if (logo != null) {

                device.Logo = logo;

            }
        }
    }

    private static void setDeviceImage(Device device) {

        if (device.ImageUrl != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + device.ImageUrl;

            Bitmap img = Requests.getImage(url);
            if (img != null) {

                device.Image = img;

            }
        }
    }

}
