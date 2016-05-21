package org.trakhound.www.trakhound.devices;

import org.trakhound.www.trakhound.users.UserConfiguration;

/**
 * Created by Patrick on 5/20/2016.
 */
public class DeviceStatusRequest {

    public UserConfiguration User;

    public boolean GetStatus;
    public boolean GetController;
    public boolean GetOee;
    public boolean GetTimers;

    public String getCommand() {

        String result = "0";

        if (GetStatus) result += "1";
        else result += "0";

        if (GetController) result += "1";
        else result += "0";

        if (GetOee) result += "1";
        else result += "0";

        if (GetTimers) result += "1";
        else result += "0";

        return result;
    }

}
