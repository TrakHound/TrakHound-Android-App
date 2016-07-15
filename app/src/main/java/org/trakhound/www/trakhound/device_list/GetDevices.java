// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MainActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.api.data.DescriptionInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.data.ControllerInfo;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.TimersInfo;
import org.trakhound.www.trakhound.api.http.PostData;
import org.trakhound.www.trakhound.api.http.Requests;
import org.trakhound.www.trakhound.api.ApiConfiguration;
import org.trakhound.www.trakhound.api.users.UserConfiguration;
import org.trakhound.www.trakhound.api.users.UserManagement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class GetDevices extends AsyncTask<String,Void,ListItem[]> {

    private DeviceList deviceList;
    private Context context;
    private static LoginType loginType;


    public enum LoginType { NONE, BASIC, CREATE_TOKEN, TOKEN, LOCAL, CREATE_LOCAL_TOKEN }

    public GetDevices(DeviceList deviceList, LoginType type) {

        this.deviceList = deviceList;
        this.context = deviceList;
        loginType = type;
    }

    public GetDevices(Context context, LoginType type) {

        this.context = context;
        loginType = type;
    }

    protected void onPreExecute(){

        MainActivity.error = false;
    }

    @Override
    protected ListItem[] doInBackground(String... arg0) {

        ListItem[] result = null;

        if (loginType == LoginType.TOKEN) {

            String token = arg0[0];
            result = get(token);

        } else if (loginType == LoginType.BASIC) {

            String username = arg0[0];
            String password = arg0[1];
            result = get(username, password, false);

        } else if (loginType == LoginType.CREATE_TOKEN) {

            String username = arg0[0];
            String password = arg0[1];
            result = get(username, password, true);

        } else if (loginType == LoginType.LOCAL || loginType == LoginType.CREATE_LOCAL_TOKEN) {

            String id = arg0[0];
            result = getLocal(id);

        } else {

            UserConfiguration userConfig = MyApplication.User;
            if (userConfig != null) {

                result = get(userConfig);
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ListItem[] listItems){

        MyApplication.Devices = null;
        MyApplication.ListItems = null;

        MyApplication.ListItems = listItems;

        if (deviceList != null) {

            deviceList.addDevices();
            deviceList.hideLoading();

        } else {

            if (listItems == null) {

                UserManagement.clearRememberToken();
                UserManagement.clearRememberUsername();

                if (context != null) context.startActivity(new Intent(context, MainActivity.class));

            } else {

                Intent deviceListIntent = new Intent(context, DeviceList.class);
                deviceListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                // Open the Device List Page
                context.startActivity(deviceListIntent);
            }
        }
    }

    private static ListItem[] processResponse(String response) {

        if (response != null && response.length() > 0) {

            try {

                // Get Parent Array from response string
                JSONArray a = new JSONArray(response);
                return processResponseArray(a);

            } catch (JSONException ex) {

                Log.d("Exception", ex.getMessage());

                MainActivity.error = true;
            }
        }

        return null;
    }

    private static ListItem[] processResponseArray(JSONArray a) {

        if (a != null && a.length() > 0) {

            // Create ArrayList to return
            ArrayList<ListItem> result = new ArrayList<>();

            try {

                for (int i = 0; i < a.length(); i++) {

                    JSONObject obj = a.optJSONObject(i);

                    ListItem item = new ListItem();
                    item.uniqueId = obj.getString("unique_id");

                    item.descriptionInfo = DescriptionInfo.parse(obj.optJSONObject("description"));
                    item.statusInfo = StatusInfo.parse(obj.optJSONObject("status"));
                    item.controllerInfo = ControllerInfo.parse(obj.optJSONObject("controller"));
                    item.oeeInfo = OeeInfo.parse(obj.optJSONObject("oee"));
                    item.timersInfo = TimersInfo.parse(obj.optJSONObject("timers"));

                    result.add(item);
                }

                ListItem[] resultArray = new ListItem[result.size()];
                return result.toArray(resultArray);

            } catch (JSONException ex) {

                Log.d("Exception", ex.getMessage());

                MainActivity.error = true;
            }
        } else {

            MainActivity.error = true;
        }

        return null;
    }

    public static ListItem[] get(String token) {

        if (token != null) {

            String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "data/get/index.php").toString();

            PostData[] postDatas = new PostData[3];
            postDatas[0] = new PostData("token", token);
            postDatas[1] = new PostData("sender_id", UserManagement.getSenderId());
            postDatas[2] = new PostData("command", "1101"); // Get Description, Status, and Oee tables

            String response = Requests.post(url, postDatas);
            return processResponse(response);
        }

        return null;
    }

    public static ListItem[] get(String id, String password, boolean remember) {

        if (id != null && password != null) {

            String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "data/get/index.php").toString();

            PostData[] postDatas = new PostData[6];
            postDatas[0] = new PostData("id", id);
            postDatas[1] = new PostData("password", password);
            postDatas[2] = new PostData("sender_id", UserManagement.getSenderId());
            postDatas[3] = new PostData("command", "1101"); // Get Description, Status, and Oee tables
            if (remember) postDatas[4] = new PostData("remember", "1");
            else postDatas[4] = new PostData("remember", "0");
            postDatas[5] = new PostData("note", "TrakHound Mobile Android App - Get Devices Login");

            String response = Requests.post(url, postDatas);
            return processResponse(response);
        }

        return null;
    }

    public static ListItem[] get(UserConfiguration userConfig) {

        if (userConfig != null) {

            try {

                String urlSuffix = "data/get/?" +
                        "token=" + URLEncoder.encode(userConfig.sessionToken, "UTF-8") +
                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
                        "&command=1101"; // Get Description, Status, and Oee tables

                String url = Uri.withAppendedPath(ApiConfiguration.apiHost, urlSuffix).toString();

                String response = Requests.get(url);
                return processResponse(response);

            } catch (UnsupportedEncodingException ex) { Log.d("Exception", ex.getMessage()); }
        }

        return null;
    }

    public static ListItem[] getLocal(String id) {

        if (id != null) {

            String url = "https://www.feenux.com/trakhound/api/mobile/get/";

            String userId = id;
            if (!userId.startsWith("%%")) userId = "%%" + userId;

            PostData[] postDatas = new PostData[3];
            postDatas[0] = new PostData("id", userId);
            postDatas[1] = new PostData("sender_id", UserManagement.getSenderId());
            postDatas[2] = new PostData("command", "1101"); // Get Description, Status, and Oee tables

            String response = Requests.post(url, postDatas);
            return processResponse(response);
        }

        return null;
    }

}
