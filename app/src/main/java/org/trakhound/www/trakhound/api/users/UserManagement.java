// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.api.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.UUID;

import org.trakhound.www.trakhound.api.ApiConfiguration;
import org.trakhound.www.trakhound.api.http.PostData;
import org.trakhound.www.trakhound.api.http.Requests;


/**
 * Created by Patrick on 4/26/2016.
 */
public class UserManagement {

    public static Context context;


    // Local Login
    public static UserConfiguration localLogin(String id, boolean remember) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            id = id.toUpperCase();

            String userId = "%%" + id;

            result = new UserConfiguration();
            result.username = userId;

            if (remember) {
                setRememberToken(userId);
                setRememberUsername(userId);
            }
        }

        return result;
    }



    // Basic Login
    public static UserConfiguration basicLogin(String id, String password, String note) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            try{

                String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "users/login/index.php").toString();

                PostData[] postDatas = new PostData[2];
                postDatas[0] = new PostData("id", id);
                postDatas[1] = new PostData("password", password);
                postDatas[2] = new PostData("sender_id", getSenderId());
                postDatas[3] = new PostData("note", note);

                String response = Requests.post(url, postDatas);
                if (response != null) {

                    result = UserConfiguration.get(response);
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }

    // Create Token Login
    public static UserConfiguration createTokenLogin(String id, String password, String note) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            try{

                String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "users/login/index.php").toString();

                PostData[] postDatas = new PostData[5];
                postDatas[0] = new PostData("id", id);
                postDatas[1] = new PostData("password", password);
                postDatas[2] = new PostData("remember", "1");
                postDatas[3] = new PostData("sender_id", getSenderId());
                postDatas[4] = new PostData("note", note);

                String response = Requests.post(url, postDatas);
                if (response != null) {

                    result = UserConfiguration.get(response);
                    if (result != null) {

                        setRememberToken(result.rememberToken);
                        setRememberUsername(result.username);
                    }
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }

    // Token Login
    public static UserConfiguration tokenLogin(String token, String note) {

        UserConfiguration result = null;

        if (token != null && token.length() > 0) {

            try{

                String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "users/login/index.php").toString();
                url += "?token=" + token;
                url += "&sender_id=" + getSenderId();
                url += "&note=" + note;

                String response = Requests.get(url);
                if (response != null) {

                    result = UserConfiguration.get(response);
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }


    // Logout
    public static boolean logout() {

        try{

            String url = Uri.withAppendedPath(ApiConfiguration.apiHost, "users/logout/?sender_id=" + getSenderId()).toString();

            String response = Requests.get(url);
            if (response != null) {

                return true;
            }
        }
        catch(Exception e){ e.printStackTrace(); }

        return false;
    }



    public static String getSenderId() {

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {
                String senderId = prefs.getString("sender_id", null);
                if (senderId == null) {

                    // Generate New Sender Id
                    senderId = UUID.randomUUID().toString();

                    prefs.edit().putString("sender_id", senderId).apply();
                }

                return senderId;
            }
        }

        return null;
    }

    public static void setRememberToken(String token) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {

                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {

                    editor.putString("login_token", token);
                    editor.apply();
                }
            }
        }
    }

    public static String getRememberToken() {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) return prefs.getString("login_token", null);
        }

        return null;
    }

    public static void clearRememberToken() {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().remove("login_token").apply();
        }
    }


    public static void setRememberUsername(String username) {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) {

                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {

                    editor.putString("login_username", username);
                    editor.apply();
                }
            }
        }
    }

    public static String getRememberUsername() {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) return prefs.getString("login_username", null);
        }

        return null;
    }

    public static void clearRememberUsername() {

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);
            if (prefs != null) prefs.edit().remove("login_username").apply();
        }
    }

}



