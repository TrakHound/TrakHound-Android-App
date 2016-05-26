// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.device_list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

    private static class ViewHolder {

        // Description
        public TextView Description;
        public TextView DeviceId;
        public TextView Manufacturer;

        public ImageView Logo;
        public ImageView Image;


        // Status
        public View StatusIndicator;
        public ImageView AlertIcon;

        public TextView ProductionStatus;
        public TextView ProductionStatusTimer;

        public TextView OEE;

        public int StatusRed;
        public int StatusYellow;
        public int StatusGreen;
        public int ForegroundNormal;

    }


    public ListAdapter(Context context, ArrayList<ListItem> listItems) {

        super(context, 0, listItems);

        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        // Get the data item for this position
        ListItem item = getItem(position);

        Device device = item.Device;
        DeviceStatus status = item.Status;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);

            holder = new ViewHolder();

            initializeColors(view, holder);
            initializeDescription(view, holder);
            initializeStatusIndicator(view, holder);
            initializeProductionStatus(view, holder);
            initializeOEEStatus(view, holder);

            view.setTag(holder);

        } else {

            holder = (ViewHolder)view.getTag();
        }

        // Set the description components
        setDescription(holder, device);

        // Set the Status Indicator Color
        setStatusIndicator(holder, status);

        // Set Production Status Info
        setProductionStatus(holder, status);

        // Set OEE Status Info
        setOEEStatus(holder, status);

        // Return the completed view to render on screen
        return view;
    }

    private void initializeColors(View view, ViewHolder holder) {

        holder.StatusGreen = ContextCompat.getColor(view.getContext(), R.color.statusGreen);
        holder.StatusRed = ContextCompat.getColor(view.getContext(), R.color.statusRed);
        holder.StatusYellow = ContextCompat.getColor(view.getContext(), R.color.statusYellow);

        holder.ForegroundNormal = ContextCompat.getColor(view.getContext(), R.color.foreground_normal_color);
    }


    private void initializeDescription(View view, ViewHolder holder) {

        View v = null;

        // Description
        v = view.findViewById(R.id.Description);
        if (v != null) holder.Description = (TextView)v;

        // Device Id
        v = view.findViewById(R.id.DeviceId);
        if (v != null) holder.DeviceId = (TextView)v;

        // Manufacturer
        v = view.findViewById(R.id.Manufacturer);
        if (v != null) holder.Manufacturer = (TextView)v;

        // Logo
        v = view.findViewById(R.id.ManufacturerLogo);
        if (v != null) holder.Logo = (ImageView)v;
    }

    private void setDescription (ViewHolder holder, Device device) {

        if (holder.Description != null) holder.Description.setText(device.Description);
        if (holder.DeviceId != null) holder.DeviceId.setText(device.Device_Id);
        if (holder.Manufacturer != null) holder.Manufacturer.setText(device.Manufacturer);

        if (holder.Logo != null) holder.Logo.setImageBitmap(device.Logo);
        if (holder.Image != null) holder.Image.setImageBitmap(device.Image);
    }


    private void initializeStatusIndicator(View view, ViewHolder holder) {

        View v = null;

        // Status Indicator
        v = view.findViewById(R.id.StatusIndicator);
        if (v != null) holder.StatusIndicator = v;

        // Alert Icon
        v = view.findViewById(R.id.AlertIcon);
        if (v != null) holder.AlertIcon = (ImageView)v;
    }

    private void setStatusIndicator(ViewHolder holder, DeviceStatus status) {

        if (holder.StatusIndicator != null && holder.AlertIcon != null) {

            if (status.Production.Alert) {

                holder.StatusIndicator.setBackgroundColor(holder.StatusRed);

                holder.AlertIcon.setVisibility(View.VISIBLE);

            } else {

                holder.AlertIcon.setVisibility(View.INVISIBLE);

                if (status.Production.Idle) holder.StatusIndicator.setBackgroundColor(holder.StatusYellow);
                else if (status.Production.Production) holder.StatusIndicator.setBackgroundColor(holder.StatusGreen);
            }
        }
    }

    private void initializeProductionStatus(View view, ViewHolder holder) {

        View v = null;

        // Production Status
        v = view.findViewById(R.id.ProductionStatus);
        if (v != null) holder.ProductionStatus = (TextView)v;

        // Production Status Timer
        v = view.findViewById(R.id.ProductionStatusTimer);
        if (v != null) holder.ProductionStatusTimer = (TextView)v;
    }

    private void setProductionStatus(ViewHolder holder, DeviceStatus status) {

        if (holder.ProductionStatus != null) holder.ProductionStatus.setText(status.Production.ProductionStatus);

        if (holder.ProductionStatusTimer != null &&
            status.Production.ProductionStatusTimer != null &&
            status.Production.ProductionStatusTimer.length() > 0) {

            int seconds = Integer.parseInt(status.Production.ProductionStatusTimer);

            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

            holder.ProductionStatusTimer.setText(statusPeriod);
        }
    }

    private void initializeOEEStatus(View view, ViewHolder holder) {

        View v = null;

        // OEE
        v = view.findViewById(R.id.OEE);
        if (v != null) holder.OEE = (TextView)v;
    }

    private void setOEEStatus(ViewHolder holder, DeviceStatus status) {

        // Set OEE
        if (holder.OEE != null) {

            double d = status.Oee.Oee * 100;
            String s = String.format("%.0f%%",d);
            holder.OEE.setText(s);

            if (status.Oee.Oee > 0.6)
                holder.OEE.setTextColor(holder.StatusGreen);
            else if (status.Oee.Oee > 0.3)
                holder.OEE.setTextColor(holder.ForegroundNormal);
            else
                holder.OEE.setTextColor(holder.StatusRed);
        }
    }

}
