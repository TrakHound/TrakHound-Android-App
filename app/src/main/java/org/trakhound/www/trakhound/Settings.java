package org.trakhound.www.trakhound;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadDeviceUpdateInterval();

    }

    private void loadDeviceUpdateInterval() {

        final TextView txt = (TextView)findViewById(R.id.UpdateIntervalText);

        SeekBar seekBar = (SeekBar)findViewById(R.id.UpdateIntervalSeekBar);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            int progress = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                txt.setText(getDuration(progresValue));
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

//                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



//                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();

            }

        });


    }

    private static String getDuration(int milliseconds) {

        Period p = new Period(milliseconds);

        return String.format("%02d:%02d:%02d", p.getHours(), p.getMinutes(), p.getSeconds());
    }
}
