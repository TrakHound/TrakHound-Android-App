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

            JSONObject root = new JSONObject(json);

            result = new UserConfiguration();

            result.Id = root.getString("id");

            result.Username = root.getString("username");

            result.FirstName = root.optString("firstName");
            result.LastName = root.optString("lastName");
            result.Company = root.optString("company");
            result.Email = root.optString("email");
            result.Phone = root.optString("phone");
            result.Address1 = root.optString("address1");
            result.Address2 = root.optString("address2");
            result.City = root.optString("city");
            result.State = root.optString("state");
            result.Country = root.optString("country");
            result.ZipCode = root.optString("zipcode");
            result.Image_Url = root.optString("imageUrl");

            String lastLogin = root.optString("lastLogin");
            if (lastLogin != null) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                result.LastLogin = formatter.parseDateTime(lastLogin);
            }

            String planType = root.optString("planType");
            if (planType.equals("0")) result.PlanType = "Basic";
            else if (planType.equals("1")) result.PlanType = "Pro";


            result.RememberToken = root.optString("token");
            result.SessionToken = root.optString("sessionToken");

        } catch (JSONException e) { e.printStackTrace(); }

        return result;
    }

}
