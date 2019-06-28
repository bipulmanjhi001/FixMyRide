package com.ust.fixmyride.carservicepack;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.bookingdetails.Car_Wash_Booking_Details;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;

/**
 * Created by Bipul on 19-09-2016.
 */
public class Car_Wash_Service_Pack extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout mapcontinue;
    RelativeLayout expolishlayer,exinpolishlayer,expolishandwax,inexcleaninglayout;
    TextView exteriorpolishtext,exandincleantext,expolishandwaxtext,inexcleantext;
    ImageView expoilshcheck,exinpolishcheck,expolishwaxcheck,inexinterroircleanhcheck;
    com.ust.fixmyride.model.CircularImageView carwashservicepackfab;
    private Boolean a,b,c,d;
    private CoordinatorLayout coordinatorlayout;
    // flag for Internet connection status
    private Boolean isInternetPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_service_pack);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_wash_service_pack_coordinatorlayout);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

         a=false;b=false;c=false;d=false;
        ////////////////////////////////////////////////////////Click footer////////////////////////////////////////////////////////////
        mapcontinue=(com.ust.fixmyride.model.FooterBarLayout)findViewById(R.id.addandcontinue);
        mapcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent && gpsEnabled) {
                    if (a == true || b == true || c == true || d == true) {
                        Intent intent = new Intent(Car_Wash_Service_Pack.this, Car_Wash_Booking_Details.class);
                        startActivity(intent);
                        Car_Wash_Service_Pack.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Choose atleast one service.", Toast.LENGTH_SHORT).show();
                    }
                }else{
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
                    // Changing message text color
                    snackbarActionTextView.setTextColor(Color.RED);
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                    // Changing action button text color
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

        ////////////////////////////////////////////////////////////call checkbutton for visible/////////////////////////////////////////////////////////////////////

        expoilshcheck=(ImageView)findViewById(R.id.expoilshcheck);
        exinpolishcheck=(ImageView)findViewById(R.id.exinpolishcheck);
        expolishwaxcheck=(ImageView)findViewById(R.id.expolishwaxcheck);
        inexinterroircleanhcheck=(ImageView)findViewById(R.id.inexinterroircleanhcheck);

        /////////////////////////////////////////////////////Click outside//////////////////////////////////////////////////////////////
        expolishlayer=(RelativeLayout)findViewById(R.id.expolishlayer);
        expolishlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expolishlayer.setBackgroundResource(0);
               expoilshcheck.setVisibility(View.GONE);
                a=false;

            }
        });
        exinpolishlayer=(RelativeLayout)findViewById(R.id.exinpolishlayer);
        exinpolishlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exinpolishlayer.setBackgroundResource(0);
                exinpolishcheck.setVisibility(View.GONE);
                b=false;

            }
        });
        expolishandwax=(RelativeLayout)findViewById(R.id.expolishandwax);
        expolishandwax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expolishandwax.setBackgroundResource(0);
                expolishwaxcheck.setVisibility(View.GONE);
                c=false;

            }
        });
        inexcleaninglayout=(RelativeLayout)findViewById(R.id.inexcleaninglayout);
        inexcleaninglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inexcleaninglayout.setBackgroundResource(0);
                inexinterroircleanhcheck.setVisibility(View.GONE);
                d=false;

            }
        });
        ////////////////////////////////////////////////////click text package name/////////////////////////////////////////////////////////////
        exteriorpolishtext=(TextView)findViewById(R.id.exteriorpolishtext);
        exteriorpolishtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expoilshcheck.getVisibility()== View.GONE) {
                    expoilshcheck.setVisibility(View.VISIBLE);
                    expolishlayer.setBackgroundResource(R.color.colorPrimaryDark);
                    a=true;
                }
            }
        });
        exandincleantext=(TextView)findViewById(R.id.exandincleantext);
        exandincleantext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exinpolishcheck.getVisibility()==View.GONE) {
                    exinpolishcheck.setVisibility(View.VISIBLE);
                    exinpolishlayer.setBackgroundResource(R.color.colorPrimaryDark);
                    b=true;
                }
            }
        });
        expolishandwaxtext=(TextView)findViewById(R.id.expolishandwaxtext);
        expolishandwaxtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expolishwaxcheck.getVisibility()==View.GONE) {
                    expolishwaxcheck.setVisibility(View.VISIBLE);
                    expolishandwax.setBackgroundResource(R.color.colorPrimaryDark);
                    c=true;
                }
            }
        });
        inexcleantext=(TextView)findViewById(R.id.inexcleantext);
        inexcleantext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inexinterroircleanhcheck.getVisibility()==View.GONE) {
                    inexinterroircleanhcheck.setVisibility(View.VISIBLE);
                    inexcleaninglayout.setBackgroundResource(R.color.colorPrimaryDark);
                    d=true;
                }
            }
        });
        ///////////////////////////////////////////////////////////Navigation Drawer/////////////////////////////////////////////////////////////////////////////

        carwashservicepackfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carwashservicepackfab);
        carwashservicepackfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, Car_Wash_Service_Pack.class);
        startActivity(intent);
        finish();
    }
}
