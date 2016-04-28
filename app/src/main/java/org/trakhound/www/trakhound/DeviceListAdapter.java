package org.trakhound.www.trakhound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.trakhound.www.trakhound.devices.Device;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/28/2016.
 */
public class DeviceListAdapter extends ArrayAdapter<Device> {

    public DeviceListAdapter(Context context, ArrayList<Device> devices) {

        super(context, 0, devices);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Device device = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_device_item, parent, false);
        }

        // Lookup view for data population
        TextView tvDescription = (TextView) convertView.findViewById(R.id.Description);
        TextView tvManufacturer = (TextView) convertView.findViewById(R.id.Manufacturer);
        TextView tvModel = (TextView) convertView.findViewById(R.id.Model);
        TextView tvSerial = (TextView) convertView.findViewById(R.id.Serial);

        // Populate the data into the template view using the data object
        tvDescription.setText(device.Description);
        tvManufacturer.setText(device.Manufacturer);
        tvModel.setText(device.Model);
        tvSerial.setText(device.Serial);

        // Return the completed view to render on screen
        return convertView;
    }

}
