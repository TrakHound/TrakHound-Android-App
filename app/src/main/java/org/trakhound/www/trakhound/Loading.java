package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Loading extends AppCompatActivity {

    public static String loadingText = "loading_text";

    public static Loading instance;

    public static void Open(Context context, String loadingText) {

        if (instance == null) {

            Intent loading = new Intent(context, Loading.class);
            loading.putExtra(Loading.loadingText, loadingText);
            context.startActivity(loading);

        } else {

            instance.setLoadingText(loadingText);
        }
    }

    public static void Close() {

        instance = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent myIntent = getIntent();
        setLoadingText(myIntent.getStringExtra(loadingText));

        // Set Status Bar Color
        setStatusBar();

        instance = this;
    }

    public void setLoadingText(String s) {

        View txt = findViewById(R.id.LoadingText);
        if (txt != null) ((TextView)txt).setText(s);
    }

    private void setStatusBar(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.accent_normal_color));
        }
    }
}
