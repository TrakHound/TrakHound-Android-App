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
import org.trakhound.www.trakhound.MyApplication;
import org.trakhound.www.trakhound.R;
import org.trakhound.www.trakhound.api.data.DescriptionInfo;
import org.trakhound.www.trakhound.api.data.OeeInfo;
import org.trakhound.www.trakhound.api.data.StatusInfo;

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
        public TextView Model;
        public TextView Serial;

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

        public View OeeLayout;
        public View SelectionArrow;

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

        //Device device = item.Device;
        DescriptionInfo description = item.descriptionInfo;
        //DeviceStatus status = item.Status;

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

        setUserType(holder);

        // Set the description components
        setDescription(holder, item.descriptionInfo);
        //setDescription(holder, device);

        // Set the Status Indicator Color
        setStatusIndicator(holder, item.statusInfo);
        //setStatusIndicator(holder, status);

        // Set Production Status Info
        setProductionStatus(holder, item.statusInfo);
        //setProductionStatus(holder, status);

        // Set OEE Status Info
        setOEEStatus(holder, item.oeeInfo);
        //setOEEStatus(holder, status);

        // Return the completed view to render on screen
        return view;
    }

    private void initializeColors(View view, ViewHolder holder) {

        holder.StatusGreen = ContextCompat.getColor(view.getContext(), R.color.statusGreen);
        holder.StatusRed = ContextCompat.getColor(view.getContext(), R.color.statusRed);
        holder.StatusYellow = ContextCompat.getColor(view.getContext(), R.color.statusYellow);

        holder.ForegroundNormal = ContextCompat.getColor(view.getContext(), R.color.foreground_normal_color);

        View v = view.findViewById(R.id.Arrow);
        if (v != null) holder.SelectionArrow = v;
    }


    private void initializeDescription(View view, ViewHolder holder) {

        View v;

        // Description
        v = view.findViewById(R.id.Description);
        if (v != null) holder.Description = (TextView)v;

        // Device Id
        v = view.findViewById(R.id.DeviceId);
        if (v != null) holder.DeviceId = (TextView)v;

        // Manufacturer
        v = view.findViewById(R.id.Manufacturer);
        if (v != null) holder.Manufacturer = (TextView)v;

        // Model
        v = view.findViewById(R.id.Model);
        if (v != null) holder.Model = (TextView)v;

        // Serial
        v = view.findViewById(R.id.Serial);
        if (v != null) holder.Serial = (TextView)v;

        // Logo
//        v = view.findViewById(R.id.ManufacturerLogo);
//        if (v != null) holder.Logo = (ImageView)v;
    }

    private void setDescription (ViewHolder holder, DescriptionInfo info) {

        if (info != null) {

            if (holder.Description != null) holder.Description.setText(info.description);
            if (holder.DeviceId != null) holder.DeviceId.setText(info.deviceId);
            if (holder.Manufacturer != null) holder.Manufacturer.setText(info.manufacturer);
            if (holder.Model != null) holder.Model.setText(info.model);
            if (holder.Serial != null) holder.Serial.setText(info.serial);

//            if (holder.Logo != null) holder.Logo.setImageBitmap(info.Logo);
//            if (holder.Image != null) holder.Image.setImageBitmap(info.Image);

        }
    }

    private void initializeStatusIndicator(View view, ViewHolder holder) {

        View v;

        // Status Indicator
        v = view.findViewById(R.id.StatusIndicator);
        if (v != null) holder.StatusIndicator = v;

        // Alert Icon
        v = view.findViewById(R.id.AlertIcon);
        if (v != null) holder.AlertIcon = (ImageView)v;
    }

    private void setStatusIndicator(ViewHolder holder, StatusInfo info) {

        if (holder.StatusIndicator != null && holder.AlertIcon != null && info != null && info.deviceStatus != null) {

            if (info.deviceStatus.equals("Alert")) {

                holder.StatusIndicator.setBackgroundColor(holder.StatusRed);

                holder.AlertIcon.setVisibility(View.VISIBLE);

            } else {

                holder.AlertIcon.setVisibility(View.INVISIBLE);

                if (info.deviceStatus.equals("Idle")) holder.StatusIndicator.setBackgroundColor(holder.StatusYellow);
                else if (info.deviceStatus.equals("Active")) holder.StatusIndicator.setBackgroundColor(holder.StatusGreen);
            }
        }
    }

    private void initializeProductionStatus(View view, ViewHolder holder) {

        View v;

        // Production Status
        v = view.findViewById(R.id.DeviceStatus);
        if (v != null) holder.ProductionStatus = (TextView)v;

        // Production Status Timer
        v = view.findViewById(R.id.ProductionStatusTimer);
        if (v != null) holder.ProductionStatusTimer = (TextView)v;
    }

    private void setProductionStatus(ViewHolder holder, StatusInfo info) {

        if (info != null && info.deviceStatus != null) {

            if (holder.ProductionStatus != null) holder.ProductionStatus.setText(info.deviceStatus);

            if (holder.ProductionStatusTimer != null && info.deviceStatusTimer != null) {

                Period period = new Period(info.deviceStatusTimer * 1000);
                String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

                holder.ProductionStatusTimer.setText(statusPeriod);
            }
        }
    }

    private void initializeOEEStatus(View view, ViewHolder holder) {

        View v;

        v = view.findViewById(R.id.OeeLayout);
        if (v != null) holder.OeeLayout = v;

        // OEE
        v = view.findViewById(R.id.OEE);
        if (v != null) holder.OEE = (TextView)v;
    }

    private void setOEEStatus(ViewHolder holder, OeeInfo info) {

        // Set OEE
        if (holder.OEE != null && info != null) {

            double d = info.oee * 100;
            String s = String.format("%.0f%%",d);
            holder.OEE.setText(s);

            if (info.oee > 0.6)
                holder.OEE.setTextColor(holder.StatusGreen);
            else if (info.oee > 0.3)
                holder.OEE.setTextColor(holder.ForegroundNormal);
            else
                holder.OEE.setTextColor(holder.StatusRed);
        }
    }

    private void setUserType(ViewHolder holder) {

        // Selection Arrow
        if (holder.SelectionArrow != null) {

            if (MyApplication.User != null && MyApplication.User.type == 1) holder.SelectionArrow.setVisibility(View.VISIBLE);
            else holder.SelectionArrow.setVisibility(View.GONE);
        }

        // Oee
        if (holder.OeeLayout != null) {

            if (MyApplication.User != null && MyApplication.User.type == 1) holder.OeeLayout.setVisibility(View.VISIBLE);
            else holder.OeeLayout.setVisibility(View.GONE);
        }
    }

}
