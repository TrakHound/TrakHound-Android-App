// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.Period;
import org.trakhound.www.trakhound.R;
import org.trakhound.www.trakhound.devices.Device;

import java.util.ArrayList;


/**
 * Created by Patrick on 4/28/2016.
 */
public class ListAdapter extends ArrayAdapter<ListItem> {

    private Context context;

    public ListAdapter(Context context, ArrayList<ListItem> listItems) {

        super(context, 0, listItems);

        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Get the data item for this position
        ListItem item = getItem(position);

        Device device = item.Device;
        DeviceStatus status = item.Status;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);
        }

        // Set the Status Indicator Color
        setStatusIndicator(view, status);

        // Set Production Status Info
        setProductionStatus(view, status);

        // Set OEE Status Info
        setOEEStatus(view, status);

        // Lookup view for data population
        TextView description = (TextView) view.findViewById(R.id.Description);
        TextView deviceId = (TextView) view.findViewById(R.id.DeviceId);
        TextView manufacturer = (TextView) view.findViewById(R.id.Manufacturer);

        // Images
        ImageView logo = (ImageView) view.findViewById(R.id.ManufacturerLogo);
        ImageView image = (ImageView) view.findViewById(R.id.DeviceImage);

        // Populate the data into the template view using the data object
        description.setText(device.Description);
        deviceId.setText(device.Device_Id);
        manufacturer.setText(device.Manufacturer);
        logo.setImageBitmap(device.Logo);
        image.setImageBitmap(device.Image);

        // Return the completed view to render on screen
        return view;
    }

    private void setStatusIndicator(View view, DeviceStatus status) {

        View statusIndicator = view.findViewById(R.id.StatusIndicator);
        ImageView alertIcon = (ImageView) view.findViewById(R.id.AlertIcon);

        if (status.Production.Alert) {
            statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusRed));
            alertIcon.setVisibility(View.VISIBLE);
        } else {
            alertIcon.setVisibility(View.INVISIBLE);

            if (status.Production.Idle) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusYellow));
            else if (status.Production.Production) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusGreen));
        }
    }

    private void setProductionStatus(View view, DeviceStatus status) {

        // Set Production Status
        TextView productionStatus = (TextView) view.findViewById(R.id.ProductionStatus);
        String statusText = status.Production.ProductionStatus;
        productionStatus.setText(statusText);

        // Set Production Status Duration
        TextView productionStatusTimer = (TextView) view.findViewById(R.id.ProductionStatusTimer);
        String statusTimer = status.Production.ProductionStatusTimer;
        if (statusTimer != null && statusTimer.length() > 0) {

            int seconds = Integer.parseInt(statusTimer);

            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

            productionStatusTimer.setText(statusPeriod);
        }
    }

    private void setOEEStatus(View view, DeviceStatus status) {

        // Set OEE
        TextView txt = (TextView) view.findViewById(R.id.OEE);
        if (txt != null) {

            double d = status.Oee.Oee * 100;
            String s = String.format("%.0f%%",d);
            txt.setText(s);

            if (status.Oee.Oee > 0.6)
                txt.setTextColor(view.getResources().getColor(R.color.statusGreen));
            else if (status.Oee.Oee > 0.3)
                txt.setTextColor(view.getResources().getColor(R.color.foreground_normal_color));
            else
                txt.setTextColor(view.getResources().getColor(R.color.statusRed));
        }
    }
}
