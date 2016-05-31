// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.http.Requests;
import org.trakhound.www.trakhound.status_info.OeeInfo;
import org.trakhound.www.trakhound.status_info.ProductionInfo;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceStatus {

    public String UniqueId;

    public ProductionInfo Production;
    public OeeInfo Oee;

    public DeviceStatus() {

        UniqueId = null;

        Production = new ProductionInfo();
        Oee = new OeeInfo();
    }

    public static DeviceStatus[] get(UserConfiguration userConfig) {

        if (userConfig != null) {

            try {

                String url = "https://www.feenux.com/trakhound/api/mobile/get/?" +
                        "token=" + URLEncoder.encode(userConfig.SessionToken, "UTF-8") +
                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
                        "&command=0101"; // Get Description, Status, and Oee tables

                String response = Requests.get(url);
                if (response != null && response.length() > 0) {

                    ArrayList<DeviceStatus> statuses = new ArrayList<>();

                    ArrayList<ProductionInfo> productionInfos = new ArrayList<>();
                    ArrayList<OeeInfo> oeeInfos = new ArrayList<>();

                    try {

                        JSONArray a = new JSONArray(response);

                        // Status is first array
                        productionInfos = processStatusArray(a.getJSONArray(0));

                        // Oee is second array
                        oeeInfos = processOeeArray(a.getJSONArray(1));

                    }
                    catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }


                    // Add StatusInfos
                    for (int i = 0; i < productionInfos.size(); i++) {

                        DeviceStatus status = null;

                        for (int x = 0; x < statuses.size(); x++) {

                            if (statuses.get(x).UniqueId.equals(productionInfos.get(i).UniqueId)) {

                                status = statuses.get(x);
                                status.Production = productionInfos.get(i);
                            }
                        }

                        if (status == null) {

                            status = new DeviceStatus();
                            status.UniqueId = productionInfos.get(i).UniqueId;
                            status.Production = productionInfos.get(i);
                            statuses.add(status);
                        }
                    }

                    // Add OeeInfos
                    for (int i = 0; i < oeeInfos.size(); i++) {

                        DeviceStatus status = null;

                        for (int x = 0; x < statuses.size(); x++) {

                            if (statuses.get(x).UniqueId.equals(oeeInfos.get(i).UniqueId)) {

                                status = statuses.get(x);
                                status.Oee = oeeInfos.get(i);
                            }
                        }

                        if (status == null) {

                            status = new DeviceStatus();
                            status.UniqueId = oeeInfos.get(i).UniqueId;
                            status.Oee = oeeInfos.get(i);
                            statuses.add(status);
                        }
                    }


                    DeviceStatus[] statusArray = new DeviceStatus[statuses.size()];
                    return statuses.toArray(statusArray);
                }

            } catch (UnsupportedEncodingException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return null;
    }

    public static ArrayList<ProductionInfo> processStatusArray(JSONArray a) {

        ArrayList<ProductionInfo> result = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {

            try {

                JSONObject obj = a.getJSONObject(i);

                ProductionInfo status = ProductionInfo.parse(obj);

                if (status != null) result.add(status);
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return result;
    }

    public static ArrayList<OeeInfo> processOeeArray(JSONArray a) {

        ArrayList<OeeInfo> result = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {

            try {

                JSONObject obj = a.getJSONObject(i);

                OeeInfo status = OeeInfo.parse(obj);

                if (status != null) result.add(status);
            }
            catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return result;
    }

}
