package org.trakhound.www.trakhound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import org.joda.time.Period;

import org.trakhound.www.trakhound.devices.Device;


/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceListAdapter extends ArrayAdapter<Device> {

    private Context context;

    public DeviceListAdapter(Context context, ArrayList<Device> devices) {

        super(context, 0, devices);

        this.context = context;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Get the data item for this position
        Device device = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
//            view = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_device_list_item, parent, false);
        }

        // Set the Status Indicator Color
        SetStatusIndicator(view, device);

        // Set Production Status Info
        SetProductionStatus(view, device);

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


    private void SetStatusIndicator(View view, Device device) {

        View statusIndicator = view.findViewById(R.id.StatusIndicator);
        ImageView alertIcon = (ImageView) view.findViewById(R.id.AlertIcon);

        if (device.Status.Alert) {
            statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusRed));
            alertIcon.setVisibility(View.VISIBLE);
        } else {
            alertIcon.setVisibility(View.INVISIBLE);

            if (device.Status.Idle) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusYellow));
            else if (device.Status.Production) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusGreen));
        }
    }

    private void SetProductionStatus(View view, Device device) {

        // Set Production Status
        TextView productionStatus = (TextView) view.findViewById(R.id.ProductionStatus);
        String status = device.Status.ProductionStatus;
        productionStatus.setText(status);

        // Set Production Status Duration
        TextView productionStatusTimer = (TextView) view.findViewById(R.id.ProductionStatusTimer);
        String statusTimer = device.Status.ProductionStatusTimer;
        if (statusTimer != null && statusTimer.length() > 0) {

            int seconds = Integer.parseInt(statusTimer);

            Period period = new Period(seconds * 1000);
            String statusPeriod = String.format("%02d:%02d:%02d", period.getHours(), period.getMinutes(), period.getSeconds());

            productionStatusTimer.setText(statusPeriod);
        }
    }
}
