// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trakhound.www.trakhound.DeviceList;
import org.trakhound.www.trakhound.MainActivity;
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;
import org.trakhound.www.trakhound.status_info.OeeInfo;
import org.trakhound.www.trakhound.status_info.ProductionInfo;
import org.trakhound.www.trakhound.tools.Json;
import org.trakhound.www.trakhound.users.UserConfiguration;
import org.trakhound.www.trakhound.users.UserManagement;

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

        if (listItems != null) {

            Device[] devices = new Device[listItems.length];

            for (int i = 0; i < listItems.length; i++) {

                devices[i] = listItems[i].Device;
            }

            MyApplication.Devices = devices;
        }

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

                int datastart = 0;

                JSONObject userLogin = a.optJSONObject(0);
                if (userLogin != null) {

                    UserConfiguration userConfig = UserConfiguration.get(userLogin);
                    if (userConfig != null) {

                        if (userConfig.Id.startsWith("%%")) userConfig.Type = UserConfiguration.UserType.LOCAL;
                        else userConfig.Type = UserConfiguration.UserType.REMOTE;

                        MyApplication.User = userConfig;

                        if (loginType == LoginType.CREATE_TOKEN) {

                            UserManagement.setRememberToken(userConfig.RememberToken);
                            UserManagement.setRememberUsername(userConfig.Username);

                        } else if (loginType == LoginType.CREATE_LOCAL_TOKEN) {

                            String username = userConfig.Id.substring(2).toUpperCase();

                            UserManagement.setRememberToken(userConfig.Id);
                            UserManagement.setRememberUsername(username);
                        }

                    } else {

                        MainActivity.error = true;
                    }

                    datastart++;
                }

                // Description Array is first index
                JSONArray descriptionArray = a.optJSONArray(datastart++);
                if (descriptionArray != null) {

                    JSONArray productionArray = null;
                    JSONArray oeeArray = null;

                    if (a.length() > 1) productionArray = a.optJSONArray(datastart++);
                    if (a.length() > 2) oeeArray = a.optJSONArray(datastart++);

                    for (int i = 0; i < descriptionArray.length(); i++) {

                        JSONObject obj = descriptionArray.getJSONObject(i);

                        Device device = Device.parse(obj);
                        if (device != null) {

                            String uniqueId = device.UniqueId;
                            if (uniqueId != null) {

                                ListItem listItem = new ListItem();
                                listItem.Device = device;

                                // Get Production Info
                                obj = Json.find(productionArray, "unique_id", uniqueId);
                                if (obj != null) {

                                    listItem.Status.Production = ProductionInfo.parse(obj);
                                }

                                // Get Oee Info
                                obj = Json.find(oeeArray, "unique_id", uniqueId);
                                if (obj != null) {

                                    listItem.Status.Oee = OeeInfo.parse(obj);
                                }

                                result.add(listItem);
                            }
                        }
                    }
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

            //String url = "https://www.feenux.com/trakhound/api/mobile/get/";
            String url = "http://192.168.1.129/api/data/get/index.php";

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

            //String url = "https://www.feenux.com/trakhound/api/mobile/get/?";
            String url = "http://192.168.1.129/api/data/get/index.php";


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

//                String url = "https://www.feenux.com/trakhound/api/mobile/get/?" +
//                        "token=" + URLEncoder.encode(userConfig.SessionToken, "UTF-8") +
//                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
//                        "&command=1101"; // Get Description, Status, and Oee tables

                String url = "http://192.168.1.129/api/data/get/?" +
                        "token=" + URLEncoder.encode(userConfig.SessionToken, "UTF-8") +
                        "&sender_id=" + URLEncoder.encode(UserManagement.getSenderId(), "UTF-8") +
                        "&command=1101"; // Get Description, Status, and Oee tables

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
