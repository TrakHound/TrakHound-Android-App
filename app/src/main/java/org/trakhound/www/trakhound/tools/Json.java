package org.trakhound.www.trakhound.tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Patrick on 5/26/2016.
 */
public class Json {

    public static JSONObject find(JSONArray a, String key, String value) {

        JSONObject result = null;

        if (a != null) {

            for (int i = 0; i < a.length(); i++) {

                try {

                    JSONObject obj = a.getJSONObject(i);

                    String s = obj.getString(key);
                    if (s.equals(value)) {

                        result = obj;
                        break;
                    }
                }
                catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            }
        }

        return result;
    }

    public static JSONObject[] findAll(JSONArray a, String key, String value) {

        ArrayList<JSONObject> result = new ArrayList<>();

        if (a != null) {

            for (int i = 0; i < a.length(); i++) {

                try {

                    JSONObject obj = a.getJSONObject(i);

                    String s = obj.getString(key);
                    if (s.equals(value)) {

                        result.add(obj);
                    }
                }
                catch (JSONException ex) { Log.d("Exception", ex.getMessage()); }
            }
        }

        // Convert list and return as array
        JSONObject[] statusArray = new JSONObject[result.size()];
        return result.toArray(statusArray);
    }

}
