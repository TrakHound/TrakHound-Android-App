package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.trakhound.www.trakhound.http.Requests;

public class UserHome extends AppCompatActivity {

    public static UserConfiguration userConfig;

    public static String username;
    public static String planType;
    public static String lastLogin;
    public static Bitmap userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        setOnClickListeners();
        setToolbar();

        if (userConfig == null) loadUser();

        loadData();
    }

    private void setOnClickListeners() {

        View v;

        v = findViewById(R.id.DeviceListButton);
        if (v != null) v.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openDeviceList(v);
                    }
                });

        v = findViewById(R.id.LogoutButton);
        if (v != null) v.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        logout(v);
                    }
                });
    }

    private void loadUser() {

        UserConfiguration user = ((MyApplication)(getApplication())).User;
        if (user != null) {

            String usr = user.Username;
            if (usr.length() > 0) usr = capitalize(usr);

            username = usr;
            planType = user.PlanType;
            lastLogin = user.LastLogin.toString("m/d h:MM:ss");

            new getUserImage(this, user).execute();
        }

        userConfig = user;
    }

    public void loadData() {

        TextView txt;

        txt = ((TextView)findViewById(R.id.Username));
        if (txt != null) txt.setText(username);

        txt = ((TextView)findViewById(R.id.PlanType));
        if (txt != null) txt.setText(planType);

        txt = ((TextView)findViewById(R.id.LastLogin));
        if (txt != null) txt.setText(lastLogin);

        ImageView userImageView = (ImageView)findViewById(R.id.UserImage);
        if (userImageView != null) userImageView.setImageBitmap(userImage);
    }

    public static void clearData() {

        username = null;
        planType = null;
        lastLogin = null;

        userImage = null;

        userConfig = null;

//        TextView txt;
//
//        txt = ((TextView)findViewById(R.id.Username));
//        if (txt != null) txt.setText(username);
//
//        txt = ((TextView)findViewById(R.id.PlanType));
//        if (txt != null) txt.setText(planType);
//
//        txt = ((TextView)findViewById(R.id.LastLogin));
//        if (txt != null) txt.setText(lastLogin);
//
//        ImageView img = (ImageView)findViewById(R.id.UserImage);
//        if (img != null) img.setImageBitmap(null);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public void openDeviceList(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), DeviceListActivity.class));
    }

    public void logout(View view){

        ProgressDialog progress = new ProgressDialog(this);

        new Logout(this, progress).execute();

        progress.setTitle("Logging Out");
        progress.setMessage("Please Wait...");
        progress.show();
    }

    private void setToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle("Home");
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        setSupportActionBar(trakhoundToolbar);
    }

}

class getUserImage extends AsyncTask<String,Void,Bitmap> {

    private UserHome context;

    private UserConfiguration userConfig;

    public getUserImage(UserHome context, UserConfiguration userConfig) {

        this.context = context;
        this.userConfig = userConfig;
    }

    protected void onPreExecute(){

    }

    @Override
    protected Bitmap doInBackground(String... arg0) {

        if (userConfig.Image_Url != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + userConfig.Image_Url;

            return Requests.getImage(url);
        }

        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap){

        if (bitmap != null) {

            UserHome.userImage = bitmap;

            context.loadData();
        }
    }
}
