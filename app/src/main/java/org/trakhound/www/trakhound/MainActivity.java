package org.trakhound.www.trakhound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private TextView errorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar/ActionBar
        SetToolbar();




        // Assign EditTexts to local variables
        usernameText = (EditText)findViewById(R.id.UsernameText);
        passwordText = (EditText)findViewById(R.id.PasswordText);

        // Assign TextViews local variable
        errorLabel = (TextView)findViewById(R.id.ErrorLabel);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }

    private void SetToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle(R.string.app_name);
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);



        // Set Navigation Button Icon
//        trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        setSupportActionBar(trakhoundToolbar);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            // action with ID action_settings was selected
//            case R.id.action_settings:
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            default:
//                break;
//        }
//
//        return true;
//    }




    public void login(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        ProgressDialog progress = new ProgressDialog(this);

        new SigninActivity(this, errorLabel, progress).execute(username, password);


        progress.setTitle("Logging In");
        progress.setMessage("Wait while loading...");
        progress.show();


    }

}
