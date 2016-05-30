package org.trakhound.www.trakhound;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Loading extends AppCompatActivity {

    public static String LOADING_TEXT = "loading_text";

    public static void Open(Context context, String loadingText) {

        Intent loading = new Intent(context, Loading.class);
        loading.putExtra(Loading.LOADING_TEXT, loadingText);
        context.startActivity(loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent myIntent = getIntent();
        String loadingText = myIntent.getStringExtra(LOADING_TEXT);
        setLoadingText(loadingText);
    }

    public void setLoadingText(String s) {

        View txt = findViewById(R.id.LoadingText);
        if (txt != null) ((TextView)txt).setText(s);
    }
}
