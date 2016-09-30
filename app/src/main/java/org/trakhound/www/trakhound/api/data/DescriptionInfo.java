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

            result.description = json.optString("description");
            result.deviceId = json.optString("device_id");
            result.manufacturer = json.optString("manufacturer");
            result.model = json.optString("model");
            result.serial = json.optString("serial");
            result.controller = json.optString("controller");

            result.imageUrl = json.optString("image_url");
            result.logoUrl = json.optString("logo_url");
        }

        return result;
    }

}
