// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.users;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 4/26/2016.
 */
public class UserConfiguration {

    public UserType Type;

    public String Id;

    public String Username;
    public String FirstName;
    public String LastName;
    public String Company;
    public String Email;
    public String Phone;
    public String Address1;
    public String Address2;
    public String City;
    public String State;
    public String Country;
    public String ZipCode;
    public String Image_Url;
    public DateTime LastLogin;

    public String PlanType;

    public String RememberToken;
    public String SessionToken;

    public enum UserType { LOCAL, REMOTE }

    public static UserConfiguration get(String json) {

        UserConfiguration result = null;



        try {

            JSONObject obj = new JSONObject(json);

            result = UserConfiguration.get(obj);
//
//            result = new UserConfiguration();
//
//            result.Id = obj.getString("id");
//
//            result.Username = obj.getString("username");
//
//            result.FirstName = obj.optString("first_name");
//            result.LastName = obj.optString("last_name");
//            result.Company = obj.optString("company");
//            result.Email = obj.optString("email");
//            result.Phone = obj.optString("phone");
//            result.Address1 = obj.optString("address1");
//            result.Address2 = obj.optString("address2");
//            result.City = obj.optString("city");
//            result.State = obj.optString("state");
//            result.Country = obj.optString("country");
//            result.ZipCode = obj.optString("zipcode");
//            result.Image_Url = obj.optString("image_url");
//
//            String lastLogin = obj.optString("last_login");
//            if (lastLogin != null) {
//
//                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//                result.LastLogin = formatter.parseDateTime(lastLogin);
//            }
//
////            String planType = obj.optString("planType");
////            if (planType.equals("0")) result.PlanType = "Basic";
////            else if (planType.equals("1")) result.PlanType = "Pro";
//
//
//            result.RememberToken = obj.optString("token");
//            result.SessionToken = obj.optString("session_token");
//
        } catch (JSONException e) { e.printStackTrace(); }

        return result;
    }

    public static UserConfiguration get(JSONObject obj) {

        UserConfiguration result = null;

        try {

            result = new UserConfiguration();

            result.Id = obj.getString("id");

            result.Username = obj.getString("username");

            result.FirstName = obj.optString("first_name");
            result.LastName = obj.optString("last_name");
            result.Company = obj.optString("company");
            result.Email = obj.optString("email");
            result.Phone = obj.optString("phone");
            result.Address1 = obj.optString("address1");
            result.Address2 = obj.optString("address2");
            result.City = obj.optString("city");
            result.State = obj.optString("state");
            result.Country = obj.optString("country");
            result.ZipCode = obj.optString("zipcode");
            result.Image_Url = obj.optString("imageUrl");

            String lastLogin = obj.optString("last_login");
            if (lastLogin != null && !lastLogin.equals("null")) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                result.LastLogin = formatter.parseDateTime(lastLogin);
            }

//            String planType = obj.optString("planType");
//            if (planType.equals("0")) result.PlanType = "Basic";
//            else if (planType.equals("1")) result.PlanType = "Pro";


            result.RememberToken = obj.optString("token");
            result.SessionToken = obj.optString("session_token");

        } catch (JSONException e) { e.printStackTrace(); }

        return result;
    }

}
