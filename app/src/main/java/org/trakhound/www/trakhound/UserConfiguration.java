package org.trakhound.www.trakhound;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 4/26/2016.
 */
public class UserConfiguration {

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

    public static UserConfiguration Get(String json) {

        UserConfiguration result = null;

        try {
            JSONArray a = new JSONArray(json);

            if (a.length() > 0) {
                JSONObject root = a.getJSONObject(0);

                result = new UserConfiguration();

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
                result.Image_Url = root.optString("image_url");
            }

        } catch (JSONException e) { e.printStackTrace(); }

        return result;
    }

}
