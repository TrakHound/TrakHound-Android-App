package org.trakhound.www.trakhound.device_list;

import org.trakhound.www.trakhound.api.data.DescriptionInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;
import org.trakhound.www.trakhound.api.data.ControllerInfo;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.TimersInfo;

/**
 * Created by Patrick on 5/23/2016.
 */
public class ListItem {

    public String uniqueId;
    public int index;
    public boolean enabled;

    public DescriptionInfo descriptionInfo;
    public StatusInfo statusInfo;
    public ControllerInfo controllerInfo;
    public OeeInfo oeeInfo;
    public TimersInfo timersInfo;

    public ListItem() {

        descriptionInfo = new DescriptionInfo();
        statusInfo = new StatusInfo();
        controllerInfo = new ControllerInfo();
        oeeInfo = new OeeInfo();
        timersInfo = new TimersInfo();
    }

}
