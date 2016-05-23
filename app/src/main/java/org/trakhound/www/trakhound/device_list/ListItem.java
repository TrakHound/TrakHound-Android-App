package org.trakhound.www.trakhound.device_list;

import org.trakhound.www.trakhound.devices.Device;

/**
 * Created by Patrick on 5/23/2016.
 */
public class ListItem {

    public Device Device;
    public DeviceStatus Status;

    public ListItem() {

        Status = new DeviceStatus();

    }

}
