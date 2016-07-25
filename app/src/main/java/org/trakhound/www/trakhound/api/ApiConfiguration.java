package org.trakhound.www.trakhound.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by Patrick on 7/9/2016.
 */
public class ApiConfiguration {

    public static Context context;

    public static Uri apiHost = Uri.parse("http://trakhound.com/api/");

    public String host;
    public String path;


    public static String getSavedHost() {

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {

                return prefs.getString("api_host", null);
            }
        }

        return null;
    }

    public static void setSavedHost(String host) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_host", host).apply();
        }
    }

    public static String getSavedPath() {

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {

                return prefs.getString("api_path", null);
            }
        }

        return null;
    }

    public static void setSavedPath(String path) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().putString("api_path", path).apply();
        }
    }

}
