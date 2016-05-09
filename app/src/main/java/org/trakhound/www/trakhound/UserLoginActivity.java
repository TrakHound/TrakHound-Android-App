package org.trakhound.www.trakhound;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserLoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private TextView errorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Setup Toolbar/ActionBar
        SetToolbar();

        // Assign EditTexts to local variables
        usernameText = (EditText)findViewById(R.id.UsernameText);
        passwordText = (EditText)findViewById(R.id.PasswordText);

        // Assign TextViews local variable
        errorLabel = (TextView)findViewById(R.id.ErrorLabel);
    }

    private void SetToolbar() {

        // Set Toolbar
        Toolbar trakhoundToolbar = (Toolbar) findViewById(R.id.TrakhoundToolbar);

        // Set Title
        trakhoundToolbar.setTitle(R.string.app_name);
        trakhoundToolbar.setTitleTextColor(Color.WHITE);

        // Set Icon
        trakhoundToolbar.setLogo(R.drawable.th_logo_toolbar);

        // Set Navigation Button Icon
        trakhoundToolbar.setNavigationIcon(R.drawable.back_01);

        setSupportActionBar(trakhoundToolbar);
    }

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
