package com.ust.fixmyride.forgotpassword;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.validate.Validate_Registration;


public class ForgotActivity extends AppCompatActivity {
    EditText forgotphonenumber,forgotusername;
    ImageView forgotloginbutton;
    private String phonenumber,username;
    private String mob_number = null;
    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.forgotcoordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        forgotusername=(EditText)findViewById(R.id.forgotusername);
        forgotphonenumber=(EditText)findViewById(R.id.forgotphonenumber);
        forgotloginbutton=(ImageView)findViewById(R.id.forgotloginbutton);

        forgotloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent && gpsEnabled) {
                    if (forgotusername.hasFocus()) {
                        UserIDvalidate();
                    } else if (forgotphonenumber.hasFocus()) {
                        PhonevalidateData();
                    } else {
                        Toast.makeText(ForgotActivity.this, "At least one field is required. ", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    // Ask user to connect to Internet
                    Snackbar snackbar = Snackbar
                            .make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            });
                    TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
                    snackbarActionTextView.setTextSize(14);

                    snackbarActionTextView.setTextColor(Color.RED);
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setMaxLines(1);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            }
        });

    }
    //Validate Data locally(Checks whether the fields are empty or not)
    private void UserIDvalidate() {

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(forgotusername.getText().toString())) {

            forgotusername.setError("Required field!");
            focusView = forgotusername;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        else
        {
            getTextValues();

        }
    }
    //Get the values from EditText
    private void getTextValues() {
        username = forgotusername.getText().toString();
        UserAuthenticated();
    }
    private void UserAuthenticated(){
        Intent intent=new Intent(ForgotActivity.this,Validate_Registration.class);
        overridePendingTransition(0,0);
        startActivity(intent);
        ForgotActivity.this.finish();
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void PhonevalidateData() {

        boolean cancel = false;
        View focusView = null;

       if (TextUtils.isEmpty(forgotphonenumber.getText().toString())) {

            forgotphonenumber.setError("Required field!");
            focusView = forgotphonenumber;
            cancel = true;
        }
        if(cancel){

            focusView.requestFocus();
        }
        else
        {
            getTextValues2();

        }
    }
    //Get the values from EditText
    private void getTextValues2() {
        phonenumber = forgotphonenumber.getText().toString();
        UserAuthenticated2();
    }
    private void UserAuthenticated2(){
        Intent intent=new Intent(ForgotActivity.this,Validate_Registration.class);
        startActivity(intent);
        ForgotActivity.this.finish();
    }
}
