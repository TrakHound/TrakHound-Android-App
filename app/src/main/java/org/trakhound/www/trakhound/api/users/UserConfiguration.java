// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.api.users;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 4/26/2016.
 */
public class UserConfiguration {

//    public UserType type;


    public String id;

    public String username;
    public String firstName;
    public String lastName;
    public String company;
    public String email;
    public String phone;
    public String address1;
    public String address2;
    public String city;
    public String state;
    public String country;
    public String zipCode;
    public String imageUrl;
    public DateTime lastLogin;

    public int type;

    public String rememberToken;
    public String sessionToken;

//    public enum UserType { LOCAL, REMOTE }

    public static UserConfiguration get(String json) {

        try {

            JSONObject obj = new JSONObject(json);

            return UserConfiguration.get(obj);

        } catch (JSONException e) { e.printStackTrace(); }

        return null;
    }

    public static UserConfiguration get(JSONObject obj) {

        try {

            UserConfiguration result = new UserConfiguration();

            result.id = obj.getString("id");

            result.username = obj.getString("username");

            result.firstName = obj.optString("first_name");
            result.lastName = obj.optString("last_name");
            result.company = obj.optString("company");
            result.email = obj.optString("email");
            result.phone = obj.optString("phone");
            result.address1 = obj.optString("address1");
            result.address2 = obj.optString("address2");
            result.city = obj.optString("city");
            result.state = obj.optString("state");
            result.country = obj.optString("country");
            result.zipCode = obj.optString("zipcode");
            result.imageUrl = obj.optString("imageUrl");

            String lastLogin = obj.optString("last_login");
            if (lastLogin != null && !lastLogin.equals("null")) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                result.lastLogin = formatter.parseDateTime(lastLogin);
            }

            result.type = obj.optInt("type");

            result.rememberToken = obj.optString("token");
            result.sessionToken = obj.optString("session_token");

            return result;

        } catch (JSONException e) { e.printStackTrace(); }

        return null;
    }

}
