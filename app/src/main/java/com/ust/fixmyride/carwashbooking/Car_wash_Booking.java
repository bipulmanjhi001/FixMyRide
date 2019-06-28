package com.ust.fixmyride.carwashbooking;

import android.app.FragmentTransaction;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.map.MapsActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;

/**
 * Created by Bipul on 15-09-2016.
 */
public class Car_wash_Booking extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout continuebooking;
    com.ust.fixmyride.model.CircularImageView carwashservicebookingfab;
    EditText carwashservicebookingaddnotes;
    String notes;

    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_service_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_wash_service_booking);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        continuebooking=(com.ust.fixmyride.model.FooterBarLayout)findViewById(R.id.carbookingcontinue);
        continuebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfield();
            }
        });
        carwashservicebookingfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carwashservicebookingfab);
        carwashservicebookingfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
        carwashservicebookingaddnotes=(EditText)findViewById(R.id.carwashservicebookingaddnotes);
    }
    private void checkfield(){
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(carwashservicebookingaddnotes.getText().toString()))
        {
            carwashservicebookingaddnotes.setError("Required field!");
            focusView = carwashservicebookingaddnotes;
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
        notes = carwashservicebookingaddnotes.getText().toString();

        // check for Internet status
        if (isInternetPresent && gpsEnabled) {
            showSuccessDialog();
        }

        else{
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

    //Show Success Dialog
    private void showSuccessDialog(){
        Intent intent=new Intent(Car_wash_Booking.this,MapsActivity.class);
        startActivity(intent);
        Car_wash_Booking.this.finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Car_wash_Booking.class);
        startActivity(intent);
        finish();
    }

}
