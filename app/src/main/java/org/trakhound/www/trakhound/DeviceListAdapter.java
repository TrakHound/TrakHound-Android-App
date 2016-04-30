package org.trakhound.www.trakhound;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.trakhound.www.trakhound.devices.Device;

import java.util.ArrayList;

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);
        }

        // Set the Status Indicator Color
        SetStatusIndicator(view, device);

        // Lookup view for data population
        TextView tvDescription = (TextView) view.findViewById(R.id.Description);
        TextView tvDeviceId = (TextView) view.findViewById(R.id.DeviceID);
        TextView tvManufacturer = (TextView) view.findViewById(R.id.Manufacturer);
        TextView tvModel = (TextView) view.findViewById(R.id.Model);
        TextView tvSerial = (TextView) view.findViewById(R.id.Serial);


//        TextView tvProductionStatus = (TextView) view.findViewById(R.id.ProductionStatus);

        // Populate the data into the template view using the data object
        tvDescription.setText(device.Description);
        tvDeviceId.setText(device.Device_Id);
        tvManufacturer.setText(device.Manufacturer);
        tvModel.setText(device.Model);
        tvSerial.setText(device.Serial);

//        tvProductionStatus.setText(device.Status.ProductionStatus);

        // Return the completed view to render on screen
        return view;
    }


    private void SetStatusIndicator(View view, Device device) {

        View statusIndicator = (View) view.findViewById(R.id.statusIndicator);
        ImageView alertIcon = (ImageView) view.findViewById(R.id.AlertIcon);

        TextView tvStatus = (TextView) view.findViewById(R.id.Status);
        String status = device.Status.ProductionStatus + " " + device.Status.ProductionStatusDuration;
        tvStatus.setText(status);

        if (device.Status.Alert) {
            statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusRed));
            alertIcon.setVisibility(View.VISIBLE);
        } else {
            alertIcon.setVisibility(View.INVISIBLE);

            if (device.Status.Idle) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusYellow));
            else if (device.Status.Production) statusIndicator.setBackgroundColor(view.getResources().getColor(R.color.statusGreen));
        }

    }

}
