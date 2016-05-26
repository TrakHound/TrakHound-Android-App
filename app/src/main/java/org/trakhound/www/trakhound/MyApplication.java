// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

import android.graphics.Bitmap;

import org.trakhound.www.trakhound.device_list.ListItem;
import org.trakhound.www.trakhound.devices.Device;
import org.trakhound.www.trakhound.users.UserConfiguration;

/**
 * Created by Patrick on 4/27/2016.
 */
public class MyApplication extends android.app.Application {

    public static Boolean LoggedIn;

    public static UserConfiguration User;

    public static Bitmap UserImage;

    public static Device[] Devices;

    public static ListItem[] ListItems;

}
