package com.ust.fixmyride.currentbooking;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.OrdersDrawerFragment;

/**
 * Created by Bipul on 16-09-2016.
 */
public class Current_Booking extends AppCompatActivity {
    com.ust.fixmyride.model.CircularImageView currentbookingfab;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    CoordinatorLayout coordinatorlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.current_booking_coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isInternetPresent && !gpsEnabled) {
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
            snackbarActionTextView.setTextSize(16);

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
        currentbookingfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.currentbookingfab);
        currentbookingfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                OrdersDrawerFragment frag = new OrdersDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Current_Booking.this, HomeActivity.class);
        startActivity(intent);
        Current_Booking.this.finish();
    }
}