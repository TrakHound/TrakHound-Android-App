package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManagement.context = getApplicationContext();

        // Attempt to login using saved credentials
        login();
    }

    private void login() {

        String token = UserManagement.getRememberToken();
        if (token != null) {

            ProgressDialog progress = new ProgressDialog(this);

            new Login(this, null, progress).execute(token);

            progress.setTitle("Logging In");
            progress.setMessage("Wait while loading...");
            progress.show();
        }
    }

    public void userLogin(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), UserLoginActivity.class));
    }

    public void localLogin(View view){

        // Open the Device List
        startActivity(new Intent(getBaseContext(), LocalLoginActivity.class));
    }

}
