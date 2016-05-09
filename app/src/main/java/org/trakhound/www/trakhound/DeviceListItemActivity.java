package org.trakhound.www.trakhound;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DeviceListItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_item);



    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        Toast.makeText(getApplicationContext(), "TOUCH", Toast.LENGTH_SHORT).show();

        return super.onTouchEvent(e);
    }

}
