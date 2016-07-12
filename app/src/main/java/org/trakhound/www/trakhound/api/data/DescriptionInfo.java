package org.trakhound.www.trakhound.api.data;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 7/11/2016.
 */
public class DescriptionInfo {

    // Description
    public String description;
    public String deviceId;
    public String manufacturer;
    public String model;
    public String serial;
    public String controller;

    // Manufacturer Logo
    public String logoUrl;
    public Bitmap logo;

    // Device Image
    public String imageUrl;
    public Bitmap image;


    public static DescriptionInfo parse(JSONObject json) {

        DescriptionInfo result = null;

        if (json != null) {

            result = new DescriptionInfo();

            try {

                result.description = json.getString("description");
                result.deviceId = json.getString("device_id");
                result.manufacturer = json.getString("manufacturer");
                result.model = json.getString("model");
                result.serial = json.getString("serial");
                result.controller = json.getString("controller");

                result.imageUrl = json.getString("image_url");
                result.logoUrl = json.getString("logo_url");
            }
            catch (JSONException ex) { ex.getStackTrace(); }
            catch (Exception ex) { ex.getStackTrace(); }
        }

        return result;
    }

}
