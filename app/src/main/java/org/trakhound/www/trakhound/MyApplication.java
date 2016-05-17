package org.trakhound.www.trakhound;

import android.graphics.Bitmap;

import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.users.UserConfiguration;

/**
 * Created by Patrick on 4/27/2016.
 */
public class MyApplication extends android.app.Application {

    public Boolean LoggedIn;

    public UserConfiguration User;

    public Bitmap UserImage;

    public Device[] Devices;

}
