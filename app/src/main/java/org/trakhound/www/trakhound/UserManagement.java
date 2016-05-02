package org.trakhound.www.trakhound;

import org.trakhound.www.trakhound.http.PostData;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 4/26/2016.
 */
public class UserManagement {

    public static UserConfiguration Login(String id, String password) {

        UserConfiguration result = null;

        if (id != null && id.length() > 0) {

            // Check if Id starts with '%%' which indicates a Local account
            if (id.startsWith("%%")) {

                result = new UserConfiguration();
                result.Username = id;
                result.Type = UserConfiguration.UserType.LOCAL;

            }
            // Otherwise Id is a TrakHound User Account
            else {

                try{

                    String url = "https://www.feenux.com/php/users/login.php";

                    PostData[] postDatas = new PostData[2];
                    postDatas[0] = new PostData("id", id);
                    postDatas[1] = new PostData("password", password);

                    String response = Requests.post(url, postDatas);
                    if (response != null) {

                        result = UserConfiguration.Get(response);
                        result.Type = UserConfiguration.UserType.REMOTE;
                    }
                }
                catch(Exception e){ e.printStackTrace(); }
            }
        }

        return result;

    }
}



