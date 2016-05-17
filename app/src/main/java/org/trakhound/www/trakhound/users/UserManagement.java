package org.trakhound.www.trakhound.users;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;


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
            result.Username = userId;
            result.Type = UserConfiguration.UserType.LOCAL;

            if (remember) setRememberToken(userId);
        }

        return result;
    }

    // Basic Login
    public static UserConfiguration basicLogin(String id, String password) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            try{

                String url = "https://www.feenux.com/trakhound/api/login/";

                PostData[] postDatas = new PostData[2];
                postDatas[0] = new PostData("id", id);
                postDatas[1] = new PostData("password", password);

                String response = Requests.post(url, postDatas);
                if (response != null) {

                    result = UserConfiguration.get(response);
                    result.Type = UserConfiguration.UserType.REMOTE;
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }

    // Create Token Login
    public static UserConfiguration createTokenLogin(String id, String password, String senderId) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            try{

                String url = "https://www.feenux.com/trakhound/api/login/";

                PostData[] postDatas = new PostData[5];
                postDatas[0] = new PostData("id", id);
                postDatas[1] = new PostData("password", password);
                postDatas[2] = new PostData("remember", "1");
                postDatas[3] = new PostData("sender_id", senderId);
                postDatas[4] = new PostData("note", "TrakHound Mobile Android App");

                String response = Requests.post(url, postDatas);
                if (response != null) {

                    result = UserConfiguration.get(response);
                    if (result != null) {

                        setRememberToken(result.RememberToken);
                    }

                    result.Type = UserConfiguration.UserType.REMOTE;
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }

    // Delete Token
    public static void deleteToken(String senderId) {

        try{

            String url = "https://www.feenux.com/trakhound/api/logout?sender_id=" + senderId;

            Requests.get(url);
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    // Token Login
    public static UserConfiguration tokenLogin(String token) {

        UserConfiguration result = null;

        if (token != null && token.length() > 0) {

            try{

                String url = "https://www.feenux.com/trakhound/api/login?token=" + token;

                String response = Requests.get(url);
                if (response != null) {

                    result = UserConfiguration.get(response);
                    result.Type = UserConfiguration.UserType.REMOTE;
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }

        return result;
    }



    public static String getSenderId() {

        SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);

        String senderId = prefs.getString("sender_id", null);
        if (senderId == null) {

            // Generate New Sender Id
            senderId = UUID.randomUUID().toString();

            prefs.edit().putString("sender_id", senderId).commit();
        }

        return senderId;
    }

    public static void setRememberToken(String token) {

        SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);

        prefs.edit().putString("login_token", token).commit();
    }

    public static String getRememberToken() {

        SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);

        return prefs.getString("login_token", null);
    }

    public static void clearRememberToken() {

        SharedPreferences prefs = context.getSharedPreferences("org.trakhound.www.trakhound", Context.MODE_PRIVATE);

        prefs.edit().remove("login_token").commit();
    }

}



