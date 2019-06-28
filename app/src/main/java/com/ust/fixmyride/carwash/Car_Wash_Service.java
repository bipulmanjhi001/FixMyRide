package com.ust.fixmyride.carwash;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.brand.Pick_Your_Brand;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;
import com.ust.fixmyride.tutorialscreens.Carwashtutorial;
import com.ust.fixmyride.tutorialscreens.Hometutorial;

/**
 * Created by Bipul on 14-09-2016.
 */
public class Car_Wash_Service extends AppCompatActivity {

    com.ust.fixmyride.model.FooterBarLayout carwashsubmit;
    RelativeLayout cardetailslayout,carextriorpolishlayout,completecleanlayout,extriorcarwashlayout,interiorcleanlayout;
    ImageView cardetailsimage,carexteriorpolishimage,completecleanimage,extriorcarwashimage,interiorcleanimage;
    ImageView cardetailcheck,carexteriorpolishcheck,completecleancheck,exteriorcarwashcheck,interiorcleancheck ;
    com.ust.fixmyride.model.CircularImageView carwashservicefab;
    private Boolean a,b,c,d,e;

    CoordinatorLayout car_wash_service_coordinatorlayout;
    // flag for Internet connection status
    private Boolean isInternetPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    boolean gpsEnabled;
    String car_wash;
    SharedPreferences carwashsharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        car_wash=getIntent().getExtras().getString("car_wash");
        car_wash_service_coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_wash_service_coordinatorlayout);
        a=false;b=false;c=false;d=false;e=false;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Checking for first time launch - before calling setContentView()
        carwashsharedPreferences = getSharedPreferences("carwashPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=carwashsharedPreferences.edit();
        boolean  firstTime=carwashsharedPreferences.getBoolean("carwash", true);
        if(firstTime) {
            editor.putBoolean("carwash", false);
            editor.apply();
            TutorialScreen();
        }
        // check for Internet status
        if (!isInternetPresent && !gpsEnabled) {
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(car_wash_service_coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
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

        ////////////////////////////////////////////////////Click footer////////////////////////////////////////////////////
        carwashsubmit=(com.ust.fixmyride.model.FooterBarLayout)findViewById(R.id.carwashsubmit);
        carwashsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a == true || b == true || c == true || d == true || e == true) {
                    Intent intent = new Intent(Car_Wash_Service.this, Pick_Your_Brand.class);
                    startActivity(intent);
                    Car_Wash_Service.this.finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Choose atleast one service.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /////////////////////////////////////////////////////Call check buttons/////////////////////////////////////////////////////////////
        cardetailcheck=(ImageView)findViewById(R.id.cardetailcheck);
        carexteriorpolishcheck=(ImageView)findViewById(R.id.carexteriorpolishcheck);
        completecleancheck=(ImageView)findViewById(R.id.completecleancheck);
        exteriorcarwashcheck=(ImageView)findViewById(R.id.exteriorcarwashcheck);
        interiorcleancheck=(ImageView)findViewById(R.id.interiorcleancheck);


        /*****************************************************Call outside**************************************************/
        cardetailslayout=(RelativeLayout)findViewById(R.id.cardetailslayout);
        cardetailslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardetailslayout.setBackgroundColor(0);
                cardetailcheck.setVisibility(View.GONE);
                a=false;

            }
        });
        carextriorpolishlayout=(RelativeLayout)findViewById(R.id.carextriorpolishlayout);
        carextriorpolishlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carextriorpolishlayout.setBackgroundColor(0);
               carexteriorpolishcheck.setVisibility(View.GONE);
                b=false;
            }
        });
        completecleanlayout=(RelativeLayout)findViewById(R.id.completecleanlayout);
        completecleanlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completecleanlayout.setBackgroundColor(0);
                completecleancheck.setVisibility(View.GONE);
                c=false;
            }
        });
        extriorcarwashlayout=(RelativeLayout)findViewById(R.id.extriorcarwashlayout);
        extriorcarwashlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extriorcarwashlayout.setBackgroundColor(0);
               exteriorcarwashcheck.setVisibility(View.GONE);
                d=false;
            }
        });
        interiorcleanlayout=(RelativeLayout)findViewById(R.id.interiorcleanlayout);
        interiorcleanlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interiorcleanlayout.setBackgroundColor(0);
               interiorcleancheck.setVisibility(View.GONE);
                e=false;
            }
        });
        ////////////////////////////////////////Call logo images///////////////////////////////////////////////////////////////////////

        cardetailsimage=(ImageView)findViewById(R.id.cardetailsimage);
        cardetailsimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cardetailcheck.setVisibility(View.VISIBLE);
                    cardetailslayout.setBackgroundColor(Color.parseColor("#2f0058"));
                     a=true;
            }
        });

        carexteriorpolishimage=(ImageView)findViewById(R.id.carexteriorpolishimage);
        carexteriorpolishimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    carexteriorpolishcheck.setVisibility(View.VISIBLE);
                    carextriorpolishlayout.setBackgroundColor(Color.parseColor("#2f0058"));
                    b=true;
            }
        });
        completecleanimage=(ImageView)findViewById(R.id.completecleanimage);
        completecleanimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    completecleancheck.setVisibility(View.VISIBLE);
                    completecleanlayout.setBackgroundColor(Color.parseColor("#2f0058"));
                    c=true;
            }
        });
        extriorcarwashimage=(ImageView)findViewById(R.id.extriorcarwashimage);
        extriorcarwashimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    exteriorcarwashcheck.setVisibility(View.VISIBLE);
                    extriorcarwashlayout.setBackgroundColor(Color.parseColor("#2f0058"));
                    d=true;

            }
        });
        interiorcleanimage=(ImageView)findViewById(R.id.interiorcleanimage);
        interiorcleanimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    interiorcleancheck.setVisibility(View.VISIBLE);
                    interiorcleanlayout.setBackgroundColor(Color.parseColor("#2f0058"));
                    e=true;

            }
        });
     ///////////////////////////////////////////////////Navigation Drawer///////////////////////////////////////////////////////
        carwashservicefab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carwashservicefab);
        carwashservicefab.setOnTouchListener(new View.OnTouchListener() {
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
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        Car_Wash_Service.this.finish();
    }
    private void TutorialScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Carwashtutorial frag = new Carwashtutorial();
        frag.show(ft, "txn_tag");
    }
    }
